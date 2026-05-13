package ivorius.yegamolchattels.tabs;

import ivorius.yegamolchattels.blocks.YGCBlocks;
import ivorius.yegamolchattels.items.YGCItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public final class YGCCreativeTabs
{
    public static final CreativeTabs tabMain = new CreativeTabs("yegamolchattels")
    {
        @Override
        public ItemStack createIcon()
        {
            return YGCBlocks.snowGlobe != null ? new ItemStack(YGCBlocks.snowGlobe) : ItemStack.EMPTY;
        }
    };

    public static final CreativeTabs tabVitas = new CreativeTabs("ygc_entityvita")
    {
        @Override
        public ItemStack createIcon()
        {
            return YGCItems.entityVita != null ? new ItemStack(YGCItems.entityVita) : ItemStack.EMPTY;
        }
    };

    private YGCCreativeTabs()
    {
    }
}
