package ivorius.yegamolchattels.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketGuiAction implements IMessage
{
    private String context;
    private byte[] payload = new byte[0];

    public PacketGuiAction()
    {
    }

    public PacketGuiAction(String context, byte[] payload)
    {
        this.context = context;
        this.payload = payload;
    }

    public static PacketGuiAction packetGuiAction(String context, Object... params)
    {
        ByteBuf buffer = Unpooled.buffer();
        for (Object param : params)
        {
            if (param instanceof Integer)
                buffer.writeInt((Integer) param);
            else if (param instanceof Float)
                buffer.writeFloat((Float) param);
            else if (param instanceof Boolean)
                buffer.writeBoolean((Boolean) param);
            else if (param instanceof String)
                ByteBufUtils.writeUTF8String(buffer, (String) param);
            else
                throw new IllegalArgumentException("Unsupported GUI action parameter: " + param);
        }

        byte[] payload = new byte[buffer.readableBytes()];
        buffer.getBytes(0, payload);
        return new PacketGuiAction(context, payload);
    }

    public String getContext()
    {
        return context;
    }

    public ByteBuf payloadBuffer()
    {
        return Unpooled.wrappedBuffer(payload);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        context = ByteBufUtils.readUTF8String(buf);
        payload = new byte[buf.readableBytes()];
        buf.readBytes(payload);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, context);
        buf.writeBytes(payload);
    }

    public interface ActionHandler
    {
        void handleAction(String context, ByteBuf buffer);

        static ActionHandler fromContainer(Container container)
        {
            return container instanceof ActionHandler ? (ActionHandler) container : null;
        }
    }
}
