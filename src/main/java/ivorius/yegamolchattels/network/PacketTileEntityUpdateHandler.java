package ivorius.yegamolchattels.network;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTileEntityUpdateHandler implements IMessageHandler<PacketTileEntityUpdate, IMessage>
{
    @Override
    public IMessage onMessage(PacketTileEntityUpdate message, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            if (Minecraft.getMinecraft().world == null)
                return;

            TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(message.getPos());
            if (tileEntity instanceof PartialUpdateHandler)
                ((PartialUpdateHandler) tileEntity).readUpdateData(message.payloadBuffer(), message.getContext());
        });
        return null;
    }
}
