/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.multiblock.IvMultiBlockHelper;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.blocks.EnumPedestalEntry;
import ivorius.yegamolchattels.blocks.TileEntityPedestal;
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

public class ItemPedestal extends ItemBlock
{
    private final Block placedBlock;

    public ItemPedestal(Block block)
    {
        super(block);
        this.placedBlock = block;
        maxStackSize = 16;
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        int identifier = stack.getItemDamage();
        EnumPedestalEntry entry = EnumPedestalEntry.getEntry(identifier);

        int rotation = IvMultiBlockHelper.getRotation(player);
        List<int[]> positions = IvMultiBlockHelper.getRotatedPositions(rotation, entry.size[0], entry.size[1], entry.size[2]);

        IvMultiBlockHelper multiBlockHelper = new IvMultiBlockHelper();
        if (multiBlockHelper.beginPlacing(positions, world, pos.getX(), pos.getY(), pos.getZ(), facing.getIndex(), stack, player, placedBlock, identifier, rotation))
        {
            for (int[] position : multiBlockHelper)
            {
                IvTileEntityMultiBlock tileEntity = multiBlockHelper.placeBlock(position);

                if (tileEntity instanceof TileEntityPedestal)
                {
                    ((TileEntityPedestal) tileEntity).pedestalIdentifier = stack.getItemDamage();
                }
            }

            stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab))
            return;

        for (int i = 0; i < EnumPedestalEntry.getNumberOfEntries(); i++)
            items.add(new ItemStack(this, 1, i));
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack)
    {
        int damage = par1ItemStack.getItemDamage();

        String material = "";
        if (damage == 0)
            material = ".planks";
        else if (damage == 1)
            material = ".stone";
        else if (damage == 2)
            material = ".iron";
        else if (damage == 3)
            material = ".gold";
        else if (damage == 4)
            material = ".diamond";
        else if (damage == 5)
            material = ".nether";

        return super.getTranslationKey(par1ItemStack) + material;
    }

}
