package ivorius.yegamolchattels.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemPlank extends Item
{
    private static final String[] VARIANT_NAMES = {"oak", "spruce", "birch", "jungle", "acacia", "big_oak"};

    public ItemPlank()
    {
        setHasSubtypes(true);
    }

    @Override
    public String getTranslationKey(ItemStack par1ItemStack)
    {
        int dmg = par1ItemStack.getItemDamage();
        String variant = dmg >= 0 && dmg < VARIANT_NAMES.length ? VARIANT_NAMES[dmg] : VARIANT_NAMES[0];
        return super.getTranslationKey() + "." + variant;
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
