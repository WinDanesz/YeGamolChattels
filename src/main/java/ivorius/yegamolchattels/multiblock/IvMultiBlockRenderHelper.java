package ivorius.yegamolchattels.multiblock;

import org.lwjgl.opengl.GL11;

public class IvMultiBlockRenderHelper
{
    public static void transformFor(IvTileEntityMultiBlock tileEntity, double renderX, double renderY, double renderZ)
    {
        double[] center = tileEntity.getActiveCenterCoords();
        GL11.glTranslated(renderX + center[0] - tileEntity.xCoord, renderY + center[1] - tileEntity.yCoord, renderZ + center[2] - tileEntity.zCoord);
        GL11.glRotatef(IvRotatableBlockRenderHelper.getAngleFromSouth(tileEntity.direction), 0.0f, 1.0f, 0.0f);
    }
}
