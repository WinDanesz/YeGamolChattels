package ivorius.yegamolchattels.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkHelperServer
{
    public static void sendTileEntityUpdatePacket(TileEntity tileEntity, String context, SimpleNetworkWrapper network, Object... params)
    {
        if (!(tileEntity instanceof PartialUpdateHandler) || tileEntity.getWorld() == null)
            return;

        ByteBuf buffer = Unpooled.buffer();
        ((PartialUpdateHandler) tileEntity).writeUpdateData(buffer, context, params);
        byte[] payload = new byte[buffer.readableBytes()];
        buffer.getBytes(0, payload);

        PacketTileEntityUpdate packet = new PacketTileEntityUpdate(tileEntity.getPos(), context, payload);
        network.sendToAllAround(packet, new NetworkRegistry.TargetPoint(tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX() + 0.5, tileEntity.getPos().getY() + 0.5, tileEntity.getPos().getZ() + 0.5, 64));
    }

    public static void sendTileEntityUpdatePacket(TileEntity tileEntity, String context, SimpleNetworkWrapper network, EntityPlayer player, Object... params)
    {
        if (!(player instanceof EntityPlayerMP) || !(tileEntity instanceof PartialUpdateHandler))
            return;

        ByteBuf buffer = Unpooled.buffer();
        ((PartialUpdateHandler) tileEntity).writeUpdateData(buffer, context, params);
        byte[] payload = new byte[buffer.readableBytes()];
        buffer.getBytes(0, payload);

        network.sendTo(new PacketTileEntityUpdate(tileEntity.getPos(), context, payload), (EntityPlayerMP) player);
    }
}
