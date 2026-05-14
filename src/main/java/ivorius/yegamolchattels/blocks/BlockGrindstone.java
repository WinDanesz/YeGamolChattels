package ivorius.yegamolchattels.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGrindstone extends Block
{
    public BlockGrindstone(Material material)
    {
        super(material);
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityGrindstone();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityGrindstone)
        {
            TileEntityGrindstone grindstone = (TileEntityGrindstone) tileEntity;
            if (!player.getHeldItem(hand).isEmpty())
            {
                if (!grindstone.tryApplyingItem(player.getHeldItem(hand), player))
                    grindstone.tryRepairingItem(player.getHeldItem(hand), player);
            }
            else
            {
                grindstone.increaseGrindstoneRotation();
            }
            return true;
        }
        return false;
    }
}
