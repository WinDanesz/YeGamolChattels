package ivorius.yegamolchattels.raytracing;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class IvRaytracerMC
{
    public static IvRaytracedIntersection getFirstIntersection(List<IvRaytraceableObject> raytraceables, Entity entity)
    {
        if (entity == null || raytraceables == null)
            return null;

        Vec3d start = entity.getPositionEyes(1.0f);
        Vec3d look = entity.getLook(1.0f);
        Vec3d end = start.add(look.x * 5.0, look.y * 5.0, look.z * 5.0);

        IvRaytracedIntersection closest = null;
        for (IvRaytraceableObject raytraceable : raytraceables)
        {
            if (raytraceable instanceof IvRaytraceableAxisAlignedBox)
            {
                RayTraceResult result = ((IvRaytraceableAxisAlignedBox) raytraceable).getBoundingBox().calculateIntercept(start, end);
                if (result != null)
                {
                    double distanceSq = start.squareDistanceTo(result.hitVec);
                    if (closest == null || distanceSq < closest.getDistanceSq())
                        closest = new IvRaytracedIntersection(raytraceable, result, distanceSq);
                }
            }
        }

        return closest;
    }
}
