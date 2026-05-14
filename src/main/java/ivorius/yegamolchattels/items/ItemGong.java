/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.blocks.BlockGong;
import ivorius.yegamolchattels.multiblock.IvMultiBlockHelper;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.blocks.TileEntityGong;
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

public class ItemGong extends ItemBlock
{
    public ItemGong(Block block)
    {
        super(block);
        maxStackSize = 1;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        int gongSize = ((BlockGong) block).gongSize;
        int gongType = 0;

        int rotation = IvMultiBlockHelper.getRotation(player);
        List<int[]> positions = IvMultiBlockHelper.getRotatedPositions(rotation, gongSize + 1, gongSize + 1, 1);

        IvMultiBlockHelper multiBlockHelper = new IvMultiBlockHelper();
        if (multiBlockHelper.beginPlacing(positions, world, pos.getX(), pos.getY(), pos.getZ(), facing.getIndex(), stack, player, block, 0, rotation))
        {
            for (int[] position : multiBlockHelper)
            {
                IvTileEntityMultiBlock tileEntity = multiBlockHelper.placeBlock(position);

                if (tileEntity instanceof TileEntityGong)
                {
                    ((TileEntityGong) tileEntity).gongType = gongType;
                }
            }

            stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }
}
