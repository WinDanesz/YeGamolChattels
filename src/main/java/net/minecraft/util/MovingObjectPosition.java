package net.minecraft.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class MovingObjectPosition extends RayTraceResult
{
    public final int blockX;
    public final int blockY;
    public final int blockZ;
    public final int sideHit;
    public final Vec3 hitVec;

    public MovingObjectPosition(int x, int y, int z, int sideHit, Vec3 hitVec)
    {
        super(hitVec, net.minecraft.util.EnumFacing.byIndex(sideHit), new BlockPos(x, y, z));
        this.blockX = x;
        this.blockY = y;
        this.blockZ = z;
        this.sideHit = sideHit;
        this.hitVec = hitVec;
    }
}
