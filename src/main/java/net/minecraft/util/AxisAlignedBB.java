package net.minecraft.util;

public class AxisAlignedBB extends net.minecraft.util.math.AxisAlignedBB
{
    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        super(x1, y1, z1, x2, y2, z2);
    }

    public static AxisAlignedBB getBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    public boolean intersectsWith(AxisAlignedBB other)
    {
        return this.intersects(other);
    }
}
