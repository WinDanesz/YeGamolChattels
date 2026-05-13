package ivorius.yegamolchattels.multiblock;

import ivorius.yegamolchattels.math.IvMathHelper;
import ivorius.yegamolchattels.raytracing.IvRaytraceableAxisAlignedBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

public class IvTileEntityRotatable extends TileEntity implements ITickable
{
    public World worldObj;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    public int direction;

    protected void updateCompatFields()
    {
        worldObj = world;
        if (pos != null)
        {
            xCoord = pos.getX();
            yCoord = pos.getY();
            zCoord = pos.getZ();
        }
    }

    @Override
    public void setWorld(World worldIn)
    {
        super.setWorld(worldIn);
        updateCompatFields();
    }

    @Override
    public void setPos(BlockPos posIn)
    {
        super.setPos(posIn);
        updateCompatFields();
    }

    @Override
    public void onLoad()
    {
        super.onLoad();
        updateCompatFields();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        updateCompatFields();
        direction = tagCompound.getInteger("direction") & 3;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        updateCompatFields();
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("direction", direction & 3);
        return tagCompound;
    }

    @Override
    public void update()
    {
        updateCompatFields();
        updateEntity();
    }

    public void updateEntity()
    {
    }

    protected void notifyBlockUpdate()
    {
        if (worldObj != null && pos != null)
        {
            IBlockState state = worldObj.getBlockState(pos);
            worldObj.notifyBlockUpdate(pos, state, state, 3);
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return (SPacketUpdateTileEntity) IvTileEntityHelper.getStandardDescriptionPacket(this);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }

    public IvRaytraceableAxisAlignedBox getInterpolatedRotatedBox(Object userInfo, double x, double y, double z, double width, double height, double depth, double x1, double y1, double z1, double width1, double height1, double depth1, float fraction)
    {
        return getRotatedBox(
                userInfo,
                IvMathHelper.mix(x, x1, fraction),
                IvMathHelper.mix(y, y1, fraction),
                IvMathHelper.mix(z, z1, fraction),
                IvMathHelper.mix(width, width1, fraction),
                IvMathHelper.mix(height, height1, fraction),
                IvMathHelper.mix(depth, depth1, fraction)
        );
    }

    public IvRaytraceableAxisAlignedBox getRotatedBox(Object userInfo, double x, double y, double z, double width, double height, double depth)
    {
        return IvMultiBlockHelper.getRotatedBox(userInfo, x, y, z, width, height, depth, direction, new double[]{pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5});
    }

    public AxisAlignedBB getRotatedBB(double x, double y, double z, double width, double height, double depth)
    {
        return IvMultiBlockHelper.getRotatedBB(x, y, z, width, height, depth, direction, new double[]{pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5});
    }

    public Vector3f getRotatedVector(Vector3f vector)
    {
        return IvMultiBlockHelper.getRotatedVector(vector, direction);
    }

    public Vec3d getRotatedVector(Vec3d vector)
    {
        return IvMultiBlockHelper.getRotatedVector(vector, direction);
    }

    public EnumFacing getFacing()
    {
        switch (direction & 3)
        {
            case 0:
                return EnumFacing.SOUTH;
            case 1:
                return EnumFacing.WEST;
            case 2:
                return EnumFacing.NORTH;
            case 3:
                return EnumFacing.EAST;
            default:
                return EnumFacing.SOUTH;
        }
    }
}
