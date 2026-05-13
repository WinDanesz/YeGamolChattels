/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockSidedWall extends Block
{
    public String[] wallIconPaths;

    public BlockSidedWall(Material par2Material, String[] wallIcons)
    {
        super(par2Material);

        this.wallIconPaths = wallIcons;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        for (int i = 0; i < wallIconPaths.length; i++)
            items.add(new ItemStack(this, 1, i));
    }
}
