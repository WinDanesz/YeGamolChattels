package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.TileEntityGrindstone;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.ResourceLocation;

public class TileEntityRendererGrindstone extends TileEntitySpecialRenderer<TileEntityGrindstone>
{
    private final ModelBase modelBase = new ModelGrindstoneBase();
    private final ModelBase modelStone = new ModelGrindstoneStone();

    private final ResourceLocation textureBase = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/grindstone_base.png");
    private final ResourceLocation textureStone = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/grindstone_stone.png");

    @Override
    public void render(TileEntityGrindstone te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        int meta = te.getBlockMetadata();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y + 1.5f, (float) z + 0.5F);
        GlStateManager.rotate(-90.0f * (meta >> 1) + 180.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);

        EntityTippedArrow emptyEntity = new EntityTippedArrow(te.getWorld());

        bindTexture(textureBase);
        emptyEntity.rotationYaw = te.crankRotationVisual + (te.crankRotationTime > 0 ? partialTicks * 0.4f : 0.0f);
        modelBase.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);

        if (te.grindstoneHealth > 0)
        {
            float grindstoneRotation = (te.grindstoneRotationVisual + te.grindstoneRotationSpeed * partialTicks) * 0.0009f;
            emptyEntity.rotationYaw = grindstoneRotation;

            float scale = (float) te.grindstoneHealth / (float) TileEntityGrindstone.maxGrindstoneHealth * 0.8f + 0.2f;
            GlStateManager.scale(1.0f, scale, scale);
            GlStateManager.translate(0.0f, 0.8f / scale - 0.8f, 0.0f);
            bindTexture(textureStone);
            modelStone.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        }

        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}
