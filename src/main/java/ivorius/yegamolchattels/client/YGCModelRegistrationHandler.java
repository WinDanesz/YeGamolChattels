package ivorius.yegamolchattels.client;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.YGCBlocks;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = YeGamolChattels.MODID)
public class YGCModelRegistrationHandler
{
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
        registerFromBlock(YGCBlocks.gong,              "gong");
        registerFromBlock(YGCBlocks.pedestal,          "pedestal");

        // Standalone items — looked up directly from the registry to avoid
        // @ObjectHolder injection-timing issues
        registerFromRegistry("bannersmall",     "bannersmall");
        registerFromRegistry("bannerlarge",     "bannerlarge");
        registerFromRegistry("flagsmall",       "flagsmall");
        registerFromRegistry("flaglarge",       "flaglarge");
        registerFromRegistry("grindstonestone", "grindstonestone");
        registerFromRegistry("mallet",          "mallet");
        registerFromRegistry("entity_vita",     "entity_vita");
    }

    private static void registerFromBlock(net.minecraft.block.Block block, String modelName)
    {
        if (block == null) return;
        Item item = Item.getItemFromBlock(block);
        if (item != null)
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(YeGamolChattels.MODID + ":" + modelName, "inventory"));
    }

    private static void registerFromRegistry(String registryName, String modelName)
    {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(YeGamolChattels.MODID, registryName));
        if (item != null)
            ModelLoader.setCustomModelResourceLocation(item, 0,
                    new ModelResourceLocation(YeGamolChattels.MODID + ":" + modelName, "inventory"));
    }
}
