package ivorius.yegamolchattels.raytracing;

import net.minecraft.util.math.RayTraceResult;

public class IvRaytracedIntersection
{
    private final IvRaytraceableObject hitObject;
    private final RayTraceResult result;
    private final double distanceSq;

    public IvRaytracedIntersection(IvRaytraceableObject hitObject, RayTraceResult result, double distanceSq)
    {
        this.hitObject = hitObject;
        this.result = result;
        this.distanceSq = distanceSq;
    }

    public IvRaytraceableObject getHitObject()
    {
        return hitObject;
    }

    public Object getUserInfo()
    {
        return hitObject.userInfo;
    }

    public RayTraceResult getResult()
    {
        return result;
    }

    public double getDistanceSq()
    {
        return distanceSq;
    }
}
