package ivorius.yegamolchattels.raytracing;

import net.minecraft.util.math.AxisAlignedBB;

public class IvRaytraceableAxisAlignedBox extends IvRaytraceableObject
{
    private final double x;
    private final double y;
    private final double z;
    private final double width;
    private final double height;
    private final double depth;

    public IvRaytraceableAxisAlignedBox(Object userInfo, double x, double y, double z, double width, double height, double depth)
    {
        super(userInfo);
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public double getDepth()
    {
        return depth;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return new AxisAlignedBB(x, y, z, x + width, y + height, z + depth);
    }
}
