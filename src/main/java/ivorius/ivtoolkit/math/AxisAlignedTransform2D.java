package ivorius.ivtoolkit.math;

import ivorius.ivtoolkit.blocks.BlockCoord;

public class AxisAlignedTransform2D
{
    private final int rotation;
    private final boolean mirrorX;

    public AxisAlignedTransform2D(int rotationClockwise, boolean mirrorX)
    {
        this.rotation = ((rotationClockwise % 4) + 4) % 4;
        this.mirrorX = mirrorX;
    }

    public static AxisAlignedTransform2D transform(int rotationClockwise, boolean flipX)
    {
        return new AxisAlignedTransform2D(rotationClockwise, flipX);
    }

    public int getRotation()
    {
        return rotation;
    }

    public BlockCoord apply(BlockCoord position, int[] size)
    {
        int positionX = mirrorX ? size[0] - 1 - position.x : position.x;

        switch (rotation)
        {
            case 0:
                return new BlockCoord(positionX, position.y, position.z);
            case 1:
                return new BlockCoord(size[2] - 1 - position.z, position.y, positionX);
            case 2:
                return new BlockCoord(size[0] - 1 - positionX, position.y, size[2] - 1 - position.z);
            case 3:
                return new BlockCoord(position.z, position.y, size[0] - 1 - positionX);
            default:
                throw new IllegalStateException();
        }
    }
}
