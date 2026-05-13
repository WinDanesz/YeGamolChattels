package ivorius.yegamolchattels.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketTileEntityClientEvent implements IMessage
{
    private BlockPos pos = BlockPos.ORIGIN;
    private String context;
    private byte[] payload = new byte[0];

    public PacketTileEntityClientEvent()
    {
    }

    public PacketTileEntityClientEvent(BlockPos pos, String context, byte[] payload)
    {
        this.pos = pos;
        this.context = context;
        this.payload = payload;
    }

    public BlockPos getPos()
    {
        return pos;
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
        pos = BlockPos.fromLong(buf.readLong());
        context = ByteBufUtils.readUTF8String(buf);
        payload = new byte[buf.readableBytes()];
        buf.readBytes(payload);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, context);
        buf.writeBytes(payload);
    }
}
