package com.brandon3055.draconicevolution.client.render.tile;

import com.brandon3055.draconicevolution.blocks.tileentity.StabilizedSpawnerLogic;
import com.brandon3055.draconicevolution.blocks.tileentity.TileStabilizedSpawner;
import com.brandon3055.draconicevolution.init.DEContent;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Created by brandon3055 on 20/05/2016.
 */
public class RenderTileStabilizedSpawner extends TileEntityRenderer<TileStabilizedSpawner> {

    private static ItemStack[] CORE_RENDER_ITEMS = new ItemStack[]{new ItemStack(DEContent.core_draconium), new ItemStack(DEContent.core_wyvern), new ItemStack(DEContent.core_awakened), new ItemStack(DEContent.core_chaotic)};

    public RenderTileStabilizedSpawner(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileStabilizedSpawner tile, float partialTicks, MatrixStack mStack, IRenderTypeBuffer getter, int packedLight, int packedOverlay) {
        StabilizedSpawnerLogic spawnerLogic = tile.spawnerLogic;

        mStack.pushPose();
        mStack.translate(0.5D, 0.0D, 0.5D);
        Entity entity = spawnerLogic.getOrCreateDisplayEntity();
        if (entity != null) {
            float f = 0.53125F;
            float f1 = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) f1 > 1.0D) {
                f /= f1;
            }





            mStack.translate(0.0D, (double) 0.4F, 0.0D);
            mStack.mulPose(Vector3f.YP.rotationDegrees((float)MathHelper.lerp((double)partialTicks, spawnerLogic.getoSpin(), spawnerLogic.getSpin()) * 10.0F));
            mStack.translate(0.0D, (double) -0.2F, 0.0D);
            mStack.mulPose(Vector3f.XP.rotationDegrees(-30.0F));
            mStack.scale(f, f, f);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, mStack, getter, packedLight);
        }
        mStack.popPose();

        mStack.translate(0.5, 1F-(1F/32F), 0.5);
        mStack.scale(0.75F, 0.75F, 0.75F);
        mStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));

        ItemStack stack = CORE_RENDER_ITEMS[tile.spawnerTier.get().ordinal()];
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.FIXED, packedLight, packedOverlay, mStack, getter);
    }
}
