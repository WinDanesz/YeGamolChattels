package ivorius.yegamolchattels.multiblock;

import ivorius.yegamolchattels.raytracing.IvRaytraceableAxisAlignedBox;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IvMultiBlockHelper implements Iterable<int[]>
{
    private Iterator<int[]> iterator;
    private List<int[]> placements;
    private IvTileEntityMultiBlock parentTileEntity;
    private World world;
    private Block block;
    private int metadata;
    private int direction;
    private double[] center;
    private double[] size;

    public boolean beginPlacing(List<int[]> positions, World world, int x, int y, int z, int side, ItemStack itemStack, EntityPlayer player, Block block, int metadata, int direction)
    {
        List<int[]> validLocations = getBestPlacement(positions, world, x, y, z, side, itemStack, player, block);
        return validLocations != null && beginPlacing(validLocations, world, block, metadata, direction);
    }

    public boolean beginPlacing(List<int[]> validLocations, World world, Block block, int metadata, int direction)
    {
        this.world = world;
        this.block = block;
        this.metadata = metadata;
        this.direction = direction & 3;
        this.center = getTileEntityCenter(validLocations);
        this.size = getTileEntitySize(validLocations);
        this.placements = new ArrayList<>(validLocations);
        this.iterator = this.placements.iterator();
        this.parentTileEntity = null;
        return true;
    }

    @Override
    public Iterator<int[]> iterator()
    {
        return iterator;
    }

    public IvTileEntityMultiBlock placeBlock(int[] position)
    {
        boolean parent = this.parentTileEntity == null;
        BlockPos pos = new BlockPos(position[0], position[1], position[2]);
        world.setBlockState(pos, block.getStateFromMeta(metadata), 3);

        net.minecraft.tileentity.TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IvTileEntityMultiBlock)
        {
            IvTileEntityMultiBlock multiblock = (IvTileEntityMultiBlock) tileEntity;
            if (parent)
            {
                parentTileEntity = multiblock;
                parentTileEntity.becomeParent(placements);
            }
            else
                multiblock.becomeChild(parentTileEntity);

            multiblock.direction = direction;
            multiblock.centerCoords = new double[]{center[0] - position[0], center[1] - position[1], center[2] - position[2]};
            multiblock.centerCoordsSize = new double[]{size[0], size[1], size[2]};
            multiblock.markDirty();
            return multiblock;
        }

        return null;
    }

    public static List<int[]> getBestPlacement(List<int[]> positions, World world, int x, int y, int z, int side, ItemStack itemStack, EntityPlayer player, Block block)
    {
        BlockPos targetPos = new BlockPos(x, y, z);
        Block target = world.getBlockState(targetPos).getBlock();
        int metadata = target.getMetaFromState(world.getBlockState(targetPos));

        if (target == Blocks.SNOW_LAYER && metadata < 1)
            side = 1;
        else if (!target.isReplaceable(world, targetPos))
        {
            if (side == 0) --y;
            if (side == 1) ++y;
            if (side == 2) --z;
            if (side == 3) ++z;
            if (side == 4) --x;
            if (side == 5) ++x;
        }

        if (!player.canPlayerEdit(new BlockPos(x, y, z), EnumFacing.byIndex(side), itemStack))
            return null;

        int[] lengths = getLengths(positions);
        int[] min = getLowerCorner(positions);

        List<int[]> preferred = null;
        float[] center = new float[]{x - lengths[0] * 0.5f, y - lengths[1] * 0.5f, z - lengths[2] * 0.5f};

        for (int xShift = min[0] - lengths[0]; xShift <= min[0]; xShift++)
        {
            for (int yShift = min[1] - lengths[1]; yShift <= min[1]; yShift++)
            {
                for (int zShift = min[2] - lengths[2]; zShift <= min[2]; zShift++)
                {
                    ArrayList<int[]> placement = new ArrayList<>(positions.size());
                    for (int[] position : positions)
                        placement.add(new int[]{position[0] + xShift + x, position[1] + yShift + y, position[2] + zShift + z});

                    if (canPlace(world, block, placement))
                    {
                        if (preferred == null || distanceSquared(placement.get(0), center) < distanceSquared(preferred.get(0), center))
                            preferred = placement;
                    }
                }
            }
        }

        return preferred;
    }

    public static boolean canPlace(World world, Block block, List<int[]> positions)
    {
        for (int[] position : positions)
        {
            int x = position[0];
            int y = position[1];
            int z = position[2];
            BlockPos pos = new BlockPos(x, y, z);
            Block existing = world.getBlockState(pos).getBlock();
            if (!world.isAirBlock(pos) && !existing.isReplaceable(world, pos))
                return false;
            if (!block.canPlaceBlockAt(world, pos))
                return false;
        }
        return true;
    }

    public static double[] getTileEntityCenter(List<int[]> positions)
    {
        double[] center = getCenter(positions);
        return new double[]{center[0] + 0.5, center[1] + 0.5, center[2] + 0.5};
    }

    public static double[] getTileEntitySize(List<int[]> positions)
    {
        int[] min = getLowerCorner(positions);
        int[] max = getHigherCorner(positions);
        return new double[]{
                (max[0] - min[0] + 1) * 0.5,
                (max[1] - min[1] + 1) * 0.5,
                (max[2] - min[2] + 1) * 0.5
        };
    }

    public static double[] getCenter(List<int[]> positions)
    {
        int[] min = getLowerCorner(positions);
        int[] max = getHigherCorner(positions);
        return new double[]{
                (min[0] + max[0]) * 0.5,
                (min[1] + max[1]) * 0.5,
                (min[2] + max[2]) * 0.5
        };
    }

    public static int[] getLengths(List<int[]> positions)
    {
        int[] min = getLowerCorner(positions);
        int[] max = getHigherCorner(positions);
        return new int[]{max[0] - min[0], max[1] - min[1], max[2] - min[2]};
    }

    public static int[] getLowerCorner(List<int[]> positions)
    {
        int[] min = positions.get(0).clone();
        for (int i = 1; i < positions.size(); i++)
        {
            int[] pos = positions.get(i);
            min[0] = Math.min(min[0], pos[0]);
            min[1] = Math.min(min[1], pos[1]);
            min[2] = Math.min(min[2], pos[2]);
        }
        return min;
    }

    public static int[] getHigherCorner(List<int[]> positions)
    {
        int[] max = positions.get(0).clone();
        for (int i = 1; i < positions.size(); i++)
        {
            int[] pos = positions.get(i);
            max[0] = Math.max(max[0], pos[0]);
            max[1] = Math.max(max[1], pos[1]);
            max[2] = Math.max(max[2], pos[2]);
        }
        return max;
    }

    private static float distanceSquared(int[] referenceBlock, float[] center)
    {
        float distX = referenceBlock[0] - center[0];
        float distY = referenceBlock[1] - center[1];
        float distZ = referenceBlock[2] - center[2];
        return distX * distX + distY * distY + distZ * distZ;
    }

    public static List<int[]> getRotatedPositions(List<int[]> positions, int direction)
    {
        ArrayList<int[]> result = new ArrayList<>(positions.size());
        for (int[] position : positions)
        {
            if ((direction & 3) == 1 || (direction & 3) == 3)
                result.add(new int[]{position[2], position[1], position[0]});
            else
                result.add(position.clone());
        }
        return result;
    }

    public static List<int[]> getRotatedPositions(int direction, int width, int height, int length)
    {
        boolean affectsX = (direction & 3) == 0 || (direction & 3) == 2;
        return getPositions(affectsX ? width : length, height, affectsX ? length : width);
    }

    public static List<int[]> getPositions(int width, int height, int length)
    {
        ArrayList<int[]> positions = new ArrayList<>();
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int z = 0; z < length; z++)
                    positions.add(new int[]{x, y, z});
        return positions;
    }

    public static IvRaytraceableAxisAlignedBox getRotatedBox(Object userInfo, double x, double y, double z, double width, double height, double depth, int direction, double[] centerCoords)
    {
        switch (direction & 3)
        {
            case 0:
                return new IvRaytraceableAxisAlignedBox(userInfo, centerCoords[0] + x, centerCoords[1] + y, centerCoords[2] + z, width, height, depth);
            case 1:
                return new IvRaytraceableAxisAlignedBox(userInfo, centerCoords[0] - z - depth, centerCoords[1] + y, centerCoords[2] + x, depth, height, width);
            case 2:
                return new IvRaytraceableAxisAlignedBox(userInfo, centerCoords[0] - x - width, centerCoords[1] + y, centerCoords[2] - z - depth, width, height, depth);
            case 3:
                return new IvRaytraceableAxisAlignedBox(userInfo, centerCoords[0] + z, centerCoords[1] + y, centerCoords[2] - x - width, depth, height, width);
            default:
                return null;
        }
    }

    public static AxisAlignedBB getRotatedBB(double x, double y, double z, double width, double height, double depth, int direction, double[] centerCoords)
    {
        switch (direction & 3)
        {
            case 0:
                return getBBWithLengths(centerCoords[0] + x, centerCoords[1] + y, centerCoords[2] + z, width, height, depth);
            case 1:
                return getBBWithLengths(centerCoords[0] - z - depth, centerCoords[1] + y, centerCoords[2] + x, depth, height, width);
            case 2:
                return getBBWithLengths(centerCoords[0] - x - width, centerCoords[1] + y, centerCoords[2] - z - depth, width, height, depth);
            case 3:
                return getBBWithLengths(centerCoords[0] + z, centerCoords[1] + y, centerCoords[2] - x - width, depth, height, width);
            default:
                return null;
        }
    }

    public static Vector3f getRotatedVector(Vector3f vector, int direction)
    {
        switch (direction & 3)
        {
            case 0:
                return new Vector3f(vector.x, vector.y, vector.z);
            case 1:
                return new Vector3f(-vector.z, vector.y, vector.x);
            case 2:
                return new Vector3f(-vector.x, vector.y, -vector.z);
            case 3:
                return new Vector3f(vector.z, vector.y, -vector.x);
            default:
                return new Vector3f(vector);
        }
    }

    public static Vec3d getRotatedVector(Vec3d vector, int direction)
    {
        switch (direction & 3)
        {
            case 0:
                return new Vec3d(vector.x, vector.y, vector.z);
            case 1:
                return new Vec3d(-vector.z, vector.y, vector.x);
            case 2:
                return new Vec3d(-vector.x, vector.y, -vector.z);
            case 3:
                return new Vec3d(vector.z, vector.y, -vector.x);
            default:
                return vector;
        }
    }

    public static AxisAlignedBB getBBWithLengths(double x, double y, double z, double width, double height, double depth)
    {
        return new AxisAlignedBB(x, y, z, x + width, y + height, z + depth);
    }

    public static int getRotation(Entity entity)
    {
        return MathHelper.floor_double((entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    }
}
