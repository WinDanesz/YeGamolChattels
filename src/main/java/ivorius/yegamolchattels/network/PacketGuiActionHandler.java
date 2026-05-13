package ivorius.yegamolchattels.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketGuiActionHandler implements IMessageHandler<PacketGuiAction, IMessage>
{
    @Override
    public IMessage onMessage(PacketGuiAction message, MessageContext ctx)
    {
        ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
            PacketGuiAction.ActionHandler handler = PacketGuiAction.ActionHandler.fromContainer(ctx.getServerHandler().player.openContainer);
            if (handler != null)
                handler.handleAction(message.getContext(), message.payloadBuffer());
        });
        return null;
    }
}
