/**
 * ************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 * ************************************************************************************************
 */

package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvBlockMultiblock;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.materials.YGCMaterials;
import ivorius.yegamolchattels.tabs.YGCCreativeTabs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPedestal extends IvBlockMultiblock
{
    public BlockPedestal()
    {
        super(YGCMaterials.mixed);

        setCreativeTab(YGCCreativeTabs.tabMain);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityPedestal();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityPedestal)
        {
            if (((TileEntityPedestal) tileEntity).tryStoringItem(player.getHeldItem(hand)))
            {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemStack.EMPTY);
            }
            else
            {
                ((TileEntityPedestal) tileEntity).startDroppingItem(player);
            }
        }

        return true;
    }

    @Override
    public void parentBlockDropItemContents(World world, IvTileEntityMultiBlock tileEntity, BlockPos pos, IBlockState state)
    {
        ((TileEntityPedestal) tileEntity).dropItem();
    }

    @Override
    public void parentBlockHarvestItem(World world, IvTileEntityMultiBlock tileEntity, BlockPos pos, IBlockState state)
    {
        spawnAsEntity(world, pos, new ItemStack(this, 1, ((TileEntityPedestal) tileEntity).pedestalIdentifier));
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityPedestal)
            return Container.calcRedstoneFromInventory((TileEntityPedestal) tileEntity);

        return super.getComparatorInputOverride(state, world, pos);
    }
}
