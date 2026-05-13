/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.blocks;

import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import ivorius.ivtoolkit.blocks.BlockArea;
import ivorius.ivtoolkit.blocks.BlockCoord;
import ivorius.ivtoolkit.blocks.IvBlockCollection;
import ivorius.yegamolchattels.network.NetworkHelperServer;
import ivorius.yegamolchattels.network.PartialUpdateHandler;
import ivorius.ivtoolkit.tools.MCRegistryDefault;
import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.client.rendering.GridQuadCache;
import ivorius.yegamolchattels.client.rendering.IIconQuadCache;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by lukas on 11.07.14.
 */
public class TileEntityMicroBlock extends TileEntity implements PartialUpdateHandler
{
    public static final int MICROBLOCKS_PER_BLOCK_X = 8;
    public static final int MICROBLOCKS_PER_BLOCK_Y = 8;
    public static final int MICROBLOCKS_PER_BLOCK_Z = 8;
    public static final int MAX_MICROBLOCK_MAPPINGS = 64;

    private IvBlockCollection blockCollection;

    private boolean[] isSideOpaque = new boolean[6];

    private boolean shouldDropAsItem = true;

    @SideOnly(Side.CLIENT)
    private GridQuadCache<Object> quadCache;

    public TileEntityMicroBlock()
    {
        this.blockCollection = new IvBlockCollection(MICROBLOCKS_PER_BLOCK_X, MICROBLOCKS_PER_BLOCK_Y, MICROBLOCKS_PER_BLOCK_Z);
    }

    public boolean canUpdate()
    {
        return false;
    }

    public IvBlockCollection getBlockCollection()
    {
        return blockCollection;
    }

    public void setBlockCollection(IvBlockCollection blockCollection)
    {
        this.blockCollection = blockCollection;
    }

    public boolean isSideOpaque(ForgeDirection direction)
    {
        return isSideOpaque[direction.ordinal()];
    }

    public boolean areAllSidesOpaque()
    {
        return isSideOpaque[0] && isSideOpaque[1] && isSideOpaque[2] && isSideOpaque[3] && isSideOpaque[4] && isSideOpaque[5];
    }

    public void markCacheInvalid()
    {
        markCacheInvalid(false);
    }

    private void markCacheInvalid(boolean fromNBT)
    {
        int[] sizeX = new int[]{1, blockCollection.height, blockCollection.length};
        int[] sizeY = new int[]{blockCollection.width, 1, blockCollection.length};
        int[] sizeZ = new int[]{blockCollection.width, blockCollection.height, 1};
        BlockCoord zeroCoord = new BlockCoord(0, 0, 0);

        isSideOpaque[ForgeDirection.DOWN.ordinal()] = areAllOpaque(BlockArea.areaFromSize(zeroCoord, sizeY));
        isSideOpaque[ForgeDirection.UP.ordinal()] = areAllOpaque(BlockArea.areaFromSize(new BlockCoord(0, blockCollection.height - 1, 0), sizeY));
        isSideOpaque[ForgeDirection.NORTH.ordinal()] = areAllOpaque(BlockArea.areaFromSize(zeroCoord, sizeZ));
        isSideOpaque[ForgeDirection.EAST.ordinal()] = areAllOpaque(BlockArea.areaFromSize(new BlockCoord(blockCollection.width - 1, 0, 0), sizeX));
        isSideOpaque[ForgeDirection.SOUTH.ordinal()] = areAllOpaque(BlockArea.areaFromSize(new BlockCoord(0, 0, blockCollection.length - 1), sizeZ));
        isSideOpaque[ForgeDirection.WEST.ordinal()] = areAllOpaque(BlockArea.areaFromSize(zeroCoord, sizeX));

        if (world != null)
        {
            BlockPos pos = getPos();
            IBlockState state = world.getBlockState(pos);
            if (!world.isRemote)
                NetworkHelperServer.sendTileEntityUpdatePacket(this, "microBlocks", YeGamolChattels.network);
            else
                markCacheInvalidClient(fromNBT);

            world.notifyBlockUpdate(pos, state, state, 3);
        }

        markDirty();
    }

    @SideOnly(Side.CLIENT)
    private void markCacheInvalidClient(boolean fromNBT)
    {
        quadCache = null;
    }

    public boolean validateBeingMicroblock()
    {
        boolean allSame = true;
        Block curBlock = blockCollection.getBlock(new BlockCoord(0, 0, 0));
        int curMeta = blockCollection.getMetadata(new BlockCoord(0, 0, 0));
        for (BlockCoord coord : blockCollection)
        {
            if (blockCollection.getBlock(coord) != curBlock || blockCollection.getMetadata(coord) != curMeta)
            {
                allSame = false;
                break;
            }
        }

        if (allSame)
        {
            shouldDropAsItem = false;
            if (world != null)
                world.setBlockState(getPos(), curBlock.getStateFromMeta(curMeta), 3);
        }
        else if (blockCollection.getBlockMultiplicity() > MAX_MICROBLOCK_MAPPINGS)
        {
            BlockMicroBlock.dropAllMicroblockFragments(this, 1.0f);
        }

        return !allSame;
    }

    private boolean areAllOpaque(BlockArea area)
    {
        for (BlockCoord coord : area)
        {
            Block block = blockCollection.getBlock(coord);
            if (!block.isOpaqueCube(block.getStateFromMeta(blockCollection.getMetadata(coord))))
                return false;
        }

        return true;
    }

    public boolean shouldDropAsItem()
    {
        return shouldDropAsItem;
    }

    public void setShouldDropAsItem(boolean shouldDropAsItem)
    {
        this.shouldDropAsItem = shouldDropAsItem;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        writeSyncToNBT(compound);
        return compound;
    }

    protected void writeSyncToNBT(NBTTagCompound compound)
    {
        compound.setTag("microblocks", blockCollection.createTagCompound());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        readSyncFromNBT(compound);
    }

    protected void readSyncFromNBT(NBTTagCompound compound)
    {
        blockCollection = new IvBlockCollection(compound.getCompoundTag("microblocks"), MCRegistryDefault.INSTANCE);
        markCacheInvalid(true);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound compound = new NBTTagCompound();
        writeSyncToNBT(compound);
        return new SPacketUpdateTileEntity(getPos(), 1, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readSyncFromNBT(pkt.getNbtCompound());
    }

    @Override
    public void writeUpdateData(ByteBuf buffer, String context, Object... params)
    {
        if ("microBlocks".equals(context))
        {
            ByteBufUtils.writeTag(buffer, blockCollection.createTagCompound());
        }
    }

    @Override
    public void readUpdateData(ByteBuf buffer, String context)
    {
        if ("microBlocks".equals(context))
        {
            blockCollection = new IvBlockCollection(ByteBufUtils.readTag(buffer), MCRegistryDefault.INSTANCE);
            markCacheInvalid(true);
        }
    }

    @SideOnly(Side.CLIENT)
    public GridQuadCache<Object> getQuadCache()
    {
        return quadCache;
    }
}
