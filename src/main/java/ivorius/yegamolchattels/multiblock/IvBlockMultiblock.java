package ivorius.yegamolchattels.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class IvBlockMultiblock extends Block
{
    protected IvBlockMultiblock(Material material)
    {
        super(material);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof IvTileEntityMultiBlock)
        {
            IvTileEntityMultiBlock multiblock = (IvTileEntityMultiBlock) tileEntity;
            multiblock.multiblockInvalid = true;
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
        super.onBlockExploded(world, pos, explosion);
    }

    public void parentBlockDropItemContents(World world, IvTileEntityMultiBlock tileEntity, BlockPos pos, IBlockState state)
    {
    }

    public void parentBlockHarvestItem(World world, IvTileEntityMultiBlock tileEntity, BlockPos pos, IBlockState state)
    {
    }

    public static boolean validateMultiblock(Block block, IBlockAccess access, BlockPos pos)
    {
        return access.getBlockState(pos).getBlock() == block;
    }

    public static IvTileEntityMultiBlock getValidatedTotalParent(Block block, IBlockAccess access, BlockPos pos)
    {
        if (!validateMultiblock(block, access, pos))
            return null;

        TileEntity tileEntity = access.getTileEntity(pos);
        return tileEntity instanceof IvTileEntityMultiBlock ? ((IvTileEntityMultiBlock) tileEntity).getTotalParent() : null;
    }
}
