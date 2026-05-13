package ivorius.yegamolchattels.client;

import ivorius.yegamolchattels.YGCProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ClientProxy implements YGCProxy
{
    @Override
    public void loadConfig(String categoryID)
    {
    }

    @Override
    public void registerRenderers()
    {
    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().player;
    }
}
