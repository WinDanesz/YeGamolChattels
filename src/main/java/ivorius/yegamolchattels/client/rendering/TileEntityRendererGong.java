package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.blocks.BlockGong;
import ivorius.yegamolchattels.blocks.TileEntityGong;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileEntityRendererGong extends TileEntitySpecialRenderer<TileEntityGong>
{
    private static final ResourceLocation TEX_SMALL  = new ResourceLocation("yegamolchattels", "textures/mod/gong_small.png");
    private static final ResourceLocation TEX_MEDIUM = new ResourceLocation("yegamolchattels", "textures/mod/gong_medium.png");
    private static final ResourceLocation TEX_LARGE  = new ResourceLocation("yegamolchattels", "textures/mod/gong_large.png");

    private final ModelGongSmall  modelSmall  = new ModelGongSmall();
    private final ModelGongMedium modelMedium = new ModelGongMedium();
    private final ModelGongLarge  modelLarge  = new ModelGongLarge();

    @Override
    public void render(TileEntityGong te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        int size = 0;
        if (te.getWorld() != null && te.getPos() != null)
        {
            Block b = te.getWorld().getBlockState(te.getPos()).getBlock();
            if (b instanceof BlockGong)
                size = ((BlockGong) b).gongSize;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.rotate(te.direction * 90.0f, 0.0f, 1.0f, 0.0f);

        final float scale = 0.0625f;
        switch (size)
        {
            case 2:
                bindTexture(TEX_LARGE);
                renderLarge(scale);
                break;
            case 1:
                bindTexture(TEX_MEDIUM);
                renderMedium(scale);
                break;
            default:
                bindTexture(TEX_SMALL);
                renderSmall(scale);
                break;
        }

        GlStateManager.popMatrix();
    }

    private void renderSmall(float s)
    {
        modelSmall.gong1.render(s);
        modelSmall.gong2.render(s);
        modelSmall.gong3.render(s);
        modelSmall.nipple.render(s);
        modelSmall.ropeleft.render(s);
        modelSmall.roperight.render(s);
    }

    private void renderMedium(float s)
    {
        modelMedium.gong1.render(s);
        modelMedium.gong2.render(s);
        modelMedium.gong3.render(s);
        modelMedium.gong4.render(s);
        modelMedium.gong5.render(s);
        modelMedium.nipple.render(s);
        modelMedium.roperight.render(s);
        modelMedium.ropeleft.render(s);
        modelMedium.ropemid.render(s);
    }

    private void renderLarge(float s)
    {
        modelLarge.gong1.render(s);
        modelLarge.gong2.render(s);
        modelLarge.gong3.render(s);
        modelLarge.gong4.render(s);
        modelLarge.gong5.render(s);
        modelLarge.gong6.render(s);
        modelLarge.gong7.render(s);
        modelLarge.nipplemid1.render(s);
        modelLarge.nipplemid2.render(s);
        modelLarge.nipplemid3.render(s);
        modelLarge.ultimatenipple.render(s);
        modelLarge.roperight1.render(s);
        modelLarge.ropeleft1.render(s);
        modelLarge.sidenipple1.render(s);
        modelLarge.sidenipple2.render(s);
        modelLarge.sidenipple3.render(s);
        modelLarge.sidenipple4.render(s);
        modelLarge.sidenipple5.render(s);
        modelLarge.sidenipple6.render(s);
        modelLarge.sidenipple7.render(s);
        modelLarge.sidenipple8.render(s);
        modelLarge.ropeleft2.render(s);
        modelLarge.roperight2.render(s);
        modelLarge.ropemid.render(s);
    }
}
