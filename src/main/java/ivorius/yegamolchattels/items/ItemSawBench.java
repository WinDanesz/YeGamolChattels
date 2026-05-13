/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.multiblock.IvMultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemSawBench extends ItemBlock
{
    private final Block placedBlock;

    public ItemSawBench(Block block)
    {
        super(block);
        this.placedBlock = block;
        maxStackSize = 16;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        int rotation = IvMultiBlockHelper.getRotation(player);
        List<int[]> positions = IvMultiBlockHelper.getRotatedPositions(rotation, 2, 2, 2);

        IvMultiBlockHelper multiBlockHelper = new IvMultiBlockHelper();
        if (multiBlockHelper.beginPlacing(positions, world, pos.getX(), pos.getY(), pos.getZ(), facing.getIndex(), stack, player, placedBlock, 0, rotation))
        {
            for (int[] position : multiBlockHelper)
            {
                multiBlockHelper.placeBlock(position);
            }

            stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
