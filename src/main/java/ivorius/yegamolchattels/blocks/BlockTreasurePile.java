package ivorius.yegamolchattels.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTreasurePile extends BlockFalling
{
    public BlockTreasurePile()
    {
        super(Material.SAND);
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
