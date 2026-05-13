/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.mods;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Created by lukas on 02.11.14.
 */
public class ModRepresentation
{
    public static Block block(String modID, String id)
    {
        return Block.REGISTRY.getObject(new ResourceLocation(modID, id));
    }

    public static String id(Block block)
    {
        return String.valueOf(Block.REGISTRY.getNameForObject(block));
    }

    public static Item item(String modID, String id)
    {
        return Item.REGISTRY.getObject(new ResourceLocation(modID, id));
    }

    public static String id(Item item)
    {
        return String.valueOf(Item.REGISTRY.getNameForObject(item));
    }
}
