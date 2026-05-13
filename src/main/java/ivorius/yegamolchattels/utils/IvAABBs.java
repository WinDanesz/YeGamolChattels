package ivorius.yegamolchattels.utils;

import net.minecraft.util.math.AxisAlignedBB;

public class IvAABBs
{
    public static AxisAlignedBB boundsIntersection(AxisAlignedBB bb, int x, int y, int z)
    {
        return new AxisAlignedBB(
                Math.max(bb.minX - x, 0.0),
                Math.max(bb.minY - y, 0.0),
                Math.max(bb.minZ - z, 0.0),
                Math.min(bb.maxX - x, 1.0),
                Math.min(bb.maxY - y, 1.0),
                Math.min(bb.maxZ - z, 1.0)
        );
    }
}
