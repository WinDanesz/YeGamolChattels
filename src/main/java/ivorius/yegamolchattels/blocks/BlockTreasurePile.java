package ivorius.yegamolchattels.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTreasurePile extends BlockFalling
{
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");

    public BlockTreasurePile()
    {
        super(Material.SAND);
        setDefaultState(blockState.getBaseState()
                .withProperty(NORTH, false)
                .withProperty(EAST, false)
                .withProperty(SOUTH, false)
                .withProperty(WEST, false));
    }

    @Override
    public void updateTick(World world, net.minecraft.util.math.BlockPos pos, IBlockState state, Random random)
    {
        super.updateTick(world, pos, state, random);
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state
                .withProperty(NORTH, connectsTo(world, pos.north()))
                .withProperty(EAST, connectsTo(world, pos.east()))
                .withProperty(SOUTH, connectsTo(world, pos.south()))
                .withProperty(WEST, connectsTo(world, pos.west()));
    }

    private boolean connectsTo(IBlockAccess world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() == this;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random random, int fortune)
    {
        return Items.GOLD_INGOT;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 7;
    }
}
