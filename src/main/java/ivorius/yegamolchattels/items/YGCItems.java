package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.YGCBlocks;
import ivorius.yegamolchattels.tabs.YGCCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;

@ObjectHolder(YeGamolChattels.MODID)
@Mod.EventBusSubscriber(modid = YeGamolChattels.MODID)
public final class YGCItems
{
    public static final Item bannerSmall = placeholder();
    public static final Item bannerLarge = placeholder();
    public static final Item flagSmall = placeholder();
    public static final Item flagLarge = placeholder();
    public static final Item grindstoneStone = placeholder();
    public static final Item mallet = placeholder();
    public static final Item plank = placeholder();
    public static final Item smoothPlank = placeholder();
    public static final Item refinedPlank = placeholder();
    public static final Item ironSaw = placeholder();
    public static final Item sandpaper = placeholder();
    public static final Item linseedOil = placeholder();
    public static final Item flaxSeeds = placeholder();
    public static final Item flaxFiber = placeholder();
    public static final ItemClubHammer clubHammer = placeholder();
    public static final Item blockFragment = placeholder();
    public static final Item detailChiselIron = placeholder();
    public static final Item carvingChiselIron = placeholder();
    public static final Item entityVita = placeholder();

    private YGCItems()
    {
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T placeholder()
    {
        return null;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        registerItemBlock(registry, YGCBlocks.tiki_torch, new ItemTikiTorch(YGCBlocks.tiki_torch));
        registerItem(registry, "bannerSmall", "bannerSmall", new ItemBanner(0, "small"), YGCCreativeTabs.tabMain);
        registerItem(registry, "bannerLarge", "bannerLarge", new ItemBanner(2, "large"), YGCCreativeTabs.tabMain);
        registerItemBlock(registry, YGCBlocks.statue, new ItemStatue(YGCBlocks.statue, 0));
        registerItem(registry, "entity_vita", "ygcEntityVita", new ItemEntityVita(), YGCCreativeTabs.tabVitas);
        registerItemBlock(registry, YGCBlocks.treasure_pile);
        registerItem(registry, "flagSmall", "flagSmall", new ItemFlag(0, "small"), YGCCreativeTabs.tabMain);
        registerItem(registry, "flagLarge", "flagLarge", new ItemFlag(2, "large"), YGCCreativeTabs.tabMain);
        registerItemBlock(registry, YGCBlocks.grandfather_clock, new ItemGrandfatherClock(YGCBlocks.grandfather_clock));
        registerItemBlock(registry, YGCBlocks.weapon_rack, new ItemWeaponRack(YGCBlocks.weapon_rack));
        registerItemBlock(registry, YGCBlocks.grindstone, new ItemGrindstone(YGCBlocks.grindstone));
        registerItem(registry, "grindstoneStone", "grindstoneStone", new ItemGrindstoneStone(), YGCCreativeTabs.tabMain);
        registerItemBlock(registry, YGCBlocks.gong, new ItemGong(YGCBlocks.gong));
        Item malletItem = new Item().setMaxStackSize(1);
        registerItem(registry, "mallet", "mallet", malletItem, YGCCreativeTabs.tabMain);
        registerItemBlock(registry, YGCBlocks.pedestal, new ItemPedestal(YGCBlocks.pedestal));
       // registerItemBlock(registry, YGCBlocks.itemShelf, new ItemItemShelf(YGCBlocks.itemShelf));
//        registerItemBlock(registry, YGCBlocks.snowGlobe);
//        registerItem(registry, "plank", "plank", new ItemPlank().setHasSubtypes(true).setMaxDamage(0), YGCCreativeTabs.tabMain);
//        registerItem(registry, "smooth_plank", "smoothPlank", new ItemPlank().setHasSubtypes(true).setMaxDamage(0), YGCCreativeTabs.tabMain);
//        registerItem(registry, "refined_plank", "refinedPlank", new ItemPlank().setHasSubtypes(true).setMaxDamage(0), YGCCreativeTabs.tabMain);
//        registerItemBlock(registry, YGCBlocks.sawBench, new ItemSawBench(YGCBlocks.sawBench));
//        registerItemBlock(registry, YGCBlocks.tablePress, new ItemTablePress(YGCBlocks.tablePress));
//        Item sandpaperItem = new Item().setMaxDamage(2048).setMaxStackSize(1).setNoRepair();
//        registerItem(registry, "sandpaper", "sandpaper", sandpaperItem, YGCCreativeTabs.tabMain);
//        Item linseedOilItem = new Item().setMaxDamage(2048).setMaxStackSize(1).setNoRepair();
//        registerItem(registry, "linseed_oil", "ygcLinseedOil", linseedOilItem, YGCCreativeTabs.tabMain);
//        Item ironSawItem = new ItemSaw().setMaxDamage(128).setMaxStackSize(1);
//        registerItem(registry, "iron_saw", "ygcSaw", ironSawItem, YGCCreativeTabs.tabMain);
//        registerItemBlock(registry, YGCBlocks.flaxPlant);
//        registerItem(registry, "flax_seeds", "ygcFlaxSeeds", new ItemFlaxSeeds(YGCBlocks.flaxPlant, Blocks.FARMLAND), YGCCreativeTabs.tabMain);
//        registerItem(registry, "flax_fiber", "ygcFlaxFiber", new Item(), YGCCreativeTabs.tabMain);
//        registerItemBlock(registry, YGCBlocks.microBlock, new ItemMicroBlock(YGCBlocks.microBlock));
//        Item detailChisel = new ItemChisel(0, 1.0f, 0.0f, Item.ToolMaterial.IRON, Collections.emptySet(), true).setMaxDamage(256).setMaxStackSize(1).setNoRepair();
//        registerItem(registry, "iron_chisel_point", "ygcChiselIron_point", detailChisel, YGCCreativeTabs.tabMain);
//        Item carvingChisel = new ItemChisel(1, 0.9f, 0.0f, Item.ToolMaterial.IRON, Collections.emptySet(), false).setMaxDamage(256).setMaxStackSize(1).setNoRepair();
//        registerItem(registry, "iron_chisel", "ygcChiselIron", carvingChisel, YGCCreativeTabs.tabMain);
//        ItemClubHammer hammer = (ItemClubHammer) new ItemClubHammer(0.0f, Item.ToolMaterial.IRON, Collections.emptySet()).setMaxDamage(512).setMaxStackSize(1).setNoRepair();
//        registerItem(registry, "club_hammer", "ygcClubHammer", hammer, YGCCreativeTabs.tabMain);
//        registerItem(registry, "block_fragment", "ygcBlockFragment", new ItemBlockFragment(), null);
//        registerItemBlock(registry, YGCBlocks.lootChest);
    }

    private static <T extends Item> T registerItem(IForgeRegistry<Item> registry, String name, String translationKey, T item, net.minecraft.creativetab.CreativeTabs creativeTab)
    {
        item.setRegistryName(YeGamolChattels.MODID, name);
        item.setTranslationKey(translationKey);
        if (creativeTab != null)
            item.setCreativeTab(creativeTab);
        registry.register(item);
        return item;
    }

    private static void registerItemBlock(IForgeRegistry<Item> registry, Block block)
    {
        Item itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        registry.register(itemBlock);
    }

    private static void registerItemBlock(IForgeRegistry<Item> registry, Block block, Item itemBlock)
    {
        itemBlock.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
        registry.register(itemBlock);
    }
}
