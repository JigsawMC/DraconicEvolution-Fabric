package com.brandon3055.draconicevolution.client.render.particle;

import com.brandon3055.brandonscore.client.particle.BCParticle;
import com.brandon3055.brandonscore.lib.Vec3D;
import net.minecraft.client.world.ClientWorld;

public class ParticleStarSpark extends BCParticle {

    public float sparkSize = 0.5F;

    public ParticleStarSpark(ClientWorld worldIn, Vec3D pos) {
        super(worldIn, pos);

        double speed = 0.1;
        this.xd = (-0.5 + random.nextDouble()) * speed;
        this.yd = (-0.5 + random.nextDouble()) * speed;
        this.zd = (-0.5 + random.nextDouble()) * speed;

//        this.particleMaxAge = 10 + rand.nextInt(10);
//        this.particleTextureIndexY = 1;
    }

//    @Override
//    public boolean shouldDisableDepth() {
//        return true;
//    }

    @Override
    public void tick() {
//        if (particleAge++ > particleMaxAge) {
//            setExpired();
//        }
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        yd += gravity;

//        particleTextureIndexX = rand.nextInt(5);
//        int ttd = particleMaxAge - particleAge;
//        if (ttd < 10) {
//            particleScale = (ttd / 10F) * baseScale;
//        }
//        if (ttd < 0) {
//            particleScale = sparkSize;
//        }

        xd *= 1 - airResistance;
        yd *= 1 - airResistance;
        zd *= 1 - airResistance;

        moveEntityNoClip(xd, yd, zd);
    }

//    @Override
//    public void renderParticle(BufferBuilder vertexbuffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
//        if (particleAge == 0) {
//            return;
//        }
//        float minU = (float) this.particleTextureIndexX / 8.0F;
//        float maxU = minU + 0.125F;
//        float minV = (float) this.particleTextureIndexY / 8.0F;
//        float maxV = minV + 0.125F;
//        float scale = 0.1F * this.particleScale;
//
//        float renderX = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTicks - interpPosX);
//        float renderY = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTicks - interpPosY);
//        float renderZ = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTicks - interpPosZ);
//        int brightnessForRender = this.getBrightnessForRender(partialTicks);
//        int j = brightnessForRender >> 16 & 65535;
//        int k = brightnessForRender & 65535;
//        vertexbuffer.pos((double) (renderX - rotationX * scale - rotationXY * scale), (double) (renderY - rotationZ * scale), (double) (renderZ - rotationYZ * scale - rotationXZ * scale)).tex((double) maxU, (double) maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        vertexbuffer.pos((double) (renderX - rotationX * scale + rotationXY * scale), (double) (renderY + rotationZ * scale), (double) (renderZ - rotationYZ * scale + rotationXZ * scale)).tex((double) maxU, (double) minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        vertexbuffer.pos((double) (renderX + rotationX * scale + rotationXY * scale), (double) (renderY + rotationZ * scale), (double) (renderZ + rotationYZ * scale + rotationXZ * scale)).tex((double) minU, (double) minV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//        vertexbuffer.pos((double) (renderX + rotationX * scale - rotationXY * scale), (double) (renderY - rotationZ * scale), (double) (renderZ + rotationYZ * scale - rotationXZ * scale)).tex((double) minU, (double) maxV).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
//    }
}