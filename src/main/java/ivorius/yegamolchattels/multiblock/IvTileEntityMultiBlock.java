package ivorius.yegamolchattels.multiblock;

import ivorius.yegamolchattels.raytracing.IvRaytraceableAxisAlignedBox;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
        if (childPositions != null)
        {
            this.childCoords = new int[childPositions.size()][3];
            for (int i = 0; i < childPositions.size(); i++)
            {
                int[] location = childPositions.get(i);
                this.childCoords[i][0] = location[0] - pos.getX();
                this.childCoords[i][1] = location[1] - pos.getY();
                this.childCoords[i][2] = location[2] - pos.getZ();
            }
        }
        else
        {
            this.childCoords = new int[0][];
        }
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
        if (world == null)
            return;

        if (parentCoords != null)
        {
            if (getParent() == null)
            {
                IvTileEntityMultiBlock recoveredParent = findRecoveredParent();
                if (recoveredParent != null)
                {
                    becomeChild(recoveredParent);
                    markDirty();
                }
            }
        }
        else if ((childCoords == null || childCoords.length == 0) && hasSiblingPieces())
        {
            IvTileEntityMultiBlock recoveredParent = findRecoveredParent();
            if (recoveredParent != null)
            {
                becomeChild(recoveredParent);
                markDirty();
            }
        }
    }

    private IvTileEntityMultiBlock findRecoveredParent()
    {
        if (world == null || pos == null)
            return null;

        double[] center = getActiveCenterCoords();
        int minX = (int) Math.floor(center[0] - centerCoordsSize[0]);
        int minY = (int) Math.floor(center[1] - centerCoordsSize[1]);
        int minZ = (int) Math.floor(center[2] - centerCoordsSize[2]);
        int maxX = (int) Math.ceil(center[0] + centerCoordsSize[0]);
        int maxY = (int) Math.ceil(center[1] + centerCoordsSize[1]);
        int maxZ = (int) Math.ceil(center[2] + centerCoordsSize[2]);

        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                for (int z = minZ; z <= maxZ; z++)
                {
                    if (x == pos.getX() && y == pos.getY() && z == pos.getZ())
                        continue;

                    net.minecraft.tileentity.TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
                    if (!getClass().isInstance(tileEntity))
                        continue;

                    IvTileEntityMultiBlock candidate = (IvTileEntityMultiBlock) tileEntity;
                    if (candidate.referencesChild(pos))
                        return candidate;
                }
            }
        }

        return null;
    }

    private boolean hasSiblingPieces()
    {
        if (world == null || pos == null)
            return false;

        double[] center = getActiveCenterCoords();
        int minX = (int) Math.floor(center[0] - centerCoordsSize[0]);
        int minY = (int) Math.floor(center[1] - centerCoordsSize[1]);
        int minZ = (int) Math.floor(center[2] - centerCoordsSize[2]);
        int maxX = (int) Math.ceil(center[0] + centerCoordsSize[0]);
        int maxY = (int) Math.ceil(center[1] + centerCoordsSize[1]);
        int maxZ = (int) Math.ceil(center[2] + centerCoordsSize[2]);

        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                for (int z = minZ; z <= maxZ; z++)
                {
                    if (x == pos.getX() && y == pos.getY() && z == pos.getZ())
                        continue;

                    net.minecraft.tileentity.TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
                    if (getClass().isInstance(tileEntity))
                        return true;
                }
            }
        }

        return false;
    }

    private boolean referencesChild(BlockPos childPos)
    {
        if (childCoords == null)
            return false;

        for (int[] childCoord : childCoords)
        {
            if (childCoord == null || childCoord.length != 3)
                continue;

            if (pos.getX() + childCoord[0] == childPos.getX()
                    && pos.getY() + childCoord[1] == childPos.getY()
                    && pos.getZ() + childCoord[2] == childPos.getZ())
                return true;
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        parentCoords = tagCompound.hasKey("parentCoords", Constants.NBT.TAG_INT_ARRAY) ? tagCompound.getIntArray("parentCoords") : null;
        if (parentCoords != null && parentCoords.length != 3)
            parentCoords = null;

        if (tagCompound.hasKey("childCoords", Constants.NBT.TAG_INT_ARRAY))
        {
            int[] childCoordsFlat = tagCompound.getIntArray("childCoords");
            childCoords = new int[childCoordsFlat.length / 3][3];
            for (int i = 0; i < childCoords.length; i++)
            {
                childCoords[i][0] = childCoordsFlat[i * 3];
                childCoords[i][1] = childCoordsFlat[i * 3 + 1];
                childCoords[i][2] = childCoordsFlat[i * 3 + 2];
            }
        }
        else if (tagCompound.hasKey("childCoords", Constants.NBT.TAG_LIST))
        {
            NBTTagList childList = tagCompound.getTagList("childCoords", Constants.NBT.TAG_COMPOUND);
            childCoords = new int[childList.tagCount()][];
            for (int i = 0; i < childList.tagCount(); i++)
            {
                int[] coords = childList.getCompoundTagAt(i).getIntArray("coords");
                childCoords[i] = coords.length == 3 ? coords : new int[0];
            }
        }
        else if (parentCoords == null)
        {
            childCoords = new int[0][];
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
            int[] childCoordsFlat = new int[childCoords.length * 3];
            for (int i = 0; i < childCoords.length; i++)
            {
                childCoordsFlat[i * 3] = childCoords[i][0];
                childCoordsFlat[i * 3 + 1] = childCoords[i][1];
                childCoordsFlat[i * 3 + 2] = childCoords[i][2];
            }
            tagCompound.setIntArray("childCoords", childCoordsFlat);
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
