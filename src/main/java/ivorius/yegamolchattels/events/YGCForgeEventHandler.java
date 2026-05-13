package ivorius.yegamolchattels.events;

import net.minecraftforge.common.MinecraftForge;

public class YGCForgeEventHandler
{
    public void register()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
