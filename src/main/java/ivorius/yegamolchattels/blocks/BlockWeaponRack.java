/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWeaponRack extends Block
{
    public BlockWeaponRack(Material material)
    {
        super(material);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityWeaponRack)
        {
            TileEntityWeaponRack tileEntityWeaponRack = (TileEntityWeaponRack) tileEntity;

            ItemStack heldItem = player.getHeldItem(hand);
            if (tileEntityWeaponRack.tryApplyingEffect(heldItem, player))
            {
                return true;
            }
            else if (!heldItem.isEmpty() && tileEntityWeaponRack.tryStoringItem(heldItem, player))
            {
                return true;
            }
            else
            {
                return tileEntityWeaponRack.pickUpItem(player);
            }
        }

        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof TileEntityWeaponRack)
                ((TileEntityWeaponRack) tileEntity).dropAllWeapons();
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess blockAccess, BlockPos pos)
    {
        TileEntity tileEntity = blockAccess.getTileEntity(pos);

        if (tileEntity instanceof TileEntityWeaponRack)
        {
            TileEntityWeaponRack tileEntityRack = (TileEntityWeaponRack) tileEntity;
            return rackBounds(tileEntityRack.direction, tileEntityRack.getWeaponRackType());
        }

        return FULL_BLOCK_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return getBoundingBox(state, world, pos).offset(pos);
    }

    private AxisAlignedBB rackBounds(int direction, int type)
    {
        if (type == TileEntityWeaponRack.weaponRackTypeWall)
        {
            float width = 0.28F;

            if (direction == 0)
                return new AxisAlignedBB(0.0F, 0.0F, 1.0F - width, 1.0F, 1.0F, 1.0F);

            if (direction == 1)
                return new AxisAlignedBB(0.0F, 0.0F, 0.0F, width, 1.0F, 1.0F);

            if (direction == 2)
                return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, width);

            if (direction == 3)
                return new AxisAlignedBB(1.0F - width, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            float width = 0.8F;

            if (direction == 0 || direction == 2)
                return new AxisAlignedBB(0.0F, 0.0F, (1.0f - width) * 0.5f, 1.0F, 1.0F, (1.0f + width) * 0.5f);

            return new AxisAlignedBB((1.0f - width) * 0.5f, 0.0F, 0.0f, (1.0f + width) * 0.5f, 1.0F, 1.0f);
        }

        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityWeaponRack();
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

        if (tileEntity instanceof TileEntityWeaponRack)
            return Container.calcRedstoneFromInventory((TileEntityWeaponRack) tileEntity);

        return super.getComparatorInputOverride(state, world, pos);
    }
}
