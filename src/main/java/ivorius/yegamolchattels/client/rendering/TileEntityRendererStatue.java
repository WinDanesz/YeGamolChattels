package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.blocks.Statue;
import ivorius.yegamolchattels.blocks.TileEntityStatue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class TileEntityRendererStatue extends TileEntitySpecialRenderer<TileEntityStatue>
{
    @Override
    public void render(TileEntityStatue te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (!te.isParent() || te.getStatue() == null || te.getWorld() == null)
            return;

        Statue statue = te.getStatue();
        Entity entity = statue.getEntity();
        if (entity == null)
            return;

        statue.updateEntityRotations();

        double[] center = te.getActiveCenterCoords();
        entity.setPosition(center[0], center[1] - te.centerCoordsSize[1], center[2]);

        int color = statue.getMaterial().getState().getMapColor(te.getWorld(), new BlockPos(center[0], center[1], center[2])).colorValue;
        float red = ((color >> 16) & 255) / 255.0f;
        float green = ((color >> 8) & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + center[0] - te.xCoord, y + center[1] - te.yCoord - te.centerCoordsSize[1], z + center[2] - te.zCoord);
        GlStateManager.color(red, green, blue, 1.0f);

        RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
        renderManager.setRenderShadow(false);
        renderManager.renderEntity(entity, 0.0, 0.0, 0.0, entity.rotationYaw, partialTicks, false);
        renderManager.setRenderShadow(true);

        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
}
