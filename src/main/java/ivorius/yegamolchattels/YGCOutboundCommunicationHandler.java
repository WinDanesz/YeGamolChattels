/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels;

import ivorius.yegamolchattels.blocks.YGCBlocks;
import ivorius.yegamolchattels.items.YGCItems;
import ivorius.yegamolchattels.mods.MineFactoryReloaded;
import net.minecraftforge.fml.common.Loader;

/**
 * Created by lukas on 24.09.14.
 */
public class YGCOutboundCommunicationHandler
{
    public static void init()
    {
        if (Loader.isModLoaded(MineFactoryReloaded.MOD_ID))
        {
            MineFactoryReloaded.registerPlantableCrop(YGCBlocks.flax_plant, YGCItems.flaxSeeds, null);

            MineFactoryReloaded.registerHarvestableCrop(YGCBlocks.flax_plant, 7);

            MineFactoryReloaded.registerFertilizableCrop(YGCBlocks.flax_plant, 7, null);
        }
    }
}
