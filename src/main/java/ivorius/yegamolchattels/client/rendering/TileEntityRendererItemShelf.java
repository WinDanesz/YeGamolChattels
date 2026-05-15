package ivorius.yegamolchattels.client.rendering;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.EntityShelfInfo;
import ivorius.yegamolchattels.blocks.TileEntityItemShelf;
import ivorius.yegamolchattels.blocks.TileEntityItemShelfModel0;
import ivorius.yegamolchattels.multiblock.IvMultiBlockRenderHelper;
import ivorius.yegamolchattels.raytracing.IvRaytraceableAxisAlignedBox;
import ivorius.yegamolchattels.raytracing.IvRaytraceableObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class TileEntityRendererItemShelf extends TileEntitySpecialRenderer<TileEntityItemShelf>
{
    private final ResourceLocation[][] shelfTextures = new ResourceLocation[3][];
    private final ModelBase[][] shelfModels = new ModelBase[3][];

    public TileEntityRendererItemShelf()
    {
        shelfModels[0] = new ModelBase[]{new ModelJamiensShelf()};
        shelfTextures[0] = new ResourceLocation[]{new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "jamiens_shelf.png")};
        shelfModels[1] = new ModelBase[]{new ModelShelfWall(), new ModelShelfWall1()};
        shelfTextures[1] = new ResourceLocation[]{new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "shelf_wall.png"), new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "shelf_wall_1.png")};
        shelfModels[2] = new ModelBase[]{new ModelWardrobe()};
        shelfTextures[2] = new ResourceLocation[]{new ResourceLocation(YeGamolChattels.MODID, YeGamolChattels.filePathTextures + "wardrobe.png")};
    }

    @Override
    public void render(TileEntityItemShelf te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (!(te instanceof TileEntityItemShelfModel0) || !te.isParent())
            return;

        TileEntityItemShelfModel0 tileEntity = (TileEntityItemShelfModel0) te;
        int shelfType = tileEntity.getShelfType();

        GlStateManager.pushMatrix();
        IvMultiBlockRenderHelper.transformFor(tileEntity, x, y, z);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);

        float[] triggerValues = tileEntity.getTriggerValues(partialTicks);
        ModelBase model = shelfModels[shelfType][Math.floorMod(tileEntity.randomSeed, shelfModels[shelfType].length)];
        ResourceLocation texture = shelfTextures[shelfType][Math.floorMod(tileEntity.randomSeed, shelfTextures[shelfType].length)];

        if (shelfType == TileEntityItemShelfModel0.SHELF_JAMIEN)
            GlStateManager.translate(-0.5f, -1.0f, 0.0f);
        else if (shelfType == TileEntityItemShelfModel0.SHELF_WALL)
            GlStateManager.translate(0.0f, -1.0f, 0.0f);
        else if (shelfType == TileEntityItemShelfModel0.SHELF_WARDROBE)
            GlStateManager.translate(0.0f, -0.5f, 0.0f);

        GlStateManager.disableCull();
        bindTexture(texture);
        model.render(new EntityShelfInfo(tileEntity.getWorld(), triggerValues), 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();

        List<IvRaytraceableObject> slots = tileEntity.getItemSlotBoxes(partialTicks);
        for (IvRaytraceableObject object : slots)
        {
            int slotNumber = tileEntity.getSlotNumber(object);
            if (slotNumber < 0)
                continue;

            ItemStack item = tileEntity.storedItems[slotNumber];
            if (item == null || item.isEmpty())
                continue;

            if (shelfType == TileEntityItemShelfModel0.SHELF_WARDROBE && slotNumber > 7 && triggerValues[0] <= 0.0f)
                continue;
            if (shelfType == TileEntityItemShelfModel0.SHELF_WARDROBE && slotNumber < 4 && triggerValues[1] <= 0.0f)
                continue;
            if (shelfType == TileEntityItemShelfModel0.SHELF_WARDROBE && slotNumber > 3 && slotNumber < 8 && triggerValues[2] <= 0.0f)
                continue;

            drawItemInBox((IvRaytraceableAxisAlignedBox) object, item, tileEntity, x, y, z, shelfType == TileEntityItemShelfModel0.SHELF_WARDROBE && slotNumber > 7 ? 3.7f : 1.0f, shelfType == TileEntityItemShelfModel0.SHELF_WARDROBE && slotNumber > 7 ? 90.0f : 0.0f);
        }

        GlStateManager.popMatrix();
    }

    private static void drawItemInBox(IvRaytraceableAxisAlignedBox box, ItemStack item, TileEntityItemShelf tileEntity, double x, double y, double z, float itemScale, float rotY)
    {
        double smallestLength = Math.min(box.getWidth(), Math.min(box.getHeight(), box.getDepth()));

        GlStateManager.pushMatrix();
        GlStateManager.translate(x - tileEntity.xCoord, y - tileEntity.yCoord, z - tileEntity.zCoord);
        GlStateManager.translate(box.getX() + box.getWidth() / 2.0, box.getY() + box.getHeight() / 2.0, box.getZ() + box.getDepth() / 2.0);
        GlStateManager.rotate(-90.0f * tileEntity.direction + 180.0f + rotY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(smallestLength * 1.9 * itemScale, smallestLength * 1.9 * itemScale, smallestLength * 1.9 * itemScale);
        GlStateManager.translate(0.0f, -0.17f, 0.0f);
        Minecraft.getMinecraft().getRenderItem().renderItem(item, ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.popMatrix();
    }
}
