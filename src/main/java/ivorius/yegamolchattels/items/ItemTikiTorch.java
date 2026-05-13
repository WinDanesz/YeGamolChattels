/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.blocks.YGCBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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
        BlockPos placePos = pos.offset(facing);
        if (!placedBlock.canPlaceBlockAt(world, placePos))
            return EnumActionResult.FAIL;

        world.setBlockState(placePos, placedBlock.getStateFromMeta(1), 3);
        world.setBlockState(placePos.up(), placedBlock.getStateFromMeta(0), 3);
        world.notifyNeighborsOfStateChange(placePos, placedBlock, false);
        world.notifyNeighborsOfStateChange(placePos.up(), placedBlock, false);
        player.getHeldItem(hand).shrink(1);
        return EnumActionResult.SUCCESS;
    }

}
