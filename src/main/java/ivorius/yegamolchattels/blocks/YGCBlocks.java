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
    public static final Block tikiTorch = placeholder();
    public static final Block statue = placeholder();
    public static final Block treasurePile = placeholder();
    public static final Block grandfatherClock = placeholder();
    public static final Block weaponRack = placeholder();
    public static final Block grindstone = placeholder();
    public static final Block gong = placeholder();
    public static final Block pedestal = placeholder();
    public static final Block itemShelf = placeholder();
    public static final Block snowGlobe = placeholder();
    public static final Block sawBench = placeholder();
    public static final Block tablePress = placeholder();
    public static final Block flaxPlant = placeholder();
    public static final Block microBlock = placeholder();
    public static final Block lootChest = placeholder();

    public static int blockTreasurePileRenderType;
    public static int blockTikiTorchRenderType;
    public static int blockMicroBlockRenderType;

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

        registerBlock(registry, "tikiTorch", "tikiTorch", new BlockTikiTorch().setHardness(0.0F).setLightLevel(0.9375F), true);
        registerBlock(registry, "statue", "ygcStatue", new BlockStatue().setHardness(2.0F), true);
        registerBlock(registry, "treasurePile", "treasurePile", new BlockTreasurePile().setHardness(0.2F), true);
        registerBlock(registry, "grandfatherClock", "grandfatherClock", new BlockGrandfatherClock(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "weaponRack", "weaponRack", new BlockWeaponRack(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "grindstone", "grindstone", new BlockGrindstone(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "gong", "gong", new BlockGong(Material.IRON).setHardness(1.5F), true);
        registerBlock(registry, "pedestal", "pedestal", new BlockPedestal().setHardness(1.5F), true);
        registerBlock(registry, "ygcItemShelf", "ygcItemShelf", new BlockItemShelf(Material.WOOD).setHardness(1.5F), true);
        registerBlock(registry, "ygcSnowGlobe", "ygcSnowGlobe", new BlockSnowGlobe(), true);
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
