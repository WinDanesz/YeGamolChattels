/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.entities.EntityFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFlag extends Item
{
    public int flagSize;
    public String namePrefix;

    public ItemFlag(int flagSize, String namePrefix)
    {
        super();
        setHasSubtypes(true);
        setMaxDamage(0);

        this.flagSize = flagSize;
        this.namePrefix = namePrefix;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;

        ItemStack stack = player.getHeldItem(hand);
        BlockPos placePos = pos.up();

        EntityFlag entityflag = new EntityFlag(world);
        entityflag.setPosition(placePos.getX(), placePos.getY(), placePos.getZ());
        entityflag.setColor(stack.getMetadata());
        entityflag.setSize(flagSize);

        if (entityflag.canStayAtPosition())
        {
            if (!world.isRemote)
                world.spawnEntity(entityflag);
            stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public String getTranslationKey(ItemStack itemStack)
    {
        return super.getTranslationKey(itemStack) + ".dye" + itemStack.getItemDamage();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab))
            return;

        for (int var4 = 0; var4 < 16; ++var4)
            items.add(new ItemStack(this, 1, var4));
    }
}
