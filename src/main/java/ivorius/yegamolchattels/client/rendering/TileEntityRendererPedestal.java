package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.EnumPedestalEntry;
import ivorius.yegamolchattels.blocks.TileEntityPedestal;
import ivorius.yegamolchattels.multiblock.IvMultiBlockRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;

public class TileEntityRendererPedestal extends TileEntitySpecialRenderer<TileEntityPedestal>
{
    private final Map<EnumPedestalEntry, ModelBase> models = new EnumMap<>(EnumPedestalEntry.class);
    private final Map<EnumPedestalEntry, ResourceLocation> textures = new EnumMap<>(EnumPedestalEntry.class);

    public void registerModel(EnumPedestalEntry entry, ModelBase model)
    {
        models.put(entry, model);
        textures.put(entry, new ResourceLocation(YeGamolChattels.MODID, "textures/mod/pedestal_" + entry.getIntIdentifier() + ".png"));
    }

    @Override
    public void render(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        EnumPedestalEntry entry = te.getPedestalEntry();
        if (!te.isParent() || entry == null)
            return;

        ModelBase model = models.get(entry);
        ResourceLocation texture = textures.get(entry);
        if (model == null || texture == null)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.5f, 0.05f);
        IvMultiBlockRenderHelper.transformFor(te, x, y, z);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);

        EntityTippedArrow emptyEntity = new EntityTippedArrow(te.getWorld());
        emptyEntity.ticksExisted = te.ticksAlive;
        emptyEntity.rotationYaw = te.ticksAlive + partialTicks;

        bindTexture(texture);
        model.render(emptyEntity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();

        ItemStack storedItem = te.storedItems[0];
        if (storedItem != null && !storedItem.isEmpty())
        {
            GlStateManager.pushMatrix();
            float fractionUp = te.getFractionItemUp();
            float hover = entry == EnumPedestalEntry.woodPedestal ? 0.2f : 0.45f + fractionUp * 0.2f;
            GlStateManager.translate(0.0f, hover, 0.0f);
            GlStateManager.rotate((te.ticksAlive + partialTicks) * 2.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.scale(0.6f, 0.6f, 0.6f);
            Minecraft.getMinecraft().getRenderItem().renderItem(storedItem, ItemCameraTransforms.TransformType.GROUND);
            GlStateManager.popMatrix();
        }

        GlStateManager.popMatrix();
    }
}
