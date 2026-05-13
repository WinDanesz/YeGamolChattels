package net.minecraftforge.common.util;

public enum ForgeDirection
{
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0),
    UNKNOWN(0, 0, 0);

    public final int offsetX;
    public final int offsetY;
    public final int offsetZ;

    ForgeDirection(int offsetX, int offsetY, int offsetZ)
    {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public static ForgeDirection getOrientation(int index)
    {
        ForgeDirection[] values = values();
        return index >= 0 && index < values.length ? values[index] : UNKNOWN;
    }

    public ForgeDirection getOpposite()
    {
        switch (this)
        {
            case DOWN:
                return UP;
            case UP:
                return DOWN;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            case EAST:
                return WEST;
            default:
                return UNKNOWN;
        }
    }
}
