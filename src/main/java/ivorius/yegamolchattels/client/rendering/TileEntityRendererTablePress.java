package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.TileEntityTablePress;
import ivorius.yegamolchattels.multiblock.IvMultiBlockRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class TileEntityRendererTablePress extends TileEntitySpecialRenderer<TileEntityTablePress>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/table_press.png");

    private final ModelTablePress model = new ModelTablePress();

    @Override
    public void render(TileEntityTablePress te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (!te.isParent())
            return;

        GlStateManager.pushMatrix();
        IvMultiBlockRenderHelper.transformFor(te, x, y, z);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        bindTexture(TEXTURE);
        model.render(null, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();

        ItemStack stack = te.containedItem;
        if (stack != null && !stack.isEmpty())
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0f, -0.2f, 0.0f);
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(0.8f, 0.8f, 0.8f);
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }
}
