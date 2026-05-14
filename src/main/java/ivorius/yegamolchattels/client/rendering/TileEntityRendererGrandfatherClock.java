package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.TileEntityGrandfatherClock;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileEntityRendererGrandfatherClock extends TileEntitySpecialRenderer<TileEntityGrandfatherClock>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/grandfather_clock_texture.png");

    private final ModelGrandfatherClock model = new ModelGrandfatherClock();

    @Override
    public void render(TileEntityGrandfatherClock te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        bindTexture(TEXTURE);
        model.renderForTESR(te.clockTimeShown, te.pendulumTimeShown, 0.0625f);
        GlStateManager.popMatrix();
    }
}
