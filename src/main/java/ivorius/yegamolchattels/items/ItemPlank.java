package ivorius.yegamolchattels.items;

import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemPlank extends Item
{
    public ItemPlank()
    {
        setHasSubtypes(true);
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack)
    {
        int dmg = par1ItemStack.getItemDamage();
        String addition = "." + BlockPlanks.EnumType.byMetadata(dmg).getName();

        return super.getTranslationKey() + addition;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab))
            return;

        for (int i = 0; i < 6; i++)
            items.add(new ItemStack(this, 1, i));
    }
}
