package com.brandon3055.draconicevolution.blocks.tileentity;

import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.lib.IChangeListener;
import com.brandon3055.brandonscore.lib.IInteractTile;
import com.brandon3055.brandonscore.lib.datamanager.*;
import com.brandon3055.brandonscore.utils.InventoryUtils;
import com.brandon3055.draconicevolution.DEConfig;
import com.brandon3055.draconicevolution.init.DEContent;
import com.brandon3055.draconicevolution.items.ItemCore;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;

import java.util.Random;

/**
 * Created by brandon3055 on 28/09/2016.
 */
public class TileStabilizedSpawner extends TileBCore implements ITickableTileEntity, IInteractTile, IChangeListener {

    public ManagedEnum<SpawnerTier> spawnerTier = register(new ManagedEnum<>("spawner_tier", SpawnerTier.BASIC, DataFlags.SAVE_BOTH_SYNC_TILE));
    public ManagedStack mobSoul = register(new ManagedStack("mob_soul", DataFlags.SAVE_BOTH_SYNC_TILE));
    public ManagedBool isPowered = register(new ManagedBool("is_powered", DataFlags.SAVE_NBT_SYNC_TILE));
    public ManagedShort spawnDelay = register(new ManagedShort("spawn_delay", (short) 100, DataFlags.SAVE_NBT_SYNC_TILE));
    public ManagedInt startSpawnDelay = register(new ManagedInt("start_spawn_delay", 100, DataFlags.SAVE_NBT_SYNC_TILE));
    public StabilizedSpawnerLogic spawnerLogic = new StabilizedSpawnerLogic(this);

    private int activatingRangeFromPlayer = 24;
//    private int spawnRange = 4;

    //region Render Fields

    public double mobRotation;

    public TileStabilizedSpawner() {
        super(DEContent.tile_stabilized_spawner);
    }

    //endregion


    @Override
    public void tick() {
        super.tick();
        spawnerLogic.tick();
    }


    public boolean isActive() {
        if (isPowered.get() || mobSoul.get().isEmpty()) {
            return false;
        } else if (spawnerTier.get().requiresPlayer && !level.hasNearbyAlivePlayer(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D, (double) this.activatingRangeFromPlayer)) {
            return false;
        }
        return true;
    }

    @Override
    public void onNeighborChange(BlockPos changePos) {
        isPowered.set(level.hasNeighborSignal(worldPosition));
    }

    @Override
    public boolean onBlockActivated(BlockState state, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == DEContent.mob_soul) {
            if (!level.isClientSide) {
                (mobSoul.set(stack.copy())).setCount(1);
                if (!player.isCreative()) {
                    InventoryUtils.consumeHeldItem(player, stack, hand);
                }
            }
            return true;
        } else if (stack.getItem() instanceof SpawnEggItem) {
            EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
            ItemStack soul = new ItemStack(DEContent.mob_soul);
            DEContent.mob_soul.setEntity(type.getRegistryName(), soul);
            mobSoul.set(soul);
            if (!player.abilities.instabuild) {
                stack.shrink(1);
            }
            return true;
        } else if (!stack.isEmpty()) {
            SpawnerTier prevTier = spawnerTier.get();
            if (stack.getItem() == DEContent.core_draconium) {
                if (spawnerTier.get() == SpawnerTier.BASIC) return false;
                spawnerTier.set(SpawnerTier.BASIC);
            } else if (stack.getItem() == DEContent.core_wyvern) {
                if (spawnerTier.get() == SpawnerTier.WYVERN) return false;
                spawnerTier.set(SpawnerTier.WYVERN);
            } else if (stack.getItem() == DEContent.core_awakened) {
                if (spawnerTier.get() == SpawnerTier.DRACONIC) return false;
                spawnerTier.set(SpawnerTier.DRACONIC);
            } else if (stack.getItem() == DEContent.core_chaotic) {
                if (spawnerTier.get() == SpawnerTier.CHAOTIC) return false;
                spawnerTier.set(SpawnerTier.CHAOTIC);
            } else {
                return false;
            }

            ItemStack dropStack = ItemStack.EMPTY;
            switch (prevTier) {
                case BASIC:
                    dropStack = new ItemStack(DEContent.core_draconium);
                    break;
                case WYVERN:
                    dropStack = new ItemStack(DEContent.core_wyvern);
                    break;
                case DRACONIC:
                    dropStack = new ItemStack(DEContent.core_awakened);
                    break;
                case CHAOTIC:
                    dropStack = new ItemStack(DEContent.core_chaotic);
                    break;
            }
            if (!level.isClientSide && !player.abilities.instabuild) {
                ItemEntity entityItem = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, dropStack);
                entityItem.setDeltaMovement(entityItem.getDeltaMovement().x, 0.2, entityItem.getDeltaMovement().z);
                ;
                level.addFreshEntity(entityItem);
                InventoryUtils.consumeHeldItem(player, stack, hand);
            }
        }

        return false;
    }

    @Override
    public void writeToItemStack(CompoundNBT compound, boolean willHarvest) {
        if (willHarvest) {
            mobSoul.set(ItemStack.EMPTY);
        }
        super.writeToItemStack(compound, willHarvest);
    }

    //region Render

    protected Entity getRenderEntity() {
        if (mobSoul.get().isEmpty()) {
            return null;
        }
        return DEContent.mob_soul.getRenderEntity(mobSoul.get());
    }

//    public double getRotationSpeed() {
//        return isActive() ? 0.5 + (1D - ((double) spawnDelay.value / (double) startSpawnDelay.value)) * 4.5 : 0;
//    }

    //endregion

    //region Spawner Tier

    public enum SpawnerTier {
        BASIC(4, true, false),
        WYVERN(6, false, false),
        DRACONIC(8, false, true),
        CHAOTIC(12, false, true);

        private int spawnCount;
        private boolean requiresPlayer;
        private boolean ignoreSpawnReq;

        SpawnerTier(int spawnCount, boolean requiresPlayer, boolean ignoreSpawnReq) {
            this.spawnCount = spawnCount;
            this.requiresPlayer = requiresPlayer;
            this.ignoreSpawnReq = ignoreSpawnReq;
        }

        public int getRandomSpawnDelay(Random random) {
            int min = getMinDelay();
            int max = getMaxDelay();
            return min + random.nextInt(max - min);
        }

        public int getMinDelay() {
            return DEConfig.spawnerDelays[ordinal() * 2];
        }

        public int getMaxDelay() {
            return DEConfig.spawnerDelays[(ordinal() * 2) + 1];
        }

        public int getSpawnCount() {
            return spawnCount;
        }

        public boolean ignoreSpawnReq() {
            return ignoreSpawnReq;
        }

        public boolean requiresPlayer() {
            return requiresPlayer;
        }

        public int getMaxCluster() {
            return (int) (spawnCount * 3D);
        }

        public static SpawnerTier getTierFromCore(ItemCore core) {
            return core == DEContent.core_chaotic ? CHAOTIC : core == DEContent.core_wyvern ? WYVERN : core == DEContent.core_awakened ? DRACONIC : SpawnerTier.BASIC;
        }
    }
    //endregion
}
