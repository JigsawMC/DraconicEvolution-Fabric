package com.brandon3055.draconicevolution.client.sound;

import codechicken.lib.math.MathHelper;
import com.brandon3055.draconicevolution.blocks.reactor.tileentity.TileReactorCore;
import com.brandon3055.draconicevolution.handlers.DESounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Created by brandon3055 on 4/10/2015.
 */
public class ReactorSound extends SimpleSound implements ITickableSound {
    public boolean donePlaying = false;
    private TileReactorCore tile;
    private float targetPitch;
    private float targetVolume;
    private int stopTimer = 0;

    public ReactorSound(TileReactorCore tile) {
        super(DESounds.coreSound, SoundCategory.BLOCKS, tile.reactorState.get() == TileReactorCore.ReactorState.BEYOND_HOPE ? 10F : 1.5F, 1, tile.getBlockPos());
        this.tile = tile;
        this.looping = true;
        this.targetPitch = 1F;
    }

    @Override
    public boolean isStopped() {
        return donePlaying;
    }

    @Override
    public void tick() {
        if (tile.roller != null) {
            x = (float) tile.roller.pos.x;
            y = (float) tile.roller.pos.y;
            z = (float) tile.roller.pos.z;
        }
        else {
            x = (float) tile.getBlockPos().getX() + 0.5F;
            y = (float) tile.getBlockPos().getY() + 0.5F;
            z = (float) tile.getBlockPos().getZ() + 0.5F;
        }


        pitch = (float) MathHelper.approachExp(pitch, targetPitch, 0.05);
        if (tile.reactorState.get() == TileReactorCore.ReactorState.WARMING_UP || tile.reactorState.get() == TileReactorCore.ReactorState.STOPPING || tile.reactorState.get() == TileReactorCore.ReactorState.COOLING) {
            targetPitch = 0.5F + (tile.shieldAnimationState / 2F);
        }
        else if (tile.reactorState.get() == TileReactorCore.ReactorState.RUNNING) {
            targetPitch = 1F + (float) Math.max(0, Math.min(0.5, 1 - ((tile.shieldCharge.get() / tile.maxShieldCharge.get()) * 10)));
        }
        else if (tile.reactorState.get() == TileReactorCore.ReactorState.BEYOND_HOPE) {
            if (volume == 1.5F) {
                donePlaying = true;
            }
            if (tile.getLevel().random.nextInt(10) == 0) {
                targetPitch = 1F + (tile.getLevel().random.nextFloat() / 2F);
            }
        }


        PlayerEntity player = Minecraft.getInstance().player;
        if (tile.isRemoved() || player == null || player.distanceToSqr(Vector3d.atLowerCornerOf(tile.getBlockPos())) > (volume > 1.5F ? 4096 : 512)){
//            if (stopTimer++ == 60) {
                donePlaying = true;
                looping = false;
//            }
        }
    }
}
