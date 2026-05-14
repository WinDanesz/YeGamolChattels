package ivorius.yegamolchattels.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGrandfatherClock extends Block
{
    public static final PropertyEnum<Part> PART = PropertyEnum.create("part", Part.class);
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);

    public BlockGrandfatherClock(Material material)
    {
        super(material);
        setDefaultState(blockState.getBaseState().withProperty(PART, Part.LOWER).withProperty(ROTATION, 0));
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
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityGrandfatherClock();
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(ROTATION) << 1) | state.getValue(PART).meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState()
                .withProperty(PART, Part.fromMeta(meta & 1))
                .withProperty(ROTATION, (meta >> 1) & 3);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PART, ROTATION);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(PART) == Part.UPPER ? Item.getItemFromBlock(net.minecraft.init.Blocks.AIR) : Item.getItemFromBlock(this);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        dropClockIfCantStay(world, pos);
    }

    private void dropClockIfCantStay(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this)
            return;

        if (state.getValue(PART) == Part.LOWER && world.getBlockState(pos.up()).getBlock() != this)
        {
            spawnAsEntity(world, pos, new net.minecraft.item.ItemStack(this));
            world.setBlockToAir(pos);
        }
        else if (state.getValue(PART) == Part.UPPER && world.getBlockState(pos.down()).getBlock() != this)
        {
            world.setBlockToAir(pos);
        }
    }

    public enum Part implements IStringSerializable
    {
        LOWER(0, "lower"),
        UPPER(1, "upper");

        private final int meta;
        private final String name;

        Part(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public static Part fromMeta(int meta)
        {
            return meta == UPPER.meta ? UPPER : LOWER;
        }

        @Override
        public String getName()
        {
            return name;
        }
    }
}
