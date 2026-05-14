package ivorius.yegamolchattels.multiblock;

import ivorius.yegamolchattels.raytracing.IvRaytraceableAxisAlignedBox;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;

public class IvTileEntityMultiBlock extends IvTileEntityRotatable implements ITickable
{
    public int[] parentCoords;
    public int[][] childCoords;
    public double[] centerCoords = new double[]{0.5, 0.5, 0.5};
    public double[] centerCoordsSize = new double[]{0.5, 0.5, 0.5};
    public boolean multiblockInvalid;

    public int[] getActiveParentCoords()
    {
        if (parentCoords == null)
            return new int[]{pos.getX(), pos.getY(), pos.getZ()};

        return new int[]{pos.getX() + parentCoords[0], pos.getY() + parentCoords[1], pos.getZ() + parentCoords[2]};
    }

    public int[][] getActiveChildCoords()
    {
        if (childCoords == null)
            return new int[0][];

        int[][] result = new int[childCoords.length][];
        for (int i = 0; i < childCoords.length; i++)
            result[i] = new int[]{pos.getX() + childCoords[i][0], pos.getY() + childCoords[i][1], pos.getZ() + childCoords[i][2]};
        return result;
    }

    public double[] getActiveCenterCoords()
    {
        return new double[]{pos.getX() + centerCoords[0], pos.getY() + centerCoords[1], pos.getZ() + centerCoords[2]};
    }

    public void becomeChild(IvTileEntityMultiBlock parent)
    {
        if (parent != null)
            this.parentCoords = new int[]{parent.getPos().getX() - pos.getX(), parent.getPos().getY() - pos.getY(), parent.getPos().getZ() - pos.getZ()};
        else
            this.parentCoords = null;
    }

    public void becomeParent(java.util.List<int[]> childPositions)
    {
        if (childPositions == null)
        {
            this.childCoords = new int[0][];
            return;
        }

        this.childCoords = childPositions.toArray(new int[childPositions.size()][]);
    }

    public boolean isParent()
    {
        return parentCoords == null;
    }

    public IvTileEntityMultiBlock getTotalParent()
    {
        return isParent() ? this : getParent();
    }

    public IvTileEntityMultiBlock getParent()
    {
        if (parentCoords != null && world != null)
        {
            int[] activeParentCoords = getActiveParentCoords();
            net.minecraft.tileentity.TileEntity parent = world.getTileEntity(new net.minecraft.util.math.BlockPos(activeParentCoords[0], activeParentCoords[1], activeParentCoords[2]));
            if (parent != null && getClass().equals(parent.getClass()))
                return (IvTileEntityMultiBlock) parent;
        }

        return null;
    }

    @Override
    public void update()
    {
        updateEntityParent();
    }

    public void updateEntityParent()
    {
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        parentCoords = tagCompound.hasKey("parentCoords", Constants.NBT.TAG_INT_ARRAY) ? tagCompound.getIntArray("parentCoords") : null;
        if (parentCoords != null && parentCoords.length != 3)
            parentCoords = null;

        if (tagCompound.hasKey("childCoords", Constants.NBT.TAG_LIST))
        {
            NBTTagList childList = tagCompound.getTagList("childCoords", Constants.NBT.TAG_COMPOUND);
            childCoords = new int[childList.tagCount()][];
            for (int i = 0; i < childList.tagCount(); i++)
            {
                int[] coords = childList.getCompoundTagAt(i).getIntArray("coords");
                childCoords[i] = coords.length == 3 ? coords : new int[0];
            }
        }
        else
        {
            childCoords = null;
        }

        centerCoords = new double[]{
                tagCompound.getDouble("centerX"),
                tagCompound.getDouble("centerY"),
                tagCompound.getDouble("centerZ")
        };
        centerCoordsSize = new double[]{
                tagCompound.getDouble("centerSizeX"),
                tagCompound.getDouble("centerSizeY"),
                tagCompound.getDouble("centerSizeZ")
        };
        multiblockInvalid = tagCompound.getBoolean("multiblockInvalid");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        if (parentCoords != null && parentCoords.length == 3)
            tagCompound.setIntArray("parentCoords", parentCoords);

        if (childCoords != null)
        {
            NBTTagList childList = new NBTTagList();
            for (int[] childCoord : childCoords)
            {
                if (childCoord != null && childCoord.length == 3)
                {
                    NBTTagCompound childTag = new NBTTagCompound();
                    childTag.setIntArray("coords", childCoord);
                    childList.appendTag(childTag);
                }
            }
            tagCompound.setTag("childCoords", childList);
        }

        tagCompound.setDouble("centerX", centerCoords[0]);
        tagCompound.setDouble("centerY", centerCoords[1]);
        tagCompound.setDouble("centerZ", centerCoords[2]);
        tagCompound.setDouble("centerSizeX", centerCoordsSize[0]);
        tagCompound.setDouble("centerSizeY", centerCoordsSize[1]);
        tagCompound.setDouble("centerSizeZ", centerCoordsSize[2]);
        tagCompound.setBoolean("multiblockInvalid", multiblockInvalid);
        return tagCompound;
    }

    public IvRaytraceableAxisAlignedBox getRotatedBox(Object userInfo, double x, double y, double z, double width, double height, double depth)
    {
        return IvMultiBlockHelper.getRotatedBox(userInfo, x, y, z, width, height, depth, direction, getActiveCenterCoords());
    }

    public AxisAlignedBB getRotatedBB(double x, double y, double z, double width, double height, double depth)
    {
        return IvMultiBlockHelper.getRotatedBB(x, y, z, width, height, depth, direction, getActiveCenterCoords());
    }

    public AxisAlignedBB getBoxAroundCenter(double width, double height, double length)
    {
        double[] center = getActiveCenterCoords();
        return new AxisAlignedBB(center[0] - width, center[1] - height, center[2] - length, center[0] + width, center[1] + height, center[2] + length);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return isParent() ? getBoxAroundCenter(centerCoordsSize[0], centerCoordsSize[1], centerCoordsSize[2]) : getBoxAroundCenter(0.0, 0.0, 0.0);
    }
}
