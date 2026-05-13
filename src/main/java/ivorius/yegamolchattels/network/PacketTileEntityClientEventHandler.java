package ivorius.yegamolchattels.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileEntityClientEventHandler implements IMessageHandler<PacketTileEntityClientEvent, IMessage>
{
    @Override
    public IMessage onMessage(PacketTileEntityClientEvent message, MessageContext ctx)
    {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
            TileEntity tileEntity = ctx.getServerHandler().player.getServerWorld().getTileEntity(message.getPos());
            if (tileEntity instanceof ClientEventHandler)
                ((ClientEventHandler) tileEntity).onClientEvent(message.payloadBuffer(), message.getContext(), ctx.getServerHandler().player);
        });
        return null;
    }
}
