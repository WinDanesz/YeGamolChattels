/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTikiTorch extends ItemBlock
{
    private final Block placedBlock;

    public ItemTikiTorch(Block block)
    {
        super(block);
        this.placedBlock = block;
        maxStackSize = 16;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState clickedState = world.getBlockState(pos);
        Block clickedBlock = clickedState.getBlock();

        BlockPos placePos = pos;
        if (clickedBlock == Blocks.SNOW_LAYER)
        {
            facing = EnumFacing.UP;
        }
        else if (!clickedBlock.isReplaceable(world, pos))
        {
            placePos = pos.offset(facing);
        }

        if (!player.canPlayerEdit(placePos, facing, stack))
            return EnumActionResult.FAIL;

        if (!placedBlock.canPlaceBlockAt(world, placePos))
            return EnumActionResult.FAIL;

        if (!world.isRemote)
        {
            world.setBlockState(placePos, placedBlock.getStateFromMeta(1), 3);
            world.setBlockState(placePos.up(), placedBlock.getStateFromMeta(0), 3);
            world.notifyNeighborsOfStateChange(placePos, placedBlock, false);
            world.notifyNeighborsOfStateChange(placePos.up(), placedBlock, false);

            if (!player.capabilities.isCreativeMode)
                stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }

}
