package ivorius.yegamolchattels.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class IvTileEntityHelper
{
    public static Packet getStandardDescriptionPacket(TileEntity tileEntity)
    {
        NBTTagCompound compound = new NBTTagCompound();
        tileEntity.writeToNBT(compound);
        return new SPacketUpdateTileEntity(tileEntity.getPos(), 1, compound);
    }
}
