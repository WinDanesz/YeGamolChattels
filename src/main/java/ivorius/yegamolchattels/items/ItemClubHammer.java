/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.blocks.TileEntityMicroBlock;
import ivorius.yegamolchattels.blocks.YGCBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by lukas on 11.07.14.
 */
public class ItemClubHammer extends ItemTool
{
    public static final float FRAGMENT_DROP_CHANCE = 0.9f;

    public ItemClubHammer(float damage, ToolMaterial material, Set damageVSBlocks)
    {
        super(material, damageVSBlocks);
    }

    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        return -1;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state)
    {
        return 1.5f;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        Block block = world.getBlockState(pos).getBlock();

        if (block == YGCBlocks.microBlock)
        {
            block.rotateBlock(world, pos, EnumFacing.UP);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    public void modifyDrops(World world, Block block, int metadata, ItemStack stack, int x, int y, int z, List<ItemStack> drops)
    {
        if (isMicroblockable(block, metadata))
        {
            drops.clear();

            Random rand = world.rand;
            int maxMicroblocks = TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_X * TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Y * TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Z;
            int droppedFragments = 0;
            for (int i = 0; i < maxMicroblocks; i++)
            {
                if (rand.nextFloat() < FRAGMENT_DROP_CHANCE)
                    droppedFragments++;
            }

            while (droppedFragments > 0)
            {
                int stackDrop = Math.min(droppedFragments, 64);
                ItemStack drop = new ItemStack(YGCItems.blockFragment, stackDrop);
                ItemBlockFragment.setFragment(drop, new ItemChisel.BlockData(block, (byte) block.getMetaFromState(world.getBlockState(new BlockPos(x, y, z)))));
                droppedFragments -= stackDrop;
                drops.add(drop);
            }
        }
    }

    public static boolean isMicroblockable(World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block.isOpaqueCube(state) && world.getTileEntity(pos) == null && block.getBlockHardness(state, world, pos) >= 0.0f;
    }

    public static boolean isMicroblockable(Block block, int metadata)
    {
        IBlockState state = block.getStateFromMeta(metadata);
        return block.isOpaqueCube(state) && !block.hasTileEntity(state);
    }
}
