package ivorius.yegamolchattels;

import ivorius.yegamolchattels.blocks.YGCBlocks;
import ivorius.yegamolchattels.entities.EntityBanner;
import ivorius.yegamolchattels.entities.EntityFakePlayer;
import ivorius.yegamolchattels.entities.EntityFlag;
import ivorius.yegamolchattels.entities.EntityGhost;
import ivorius.yegamolchattels.entities.YGCEntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class YGCRegistryHandler
{
    public static void init()
    {
        YGCBlocks.blockTreasurePileRenderType = -1;
        YGCBlocks.blockTikiTorchRenderType = -1;
        YGCBlocks.blockMicroBlockRenderType = -1;

        YGCBlocks.registerTileEntities();
        registerEntities();
    }

    private static void registerEntities()
    {
        EntityRegistry.registerModEntity(new ResourceLocation(YeGamolChattels.MODID, "ygcBanner"), EntityBanner.class, "ygcBanner", YGCEntityList.bannerID, YeGamolChattels.instance, 160, Integer.MAX_VALUE, false);
        EntityRegistry.registerModEntity(new ResourceLocation(YeGamolChattels.MODID, "ygcFlag"), EntityFlag.class, "ygcFlag", YGCEntityList.flagID, YeGamolChattels.instance, 160, Integer.MAX_VALUE, false);
        EntityRegistry.registerModEntity(new ResourceLocation(YeGamolChattels.MODID, "ygcGhost"), EntityGhost.class, "ygcGhost", YGCEntityList.ghostID, YeGamolChattels.instance, 80, 3, false, 0x888888, 0x554444);
        EntityRegistry.registerModEntity(new ResourceLocation(YeGamolChattels.MODID, "fakePlayer"), EntityFakePlayer.class, "fakePlayer", YGCEntityList.fakePlayerID, YeGamolChattels.instance, 80, 3, false);

        for (Biome biome : Biome.REGISTRY)
        {
            if (biome != null && "minecraft:mushroom_island".equals(String.valueOf(Biome.REGISTRY.getNameForObject(biome))))
            {
                biome.getSpawnableList(net.minecraft.entity.EnumCreatureType.CREATURE).add(new Biome.SpawnListEntry(EntityGhost.class, 1, 1, 4));
                break;
            }
        }
    }
}
