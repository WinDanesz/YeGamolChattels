package ivorius.ivtoolkit.blocks;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlockArea implements Iterable<BlockCoord>
{
    private final BlockCoord point1;
    private final BlockCoord point2;

    public BlockArea(BlockCoord point1, BlockCoord point2)
    {
        this.point1 = point1;
        this.point2 = point2;
    }

    public static BlockArea areaFromSize(BlockCoord coord, int[] size)
    {
        return new BlockArea(coord, new BlockCoord(coord.x + size[0] - 1, coord.y + size[1] - 1, coord.z + size[2] - 1));
    }

    public BlockCoord getLowerCorner()
    {
        return new BlockCoord(Math.min(point1.x, point2.x), Math.min(point1.y, point2.y), Math.min(point1.z, point2.z));
    }

    public BlockCoord getHigherCorner()
    {
        return new BlockCoord(Math.max(point1.x, point2.x), Math.max(point1.y, point2.y), Math.max(point1.z, point2.z));
    }

    @Override
    public Iterator<BlockCoord> iterator()
    {
        final BlockCoord lower = getLowerCorner();
        final BlockCoord higher = getHigherCorner();

        return new Iterator<BlockCoord>()
        {
            private int x = lower.x;
            private int y = lower.y;
            private int z = lower.z;

            @Override
            public boolean hasNext()
            {
                return z <= higher.z;
            }

            @Override
            public BlockCoord next()
            {
                if (!hasNext())
                    throw new NoSuchElementException();

                BlockCoord coord = new BlockCoord(x, y, z);
                x++;
                if (x > higher.x)
                {
                    x = lower.x;
                    y++;
                    if (y > higher.y)
                    {
                        y = lower.y;
                        z++;
                    }
                }
                return coord;
            }
        };
    }
}
