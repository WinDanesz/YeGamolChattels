package ivorius.yegamolchattels.crafting;

import ivorius.yegamolchattels.YGCConfig;
import ivorius.yegamolchattels.blocks.PlankSawEntry;
import ivorius.yegamolchattels.blocks.PlankSawRegistry;
import ivorius.yegamolchattels.blocks.PlanksRefinementEntry;
import ivorius.yegamolchattels.blocks.PlanksRefinementRegistry;
import ivorius.yegamolchattels.items.YGCItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class YGCCrafting
{
    public static void init()
    {
        OreDictionary.registerOre(OreDictionaryConstants.DC_FLAX_SEEDS, new ItemStack(YGCItems.flaxSeeds));
        OreDictionary.registerOre(OreDictionaryConstants.DC_FLAX_FIBER, new ItemStack(YGCItems.flaxFiber));

        for (int meta = 0; meta < 6; meta++)
        {
            OreDictionary.registerOre(OreDictionaryConstants.DC_SINGLE_PLANK_WOOD, new ItemStack(YGCItems.plank, 1, meta));
            OreDictionary.registerOre(OreDictionaryConstants.DC_SINGLE_PLANK_WOOD_SMOOTHED, new ItemStack(YGCItems.smoothPlank, 1, meta));
            OreDictionary.registerOre(OreDictionaryConstants.DC_SINGLE_PLANK_WOOD_REFINED, new ItemStack(YGCItems.refinedPlank, 1, meta));
        }

        for (PlankSawRegistry.Entry entry : YGCConfig.customPlankSawing)
            PlankSawRegistry.addSawing(entry);

        PlankSawRegistry.addSawing(new PlankSawEntry(new ItemStack(Blocks.LOG, 1, 0), new ItemStack(YGCItems.plank, 1, 0)));
        PlankSawRegistry.addSawing(new PlankSawEntry(new ItemStack(Blocks.LOG, 1, 1), new ItemStack(YGCItems.plank, 1, 1)));
        PlankSawRegistry.addSawing(new PlankSawEntry(new ItemStack(Blocks.LOG, 1, 2), new ItemStack(YGCItems.plank, 1, 2)));
        PlankSawRegistry.addSawing(new PlankSawEntry(new ItemStack(Blocks.LOG, 1, 3), new ItemStack(YGCItems.plank, 1, 3)));
        PlankSawRegistry.addSawing(new PlankSawEntry(new ItemStack(Blocks.LOG2, 1, 0), new ItemStack(YGCItems.plank, 1, 4)));
        PlankSawRegistry.addSawing(new PlankSawEntry(new ItemStack(Blocks.LOG2, 1, 1), new ItemStack(YGCItems.plank, 1, 5)));

        for (PlanksRefinementRegistry.Entry entry : YGCConfig.customPlankRefinement)
            PlanksRefinementRegistry.addRefinement(entry);

        for (int meta = 0; meta < 6; meta++)
        {
            PlanksRefinementRegistry.addRefinement(new PlanksRefinementEntry(new ItemStack(YGCItems.plank, 1, meta), new ItemStack(YGCItems.smoothPlank, 1, meta), YGCItems.sandpaper, null));
            PlanksRefinementRegistry.addRefinement(new PlanksRefinementEntry(new ItemStack(YGCItems.smoothPlank, 1, meta), new ItemStack(YGCItems.refinedPlank, 1, meta), YGCItems.linseedOil, new ItemStack(Items.GLASS_BOTTLE)));
        }

        MinecraftForge.addGrassSeed(new ItemStack(YGCItems.flaxSeeds), 2);
    }
}
