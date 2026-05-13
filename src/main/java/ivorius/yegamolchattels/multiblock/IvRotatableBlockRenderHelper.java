package ivorius.yegamolchattels.multiblock;

import org.lwjgl.opengl.GL11;

public class IvRotatableBlockRenderHelper
{
    public static void transformFor(IvTileEntityRotatable tileEntity, double renderX, double renderY, double renderZ)
    {
        GL11.glTranslated(renderX + 0.5, renderY + 0.5, renderZ + 0.5);
        GL11.glRotatef(getAngleFromSouth(tileEntity.direction), 0.0f, 1.0f, 0.0f);
    }

    public static float getAngleFromSouth(int direction)
    {
        switch (direction & 3)
        {
            case 0:
                return 180.0f;
            case 1:
                return 90.0f;
            case 2:
                return 0.0f;
            case 3:
                return 270.0f;
            default:
                return 180.0f;
        }
    }
}
