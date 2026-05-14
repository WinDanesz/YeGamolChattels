package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.TileEntityLootChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileEntityRendererLootChest extends TileEntitySpecialRenderer<TileEntityLootChest>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            YeGamolChattels.MODID, "textures/mod/loot_chest.png");

    private final ModelLootChest model = new ModelLootChest();

    @Override
    public void render(TileEntityLootChest te, double x, double y, double z,
                       float partialTicks, int destroyStage, float alpha)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        bindTexture(TEXTURE);
        model.render(te);
        GlStateManager.popMatrix();
    }
}
