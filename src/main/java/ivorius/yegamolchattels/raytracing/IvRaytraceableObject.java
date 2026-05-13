package ivorius.yegamolchattels.raytracing;

public abstract class IvRaytraceableObject
{
    public final Object userInfo;

    protected IvRaytraceableObject(Object userInfo)
    {
        this.userInfo = userInfo;
    }
}
