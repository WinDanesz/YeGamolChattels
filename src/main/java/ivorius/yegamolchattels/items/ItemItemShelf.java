/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.multiblock.IvMultiBlockHelper;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.blocks.BlockItemShelf;
import ivorius.yegamolchattels.blocks.TileEntityItemShelf;
import ivorius.yegamolchattels.blocks.TileEntityItemShelfModel0;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemItemShelf extends ItemBlock
{
    private final Block placedBlock;

    public ItemItemShelf(Block block)
    {
        super(block);
        this.placedBlock = block;
        maxStackSize = 1;
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        int shelfType = stack.getItemDamage();

        int i1 = IvMultiBlockHelper.getRotation(player);
        List<int[]> positions = IvMultiBlockHelper.getRotatedPositions(TileEntityItemShelfModel0.getPositionsForType(shelfType), i1);

        IvMultiBlockHelper multiBlockHelper = new IvMultiBlockHelper();
        if (multiBlockHelper.beginPlacing(positions, world, pos.getX(), pos.getY(), pos.getZ(), facing.getIndex(), stack, player, placedBlock, shelfType, i1))
        {
            for (int[] position : multiBlockHelper)
            {
                IvTileEntityMultiBlock tileEntity = multiBlockHelper.placeBlock(position);

                if (tileEntity != null && tileEntity instanceof TileEntityItemShelf)
                {

                }
            }

            stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack)
    {
        int type = par1ItemStack.getItemDamage();

        return super.getTranslationKey(par1ItemStack) + ".type" + type;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab))
            return;

        for (int size = 0; size < BlockItemShelf.shelfTypes; size++)
            items.add(new ItemStack(this, 1, size));
    }

}
