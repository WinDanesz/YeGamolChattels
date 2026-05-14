package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvBlockMultiblock;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by lukas on 04.05.14.
 */
public class BlockTablePress extends IvBlockMultiblock
{
    public BlockTablePress()
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

        if (tileEntity instanceof TileEntityTablePress)
        {
            TileEntityTablePress planksRefinement = (TileEntityTablePress) tileEntity;

            if (planksRefinement.tryStoringItem(player.getHeldItem(hand), player))
                return true;
            else if (planksRefinement.tryUsingItem(player.getHeldItem(hand), player))
                return true;
            else if (planksRefinement.tryEquippingItemOnPlayer(player))
                return true;

            return false;
        }

        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityTablePress();
    }
}
