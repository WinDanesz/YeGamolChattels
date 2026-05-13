/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvMultiBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLootChest extends Block
{
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.8, 1.0);

    public BlockLootChest()
    {
        super(Material.WOOD);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, net.minecraft.world.IBlockAccess source, BlockPos pos)
    {
        return BOUNDS;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityLootChest)
            ((TileEntityLootChest) tileEntity).dropAllItems();

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof TileEntityLootChest))
            return false;

        TileEntityLootChest lootChest = (TileEntityLootChest) tileEntity;

        if (!world.isRemote)
        {
            if (!lootChest.opened)
            {
                lootChest.open();
            }
            else if (lootChest.itemAccessible())
            {
                if (lootChest.firstItem() == null)
                {
                    ItemStack currentItem = player.getHeldItem(hand);
                    if (!currentItem.isEmpty())
                    {
                        lootChest.addLoot(currentItem.copy());
                        currentItem.setCount(0);
                    }
                    lootChest.close();
                }
                else
                {
                    lootChest.pickUpItem(player);
                }
            }
        }

        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLivingBase, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, pos, state, entityLivingBase, itemStack);

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityLootChest)
            ((TileEntityLootChest) tileEntity).direction = IvMultiBlockHelper.getRotation(entityLivingBase);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityLootChest();
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
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityLootChest)
            return Container.calcRedstoneFromInventory((TileEntityLootChest) tileEntity);

        return super.getComparatorInputOverride(state, world, pos);
    }
}
