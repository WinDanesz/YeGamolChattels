package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvBlockMultiblock;
import ivorius.yegamolchattels.tabs.YGCCreativeTabs;
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

public class BlockGong extends IvBlockMultiblock
{
    public final int gongSize;

    public BlockGong(Material material, int gongSize)
    {
        super(material);
        this.gongSize = gongSize;
        setCreativeTab(YGCCreativeTabs.tabMain);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityGong)
            ((TileEntityGong) tileEntity).hitGong(player.getHeldItem(hand), player);
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityGong();
    }

    @Override
    public void parentBlockHarvestItem(World world, ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock tileEntity, BlockPos pos, IBlockState state)
    {
        spawnAsEntity(world, pos, new ItemStack(this, 1, damageDropped(state)));
    }
}
