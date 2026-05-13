package ivorius.yegamolchattels.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public interface ClientEventHandler
{
    void assembleClientEvent(ByteBuf buffer, String context, Object... params);

    void onClientEvent(ByteBuf buffer, String context, EntityPlayerMP player);
}
