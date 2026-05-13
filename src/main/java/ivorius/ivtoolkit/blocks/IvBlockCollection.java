package ivorius.ivtoolkit.blocks;

import ivorius.ivtoolkit.tools.MCRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class IvBlockCollection implements Iterable<BlockCoord>
{
    public final int width;
    public final int height;
    public final int length;

    private final Block[] blocks;
    private final byte[] metadata;

    public IvBlockCollection(int width, int height, int length)
    {
        this.width = width;
        this.height = height;
        this.length = length;
        this.blocks = new Block[width * height * length];
        this.metadata = new byte[width * height * length];

        for (int i = 0; i < blocks.length; i++)
            blocks[i] = Blocks.AIR;
    }

    public IvBlockCollection(NBTTagCompound compound, MCRegistry registry)
    {
        this(compound.getInteger("width"), compound.getInteger("height"), compound.getInteger("length"));

        if (compound.hasKey("entries", Constants.NBT.TAG_LIST))
        {
            NBTTagList entries = compound.getTagList("entries", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < Math.min(entries.tagCount(), blocks.length); i++)
            {
                NBTTagCompound entry = entries.getCompoundTagAt(i);
                blocks[i] = registry.blockFromID(new net.minecraft.util.ResourceLocation(entry.getString("block")));
                metadata[i] = entry.getByte("meta");
                if (blocks[i] == null)
                    blocks[i] = Blocks.AIR;
            }
        }
    }

    private int indexFromCoord(BlockCoord coord)
    {
        return ((coord.z * height) + coord.y) * width + coord.x;
    }

    public boolean hasCoord(BlockCoord coord)
    {
        return coord.x >= 0 && coord.x < width && coord.y >= 0 && coord.y < height && coord.z >= 0 && coord.z < length;
    }

    public Block getBlock(BlockCoord coord)
    {
        return hasCoord(coord) ? blocks[indexFromCoord(coord)] : Blocks.AIR;
    }

    public byte getMetadata(BlockCoord coord)
    {
        return hasCoord(coord) ? metadata[indexFromCoord(coord)] : 0;
    }

    public void setBlockAndMetadata(BlockCoord coord, Block block, byte meta)
    {
        if (!hasCoord(coord))
            return;

        int index = indexFromCoord(coord);
        blocks[index] = block != null ? block : Blocks.AIR;
        metadata[index] = meta;
    }

    public boolean shouldRenderSide(BlockCoord coord, ForgeDirection direction)
    {
        BlockCoord sideCoord = coord.add(direction.offsetX, direction.offsetY, direction.offsetZ);
        Block sideBlock = getBlock(sideCoord);
        return !hasCoord(sideCoord) || !sideBlock.isOpaqueCube(sideBlock.getStateFromMeta(getMetadata(sideCoord)));
    }

    public int getBlockMultiplicity()
    {
        Set<String> unique = new HashSet<>();
        for (int i = 0; i < blocks.length; i++)
        {
            Block block = blocks[i];
            unique.add(Block.REGISTRY.getNameForObject(block) + ":" + metadata[i]);
        }
        return unique.size();
    }

    public NBTTagCompound createTagCompound()
    {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("width", width);
        compound.setInteger("height", height);
        compound.setInteger("length", length);

        NBTTagList entries = new NBTTagList();
        for (int i = 0; i < blocks.length; i++)
        {
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString("block", String.valueOf(Block.REGISTRY.getNameForObject(blocks[i])));
            entry.setByte("meta", metadata[i]);
            entries.appendTag(entry);
        }
        compound.setTag("entries", entries);
        return compound;
    }

    public MovingObjectPosition rayTrace(Vec3 position, Vec3 direction)
    {
        double lengthSq = direction.xCoord * direction.xCoord + direction.yCoord * direction.yCoord + direction.zCoord * direction.zCoord;
        if (lengthSq <= 0.0)
            return null;

        double length = Math.sqrt(lengthSq);
        double dirX = direction.xCoord / length;
        double dirY = direction.yCoord / length;
        double dirZ = direction.zCoord / length;

        BlockCoord lastCoord = null;
        for (int step = 0; step <= 1024; step++)
        {
            double scale = step / 128.0;
            Vec3 sample = Vec3.createVectorHelper(position.xCoord + dirX * scale, position.yCoord + dirY * scale, position.zCoord + dirZ * scale);
            BlockCoord coord = new BlockCoord((int) Math.floor(sample.xCoord), (int) Math.floor(sample.yCoord), (int) Math.floor(sample.zCoord));

            if (!hasCoord(coord))
                continue;

            Block block = getBlock(coord);
            if (block.getMaterial(block.getStateFromMeta(getMetadata(coord))) != Material.AIR)
            {
                ForgeDirection hitSide = ForgeDirection.UNKNOWN;
                if (lastCoord != null)
                {
                    int dx = coord.x - lastCoord.x;
                    int dy = coord.y - lastCoord.y;
                    int dz = coord.z - lastCoord.z;
                    if (dx > 0) hitSide = ForgeDirection.WEST;
                    else if (dx < 0) hitSide = ForgeDirection.EAST;
                    else if (dy > 0) hitSide = ForgeDirection.DOWN;
                    else if (dy < 0) hitSide = ForgeDirection.UP;
                    else if (dz > 0) hitSide = ForgeDirection.NORTH;
                    else if (dz < 0) hitSide = ForgeDirection.SOUTH;
                }

                return new MovingObjectPosition(coord.x, coord.y, coord.z, hitSide.ordinal(), sample);
            }

            lastCoord = coord;
        }

        return null;
    }

    @Override
    public Iterator<BlockCoord> iterator()
    {
        return new Iterator<BlockCoord>()
        {
            private int index;

            @Override
            public boolean hasNext()
            {
                return index < blocks.length;
            }

            @Override
            public BlockCoord next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();

                int local = index++;
                int x = local % width;
                int yz = local / width;
                int y = yz % height;
                int z = yz / height;
                return new BlockCoord(x, y, z);
            }
        };
    }
}
