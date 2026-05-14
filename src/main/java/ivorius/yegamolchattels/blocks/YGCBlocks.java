package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.materials.YGCMaterials;
import ivorius.yegamolchattels.tabs.YGCCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

@ObjectHolder(YeGamolChattels.MODID)
@Mod.EventBusSubscriber(modid = YeGamolChattels.MODID)
public final class YGCBlocks
{
    public static final Block tiki_torch = placeholder();
    public static final Block statue = placeholder();
    public static final Block treasure_pile = placeholder();
    public static final Block grandfather_clock = placeholder();
    public static final Block weapon_rack = placeholder();
    public static final Block grindstone = placeholder();
    public static final Block gong_small = placeholder();
    public static final Block gong_medium = placeholder();
    public static final Block gong_large = placeholder();
    public static final Block pedestal = placeholder();
    public static final Block item_shelf = placeholder();
    public static final Block snow_globe = placeholder();
    public static final Block plank_saw = placeholder();
    public static final Block table_press = placeholder();
    public static final Block flax_plant = placeholder();
    public static final Block micro_block = placeholder();
    public static final Block loot_chest = placeholder();

    public static int block_treasure_pile_render_type;
    public static int block_tiki_torch_render_type;
    public static int block_micro_block_render_type;

    private YGCBlocks()
    {
    }

    @Nonnull
    @SuppressWarnings("ConstantConditions")
    private static <T> T placeholder()
    {
        return null;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        registerBlock(registry, "tiki_torch", "tikiTorch", new BlockTikiTorch().setHardness(0.0F).setLightLevel(0.9375F), true);
        registerBlock(registry, "statue", "ygcStatue", new BlockStatue().setHardness(2.0F), true);
        registerBlock(registry, "treasure_pile", "treasurePile", new BlockTreasurePile().setHardness(0.2F), true);
        registerBlock(registry, "grandfather_clock", "grandfatherClock", new BlockGrandfatherClock(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "weapon_rack", "weaponRack", new BlockWeaponRack(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "grindstone", "grindstone", new BlockGrindstone(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "gong_small",  "gong.size0", new BlockGong(Material.IRON, 0).setHardness(1.5F), true);
        registerBlock(registry, "gong_medium", "gong.size1", new BlockGong(Material.IRON, 1).setHardness(1.5F), true);
        registerBlock(registry, "gong_large",  "gong.size2", new BlockGong(Material.IRON, 2).setHardness(1.5F), true);
        registerBlock(registry, "pedestal", "pedestal", new BlockPedestal().setHardness(1.5F), true);
        registerBlock(registry, "item_shelf", "ygcItemShelf", new BlockItemShelf(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "snow_globe", "ygcSnowGlobe", new BlockSnowGlobe(), true);
        registerBlock(registry, "plank_saw", "ygcSawBench", new BlockSawBench().setHardness(1.5F), true);
        registerBlock(registry, "table_press", "tablePress", new BlockTablePress().setHardness(1.5F), true);
        registerBlock(registry, "flax_plant", "ygcFlaxPlant", new BlockFlaxPlant(), false);
        registerBlock(registry, "micro_block", "ygcMicroBlock", new BlockMicroBlock().setHardness(1.0F), false);
        registerBlock(registry, "loot_chest", "ygcLootChest", new BlockLootChest().setHardness(1.5F), true);
    }

    private static <T extends Block> T registerBlock(IForgeRegistry<Block> registry, String name, String translationKey, T block, boolean creativeTab)
    {
        block.setRegistryName(YeGamolChattels.MODID, name);
        block.setTranslationKey(translationKey);
        if (creativeTab)
            block.setCreativeTab(YGCCreativeTabs.tabMain);
        registry.register(block);
        return block;
    }

    public static void registerTileEntities()
    {
        GameRegistry.registerTileEntity(TileEntityStatue.class, new ResourceLocation(YeGamolChattels.MODID, "ygcStatue"));
        GameRegistry.registerTileEntity(TileEntityGrandfatherClock.class, new ResourceLocation(YeGamolChattels.MODID, "ygcGrandfatherClock"));
        GameRegistry.registerTileEntity(TileEntityWeaponRack.class, new ResourceLocation(YeGamolChattels.MODID, "ygcWeaponRack"));
        GameRegistry.registerTileEntity(TileEntityGrindstone.class, new ResourceLocation(YeGamolChattels.MODID, "ygcGrindstone"));
        GameRegistry.registerTileEntity(TileEntityGong.class, new ResourceLocation(YeGamolChattels.MODID, "ygcGong"));
        GameRegistry.registerTileEntity(TileEntityPedestal.class, new ResourceLocation(YeGamolChattels.MODID, "ygcPedestal"));
        GameRegistry.registerTileEntity(TileEntityItemShelfModel0.class, new ResourceLocation(YeGamolChattels.MODID, "ygcItemShelf"));
        GameRegistry.registerTileEntity(TileEntitySnowGlobe.class, new ResourceLocation(YeGamolChattels.MODID, "ygcSnowGlobe"));
        GameRegistry.registerTileEntity(TileEntitySawBench.class, new ResourceLocation(YeGamolChattels.MODID, "ygcPlankSaw"));
        GameRegistry.registerTileEntity(TileEntityTablePress.class, new ResourceLocation(YeGamolChattels.MODID, "ygcTablePress"));
        GameRegistry.registerTileEntity(TileEntityMicroBlock.class, new ResourceLocation(YeGamolChattels.MODID, "ygcMicroBlock"));
        GameRegistry.registerTileEntity(TileEntityLootChest.class, new ResourceLocation(YeGamolChattels.MODID, "ygcLootChest"));
    }
}
