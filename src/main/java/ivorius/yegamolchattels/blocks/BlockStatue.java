/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvBlockMultiblock;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.achievements.YGCAchievementList;
import ivorius.yegamolchattels.items.ItemStatue;
import ivorius.yegamolchattels.materials.YGCMaterials;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockStatue extends IvBlockMultiblock
{
    public BlockStatue()
    {
        super(YGCMaterials.mixed);
    }

    @Override
    public void parentBlockHarvestItem(World world, IvTileEntityMultiBlock tileEntity, BlockPos pos, IBlockState state)
    {
        if (tileEntity instanceof TileEntityStatue)
        {
            Statue statue = ((TileEntityStatue) tileEntity).getStatue();

            if (statue != null)
            {
                ItemStack stack = new ItemStack(Item.getItemFromBlock(YGCBlocks.statue));
                ItemStatue.setStatue(stack, statue);

                spawnAsEntity(world, pos, stack);
            }
        }
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, net.minecraft.block.Block block, BlockPos fromPos)
    {
        super.neighborChanged(state, world, pos, block, fromPos);

        if (world.isBlockPowered(pos))
        {
            IvTileEntityMultiBlock parent = getValidatedTotalParent(this, world, pos);

            if (parent instanceof TileEntityStatue)
            {
                if (((TileEntityStatue) parent).letStatueComeAlive())
                    world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IvTileEntityMultiBlock parent = getValidatedTotalParent(this, world, pos);

        if (parent instanceof TileEntityStatue)
        {
            TileEntityStatue tileEntityStatue = (TileEntityStatue) parent;
            if (tileEntityStatue.getStatue() != null && tileEntityStatue.tryEquipping(player.getHeldItem(hand)))
            {
                Statue statue = tileEntityStatue.getStatue();
                Entity entity = statue.getEntity();
                if (entity instanceof EntityLivingBase && statue.getMaterial().getBlock() == Blocks.GOLD_BLOCK)
                {
                    EntityLivingBase living = (EntityLivingBase) entity;
                    if (isDiamond(living.getItemStackFromSlot(EntityEquipmentSlot.FEET))
                            && isDiamond(living.getItemStackFromSlot(EntityEquipmentSlot.LEGS))
                            && isDiamond(living.getItemStackFromSlot(EntityEquipmentSlot.CHEST))
                            && isDiamond(living.getItemStackFromSlot(EntityEquipmentSlot.HEAD)))
                    {
                        YGCAchievementList.trigger(player, YGCAchievementList.superExpensiveStatue);
                    }
                }

                return true;
            }
            else
            {
                tileEntityStatue.addEquipmentToInventory(player);
//                tileEntityStatue.dropEquipment();

                return true;
            }
        }

        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    private boolean isDiamond(ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof ItemArmor && ((ItemArmor) stack.getItem()).getArmorMaterial() == ItemArmor.ArmorMaterial.DIAMOND;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityStatue();
    }
}
