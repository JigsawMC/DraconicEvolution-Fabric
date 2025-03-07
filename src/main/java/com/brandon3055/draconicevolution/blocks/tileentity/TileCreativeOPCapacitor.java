package com.brandon3055.draconicevolution.blocks.tileentity;

import com.brandon3055.brandonscore.BrandonsCore;
import com.brandon3055.brandonscore.api.power.IOPStorage;
import com.brandon3055.brandonscore.blocks.TileBCore;
import com.brandon3055.brandonscore.capability.CapabilityOP;
import com.brandon3055.brandonscore.lib.IInteractTile;
import com.brandon3055.brandonscore.lib.datamanager.DataFlags;
import com.brandon3055.brandonscore.lib.datamanager.ManagedLong;
import com.brandon3055.brandonscore.utils.Utils;
import com.brandon3055.draconicevolution.init.DEContent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;

/**
 * Created by brandon3055 on 19/07/2016.
 */
public class TileCreativeOPCapacitor extends TileBCore implements ITickableTileEntity, IInteractTile {

    private final ManagedLong powerRate = register(new ManagedLong("power_rate", 1000000000, DataFlags.SAVE_NBT));

    public TileCreativeOPCapacitor() {
        super(DEContent.tile_creative_op_capacitor);

        capManager.set(CapabilityOP.OP, new IOPStorage() {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                return (int) Math.min(maxReceive, powerRate.get());
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                return (int) Math.min(maxExtract, powerRate.get());
            }

            @Override
            public int getEnergyStored() {
                return Integer.MAX_VALUE / 2;
            }

            @Override
            public int getMaxEnergyStored() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean canExtract() {
                return true;
            }

            @Override
            public boolean canReceive() {
                return true;
            }

            @Override
            public long getOPStored() {
                return Long.MAX_VALUE / 2;
            }

            @Override
            public long getMaxOPStored() {
                return Long.MAX_VALUE;
            }

            @Override
            public long receiveOP(long maxReceive, boolean simulate) {
                return Math.min(maxReceive, powerRate.get());
            }

            @Override
            public long extractOP(long maxExtract, boolean simulate) {
                return Math.min(maxExtract, powerRate.get());
            }
        });

    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            for (Direction direction : Direction.values()) {
                sendEnergyTo(level, worldPosition, powerRate.get(), direction);
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!level.isClientSide) {
            if (player.isShiftKeyDown()) {
                powerRate.divide(10);
            } else {
                if (powerRate.get() == Long.MAX_VALUE) {
                    powerRate.set(1);
                }else {
                    powerRate.multiply(10);
                }
            }
            if (powerRate.get() < 1) {
                powerRate.set(Long.MAX_VALUE);
            }

            BrandonsCore.proxy.sendIndexedMessage(player, new StringTextComponent("Power Rate: " + Utils.addCommas(powerRate.get()) + " OP/t"), 42);
//            player.sendMessage(new StringTextComponent("Power Rate: " + Utils.addCommas(powerRate.get()) + " OP/t"));
        }
        return true;
    }
}
