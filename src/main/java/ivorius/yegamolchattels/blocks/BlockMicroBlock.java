package ivorius.yegamolchattels.blocks;

import ivorius.ivtoolkit.blocks.BlockCoord;
import ivorius.ivtoolkit.blocks.IvBlockCollection;
import ivorius.ivtoolkit.math.AxisAlignedTransform2D;
import ivorius.yegamolchattels.items.ItemBlockFragment;
import ivorius.yegamolchattels.items.ItemChisel;
import ivorius.yegamolchattels.items.ItemClubHammer;
import ivorius.yegamolchattels.items.ItemMicroBlock;
import ivorius.yegamolchattels.items.YGCItems;
import ivorius.yegamolchattels.materials.YGCMaterials;
import ivorius.yegamolchattels.utils.IvBlockCollections;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockMicroBlock extends Block
{
    public BlockMicroBlock()
    {
        super(YGCMaterials.mixed);
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

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityMicroBlock();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityMicroBlock)
            {
                TileEntityMicroBlock microBlock = (TileEntityMicroBlock) tileEntity;
                if (microBlock.shouldDropAsItem())
                {
                    ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
                    ItemMicroBlock.setMicroBlock(stack, microBlock.getBlockCollection());
                    world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
                }
            }
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMicroBlock)
        {
            ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
            ItemMicroBlock.setMicroBlock(stack, ((TileEntityMicroBlock) tileEntity).getBlockCollection());
            return stack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        super.onBlockHarvested(world, pos, state, player);

        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() == YGCItems.clubHammer)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityMicroBlock)
                dropAllMicroblockFragments((TileEntityMicroBlock) tileEntity, ItemClubHammer.FRAGMENT_DROP_CHANCE);
        }
    }

    public static void dropAllMicroblockFragments(TileEntityMicroBlock tileEntityMicroBlock, float dropChance)
    {
        World world = tileEntityMicroBlock.getWorld();
        BlockPos pos = tileEntityMicroBlock.getPos();
        IvBlockCollection collection = tileEntityMicroBlock.getBlockCollection();

        if (world != null && !world.isRemote)
        {
            for (BlockCoord coord : collection)
            {
                Block block = collection.getBlock(coord);
                if (block.getMaterial(block.getDefaultState()) != Material.AIR && world.rand.nextFloat() < dropChance)
                {
                    ItemStack drop = new ItemStack(YGCItems.blockFragment);
                    ItemBlockFragment.setFragment(drop, new ItemChisel.BlockData(block, collection.getMetadata(coord)));
                    world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
                }
            }
        }

        tileEntityMicroBlock.setShouldDropAsItem(false);
        if (world != null)
            world.setBlockToAir(pos);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (!world.isRemote)
        {
            IvBlockCollection collection = ItemMicroBlock.containedMicroBlock(stack);
            TileEntity tileEntity = world.getTileEntity(pos);
            if (collection != null && tileEntity instanceof TileEntityMicroBlock)
                ((TileEntityMicroBlock) tileEntity).setBlockCollection(collection);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityMicroBlock))
        {
            super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity, isActualState);
            return;
        }

        IvBlockCollection collection = ((TileEntityMicroBlock) tileEntity).getBlockCollection();
        double oneX = 1.0 / collection.width;
        double oneY = 1.0 / collection.height;
        double oneZ = 1.0 / collection.length;

        for (BlockCoord coord : collection)
        {
            if (collection.getBlock(coord).getMaterial(collection.getBlock(coord).getDefaultState()) != Material.AIR)
            {
                AxisAlignedBB bb = new AxisAlignedBB(
                        pos.getX() + coord.x * oneX,
                        pos.getY() + coord.y * oneY,
                        pos.getZ() + coord.z * oneZ,
                        pos.getX() + (coord.x + 1) * oneX,
                        pos.getY() + (coord.y + 1) * oneY,
                        pos.getZ() + (coord.z + 1) * oneZ);
                if (bb.intersects(entityBox))
                    collidingBoxes.add(bb);
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMicroBlock)
        {
            TileEntityMicroBlock microBlock = (TileEntityMicroBlock) tileEntity;
            microBlock.setBlockCollection(IvBlockCollections.transform(microBlock.getBlockCollection(), AxisAlignedTransform2D.transform(1, false)));
            microBlock.markCacheInvalid();
        }

        return super.rotateBlock(world, pos, axis);
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
    {
        ItemChisel.MicroBlockFragment hitFragment = ItemChisel.getHoveredFragment(world, pos.getX(), pos.getY(), pos.getZ(), new net.minecraft.util.Vec3(start.x, start.y, start.z), new net.minecraft.util.Vec3(end.x - start.x, end.y - start.y, end.z - start.z));
        if (hitFragment == null)
            return null;

        return new RayTraceResult(hitFragment.getHitPoint(), EnumFacing.byIndex(hitFragment.getInternalSide().ordinal()), pos);
    }
}
