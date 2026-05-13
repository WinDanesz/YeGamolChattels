/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.YGCBlockAccessor;

/**
 * Created by lukas on 11.09.14.
 */
public class YGCMaterials
{
    public static Material mixed;

    static
    {
        init();
    }

    public static void init()
    {
        if (mixed == null)
        {
            mixed = new Material(MapColor.STONE);
            YGCBlockAccessor.setImmovableMobility(mixed);
        }
    }
}
