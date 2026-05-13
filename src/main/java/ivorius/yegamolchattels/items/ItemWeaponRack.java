/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.multiblock.IvMultiBlockHelper;
import ivorius.yegamolchattels.blocks.TileEntityWeaponRack;
import ivorius.yegamolchattels.blocks.YGCBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWeaponRack extends ItemBlock
{
    private final Block placedBlock;

    public ItemWeaponRack(Block block)
    {
        super(block);
        this.placedBlock = block;
        maxStackSize = 16;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing == EnumFacing.DOWN)
            return EnumActionResult.FAIL;

        BlockPos placePos = pos.offset(facing);
        boolean onWall = facing != EnumFacing.UP;
        if (!placedBlock.canPlaceBlockAt(world, placePos))
            return EnumActionResult.FAIL;

        int direction = 0;
        if (!onWall)
            direction = IvMultiBlockHelper.getRotation(player);
        else
        {
            if (facing == EnumFacing.NORTH)
                direction = 0;
            if (facing == EnumFacing.SOUTH)
                direction = 2;
            if (facing == EnumFacing.WEST)
                direction = 3;
            if (facing == EnumFacing.EAST)
                direction = 1;
        }

        world.setBlockState(placePos, placedBlock.getStateFromMeta(onWall ? 1 : 0), 3);

        TileEntity tileEntity = world.getTileEntity(placePos);
        if (tileEntity instanceof TileEntityWeaponRack)
        {
            ((TileEntityWeaponRack) tileEntity).direction = direction;
        }

        player.getHeldItem(hand).shrink(1);

        return EnumActionResult.SUCCESS;
    }
}
