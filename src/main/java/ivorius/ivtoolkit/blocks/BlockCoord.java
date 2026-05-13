package ivorius.ivtoolkit.blocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCoord
{
    public final int x;
    public final int y;
    public final int z;

    public BlockCoord(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockCoord(TileEntity tileEntity)
    {
        this(tileEntity.getPos());
    }

    public BlockCoord(BlockPos pos)
    {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockCoord add(int x, int y, int z)
    {
        return new BlockCoord(this.x + x, this.y + y, this.z + z);
    }

    public BlockCoord add(BlockCoord other)
    {
        return add(other.x, other.y, other.z);
    }

    public BlockCoord subtract(int x, int y, int z)
    {
        return new BlockCoord(this.x - x, this.y - y, this.z - z);
    }

    public BlockCoord subtract(BlockCoord other)
    {
        return subtract(other.x, other.y, other.z);
    }

    public Block getBlock(World world)
    {
        return world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public int getMetadata(World world)
    {
        return getBlock(world).getMetaFromState(world.getBlockState(new BlockPos(x, y, z)));
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (!(obj instanceof BlockCoord)) return false;
        BlockCoord other = (BlockCoord) obj;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode()
    {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
