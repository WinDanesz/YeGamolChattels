package net.minecraft.util;

import net.minecraft.util.math.Vec3d;

public class Vec3 extends Vec3d
{
    public final double xCoord;
    public final double yCoord;
    public final double zCoord;

    public Vec3(double xIn, double yIn, double zIn)
    {
        super(xIn, yIn, zIn);
        this.xCoord = xIn;
        this.yCoord = yIn;
        this.zCoord = zIn;
    }

    public static Vec3 createVectorHelper(double x, double y, double z)
    {
        return new Vec3(x, y, z);
    }
}
