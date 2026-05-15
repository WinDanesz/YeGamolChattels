package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.TileEntitySawBench;
import ivorius.yegamolchattels.multiblock.IvMultiBlockRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TileEntityRendererSawBench extends TileEntitySpecialRenderer<TileEntitySawBench>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/saw_bench.png");

    private final ModelSawBench model = new ModelSawBench();

    @Override
    public void render(TileEntitySawBench te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (!te.isParent())
            return;

        GlStateManager.pushMatrix();
        IvMultiBlockRenderHelper.transformFor(te, x, y, z);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(-0.5f, -0.501f, 0.5f);
        bindTexture(TEXTURE);
        model.render(null, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();

        ItemStack stack = te.containedItem;
        if (stack != null && !stack.isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.1f, -0.33f, 0.0f);
            GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.scale(0.85f, 0.85f, 0.85f);
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }
}
