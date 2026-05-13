package ivorius.yegamolchattels.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHelperClient
{
    public static void sendTileEntityEventPacket(TileEntity tileEntity, String context, SimpleNetworkWrapper network, Object... params)
    {
        if (!(tileEntity instanceof ClientEventHandler))
            return;

        ByteBuf buffer = Unpooled.buffer();
        ((ClientEventHandler) tileEntity).assembleClientEvent(buffer, context, params);
        byte[] payload = new byte[buffer.readableBytes()];
        buffer.getBytes(0, payload);

        network.sendToServer(new PacketTileEntityClientEvent(tileEntity.getPos(), context, payload));
    }
}
