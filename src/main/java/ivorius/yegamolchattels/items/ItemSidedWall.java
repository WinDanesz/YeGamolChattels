/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSidedWall extends ItemBlock
{
    private final Block placedBlock;

    public ItemSidedWall(Block block)
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

        int i1 = MathHelper.floor_double((player.rotationYaw * 4F) / 360F + 0.5D) & 3;

        if (i1 == 0)
            i1 = 0;
        else if (i1 == 1)
            i1 = 3;
        else if (i1 == 2)
            i1 = 1;
        else if (i1 == 3)
            i1 = 2;

        int type = player.getHeldItem(hand).getItemDamage();

        world.setBlockState(placePos, placedBlock.getStateFromMeta(type | (i1 << 2)), 3);

        player.getHeldItem(hand).shrink(1);

        return EnumActionResult.SUCCESS;
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack)
    {
        return super.getTranslationKey(par1ItemStack) + ".meta" + par1ItemStack.getItemDamage();
    }
}
