package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.TileEntityWeaponRack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileEntityRendererWeaponRack extends TileEntitySpecialRenderer<TileEntityWeaponRack>
{
    private static final ResourceLocation TEXTURE_FLOOR = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture.png");
    private static final ResourceLocation TEXTURE_WALL = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/weapon_rack_texture_wall.png");

    private final ModelWeaponRack modelFloor = new ModelWeaponRack();
    private final ModelWeaponRackWall modelWall = new ModelWeaponRackWall();

    @Override
    public void render(TileEntityWeaponRack te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.rotate(te.direction * 90.0f, 0.0f, 1.0f, 0.0f);
        int type = te.getBlockMetadata() & 1;
        if (type == 1)
        {
            bindTexture(TEXTURE_WALL);
            modelWall.render(null, 0, 0, 0, 0, 0, 0.0625f);
        }
        else
        {
            bindTexture(TEXTURE_FLOOR);
            modelFloor.render(null, 0, 0, 0, 0, 0, 0.0625f);
        }
        GlStateManager.popMatrix();
    }
}
