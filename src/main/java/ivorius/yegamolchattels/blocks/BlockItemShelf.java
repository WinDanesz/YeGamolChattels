package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvBlockMultiblock;
import ivorius.yegamolchattels.tabs.YGCCreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockItemShelf extends IvBlockMultiblock
{
    public static final int shelfTypes = 3;

    public BlockItemShelf(Material material)
    {
        super(material);
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
        return tileEntity instanceof TileEntityItemShelf && ((TileEntityItemShelf) tileEntity).onRightClick(player, player.getHeldItem(hand), facing.getIndex());
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityItemShelfModel0(world);
    }
}
