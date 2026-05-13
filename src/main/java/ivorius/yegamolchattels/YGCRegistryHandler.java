package ivorius.yegamolchattels;

import ivorius.yegamolchattels.blocks.YGCBlocks;
import ivorius.yegamolchattels.items.YGCItems;
import ivorius.yegamolchattels.tabs.YGCCreativeTabs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class YGCRegistryHandler
{
    public static void init()
    {
        YGCCreativeTabs.tabMain = new CreativeTabs("yegamolchattels")
        {
            @Override
            public ItemStack createIcon()
            {
                return ItemStack.EMPTY;
            }
        };

        YGCCreativeTabs.tabVitas = new CreativeTabs("ygc_entityvita")
        {
            @Override
            public ItemStack createIcon()
            {
                return ItemStack.EMPTY;
            }
        };

        YGCBlocks.blockTreasurePileRenderType = -1;
        YGCBlocks.blockTikiTorchRenderType = -1;
        YGCBlocks.blockMicroBlockRenderType = -1;
    }
}
