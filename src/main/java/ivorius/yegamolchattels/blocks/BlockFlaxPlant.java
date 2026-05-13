/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.items.YGCItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

/**
 * Created by lukas on 10.07.14.
 */
public class BlockFlaxPlant extends BlockCrops
{
    @Override
    protected Item getSeed()
    {
        return YGCItems.flaxSeeds;
    }

    @Override
    protected Item getCrop()
    {
        return YGCItems.flaxFiber;
    }

    @Override
    protected boolean canSustainBush(IBlockState state)
    {
        net.minecraft.block.Block block = state.getBlock();
        return block == Blocks.FARMLAND || block == Blocks.GRASS || block == Blocks.DIRT;
    }
}
