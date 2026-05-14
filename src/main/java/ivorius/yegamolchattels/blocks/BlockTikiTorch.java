package ivorius.yegamolchattels.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTikiTorch extends Block
{
    public static final PropertyEnum<Part> PART = PropertyEnum.create("part", Part.class);

    public BlockTikiTorch()
    {
        super(Material.CIRCUITS);
        setTickRandomly(true);
        setHardness(0.0F);
        setSoundType(SoundType.WOOD);
        setLightLevel(0.9375F);
        setDefaultState(blockState.getBaseState().withProperty(PART, Part.UPPER));
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return NULL_AABB;
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
        return state.getValue(PART) == Part.UPPER ? Item.getItemFromBlock(this) : Items.AIR;
    }

    private boolean canPlaceTorchOn(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return state.isSideSolid(world, pos, EnumFacing.UP)
                || block instanceof BlockFence
                || block == Blocks.NETHER_BRICK_FENCE
                || block == Blocks.GLASS
                || (block == this && state.getValue(PART) == Part.LOWER);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return pos.getY() < world.getHeight() - 1
                && canPlaceTorchOn(world, pos.down())
                && super.canPlaceBlockAt(world, pos)
                && world.mayPlace(this, pos.up(), false, EnumFacing.UP, null);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
    {
        if (state.getValue(PART) == Part.UPPER)
        {
            validateStructure(world, pos, state, true);
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        validateStructure(world, pos, state, true);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        validateStructure(world, pos, state, true);
    }

    private boolean validateStructure(World world, BlockPos pos, IBlockState state, boolean dropItem)
    {
        Part part = state.getValue(PART);
        BlockPos lowerPos = part == Part.LOWER ? pos : pos.down();
        BlockPos upperPos = lowerPos.up();
        IBlockState lowerState = world.getBlockState(lowerPos);
        IBlockState upperState = world.getBlockState(upperPos);

        boolean valid = lowerState.getBlock() == this
                && lowerState.getValue(PART) == Part.LOWER
                && upperState.getBlock() == this
                && upperState.getValue(PART) == Part.UPPER
                && canPlaceTorchOn(world, lowerPos.down());

        if (!valid)
        {
            if (dropItem && upperState.getBlock() == this && upperState.getValue(PART) == Part.UPPER && !world.isRemote)
            {
                spawnAsEntity(world, upperPos, new net.minecraft.item.ItemStack(this));
            }

            if (lowerState.getBlock() == this)
            {
                world.setBlockToAir(lowerPos);
            }

            if (upperState.getBlock() == this)
            {
                world.setBlockToAir(upperPos);
            }
        }

        return valid;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random)
    {
        if (state.getValue(PART) == Part.UPPER)
        {
            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.7D;
            double z = pos.getZ() + 0.5D;
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(PART).meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(PART, Part.fromMeta(meta));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, PART);
    }

    public enum Part implements IStringSerializable
    {
        UPPER(0, "upper"),
        LOWER(1, "lower");

        private final int meta;
        private final String name;

        Part(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public static Part fromMeta(int meta)
        {
            return meta == LOWER.meta ? LOWER : UPPER;
        }

        @Override
        public String getName()
        {
            return name;
        }
    }
}
