package ivorius.yegamolchattels.client;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.YGCBlocks;
import ivorius.yegamolchattels.items.ItemEntityVita;
import ivorius.yegamolchattels.items.YGCItems;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = YeGamolChattels.MODID)
public class YGCModelRegistrationHandler
{
    private static final String[] WOOD_VARIANT_NAMES = {"oak", "spruce", "birch", "jungle", "acacia", "big_oak"};

    private YGCModelRegistrationHandler() {}

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event)
    {
        // ItemBlocks — looked up via the already-registered block references
        registerFromBlock(YGCBlocks.tiki_torch,        "tiki_torch");
        registerFromBlock(YGCBlocks.statue,            "statue");
        registerFromBlock(YGCBlocks.treasure_pile,     "treasure_pile");
        registerFromBlock(YGCBlocks.grandfather_clock, "grandfather_clock");
        registerFromBlock(YGCBlocks.weapon_rack,       "weapon_rack");
        registerFromBlock(YGCBlocks.grindstone,        "grindstone");
        registerFromBlock(YGCBlocks.gong_small,       "gong_small");
        registerFromBlock(YGCBlocks.gong_medium,      "gong_medium");
        registerFromBlock(YGCBlocks.gong_large,       "gong_large");
        registerSubtypedFromBlock(YGCBlocks.pedestal,  "pedestal_", 5);
        registerFromBlock(YGCBlocks.item_shelf,       "item_shelf");
        registerFromBlock(YGCBlocks.snow_globe,       "snow_globe");
        registerFromBlock(YGCBlocks.plank_saw,        "plank_saw");
        registerFromBlock(YGCBlocks.table_press,      "table_press");
        registerFromBlock(YGCBlocks.loot_chest,       "loot_chest");

        // Standalone items — looked up directly from the registry to avoid
        // @ObjectHolder injection-timing issues
        registerSubtypedFromRegistry("bannerSmall", "bannersmall", 16);
        registerSubtypedFromRegistry("bannerLarge", "bannerlarge", 16);
        registerSubtypedFromRegistry("flagSmall", "flagsmall", 16);
        registerSubtypedFromRegistry("flagLarge", "flaglarge", 16);
        registerFromRegistry("grindstonestone", "grindstonestone");
        registerFromRegistry("mallet",          "mallet");
        registerFromRegistry("entity_vita",     "entity_vita");
        registerMetaModelsFromRegistry("plank", "plank_", WOOD_VARIANT_NAMES);
        registerMetaModelsFromRegistry("smooth_plank", "plank_smooth_", WOOD_VARIANT_NAMES);
        registerMetaModelsFromRegistry("refined_plank", "plank_refined_", WOOD_VARIANT_NAMES);
        registerFromRegistry("iron_saw", "iron_saw");
        registerFromRegistry("sandpaper", "sandpaper");
        registerFromRegistry("linseed_oil", "linseed_oil");
        registerFromRegistry("flax_seeds", "flax_seeds");
        registerFromRegistry("flax_fiber", "flax_fiber");
    }

    @SubscribeEvent
    public static void onItemColors(ColorHandlerEvent.Item event)
    {
        registerClothTintFromRegistry(event, "bannerSmall");
        registerClothTintFromRegistry(event, "bannerLarge");
        registerClothTintFromRegistry(event, "flagSmall");
        registerClothTintFromRegistry(event, "flagLarge");

        if (YGCItems.entityVita != null)
        {
            event.getItemColors().registerItemColorHandler(
                    (stack, tintIndex) -> tintIndex == 0 && stack.getItem() instanceof ItemEntityVita
                            ? ((ItemEntityVita) stack.getItem()).getColorFromItemstack(stack, tintIndex)
                            : -1,
                    YGCItems.entityVita);
        }
    }

    private static void registerFromBlock(net.minecraft.block.Block block, String modelName)
    {
        if (block == null) return;
        Item item = Item.getItemFromBlock(block);
        if (item != null)
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(YeGamolChattels.MODID + ":" + modelName, "inventory"));
    }

    private static void registerSubtypedFromBlock(net.minecraft.block.Block block, String modelPrefix, int variants)
    {
        if (block == null)
            return;

        Item item = Item.getItemFromBlock(block);
        if (item == null)
            return;

        for (int meta = 0; meta < variants; meta++)
        {
            ModelLoader.setCustomModelResourceLocation(item, meta,
                    new ModelResourceLocation(YeGamolChattels.MODID + ":" + modelPrefix + meta, "inventory"));
        }
    }

    private static void registerFromRegistry(String registryName, String modelName)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(YeGamolChattels.MODID, registryName));
        if (item != null)
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(YeGamolChattels.MODID + ":" + modelName, "inventory"));
    }

    private static void registerClothTint(ColorHandlerEvent.Item event, Item item)
    {
        if (item == null)
            return;

        event.getItemColors().registerItemColorHandler(
                (stack, tintIndex) -> tintIndex == 1 ? EnumDyeColor.byDyeDamage(stack.getMetadata()).getColorValue() : -1,
                item);
    }

    private static void registerClothTintFromRegistry(ColorHandlerEvent.Item event, String registryName)
    {
        registerClothTint(event, ForgeRegistries.ITEMS.getValue(new ResourceLocation(YeGamolChattels.MODID, registryName)));
    }

    private static void registerSubtypedFromRegistry(String registryName, String modelName, int variants)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(YeGamolChattels.MODID, registryName));
        if (item == null)
            return;

        ModelResourceLocation location = new ModelResourceLocation(YeGamolChattels.MODID + ":" + modelName, "inventory");
        for (int meta = 0; meta < variants; meta++)
            ModelLoader.setCustomModelResourceLocation(item, meta, location);
    }

    private static void registerMetaModelsFromRegistry(String registryName, String modelPrefix, String[] suffixes)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(YeGamolChattels.MODID, registryName));
        if (item == null)
            return;

        for (int meta = 0; meta < suffixes.length; meta++)
        {
            ModelLoader.setCustomModelResourceLocation(item, meta,
                    new ModelResourceLocation(YeGamolChattels.MODID + ":" + modelPrefix + suffixes[meta], "inventory"));
        }
    }
}
