package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvBlockMultiblock;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.network.NetworkHelperServer;
import ivorius.yegamolchattels.utils.IvAABBs;
import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.items.ItemSaw;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by lukas on 04.05.14.
 */
public class BlockSawBench extends IvBlockMultiblock
{
    public BlockSawBench()
    {
        super(Material.WOOD);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void parentBlockHarvestItem(World world, IvTileEntityMultiBlock tileEntity, BlockPos pos, IBlockState state)
    {
        spawnAsEntity(world, pos, new ItemStack(this));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IvTileEntityMultiBlock tileEntity = getValidatedTotalParent(this, world, pos);

        if (tileEntity instanceof TileEntitySawBench)
        {
            TileEntitySawBench plankSaw = (TileEntitySawBench) tileEntity;

            if (plankSaw.tryStoringItem(player.getHeldItem(hand), player))
                return true;
            else if (player.isSneaking() && plankSaw.tryEquippingItemOnPlayer(player))
                return true;
            else if (canUseItemToSaw(player.getHeldItem(hand)))
            {
                if (!world.isRemote)
                {
                    NetworkHelperServer.sendTileEntityUpdatePacket(plankSaw, "sawOpenGui", YeGamolChattels.network, player);
                }

                return true;
            }
        }

        return false;
    }

    public static boolean canUseItemToSaw(ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof ItemSaw;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntitySawBench();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
    {
        IvTileEntityMultiBlock tileEntity = getValidatedTotalParent(this, blockAccess, pos);

        if (tileEntity instanceof TileEntitySawBench)
            return IvAABBs.boundsIntersection(tileEntity.getRotatedBB(-1, -1, -0.45, 2, 1.8, 1.45), pos.getX(), pos.getY(), pos.getZ());

        return FULL_BLOCK_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return getBoundingBox(state, world, pos).offset(pos);
    }
}
