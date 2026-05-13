package ivorius.yegamolchattels.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerItemHelper
{
    public static boolean addAsCurrentItem(EntityPlayer player, ItemStack stack)
    {
        if (stack == null)
            return false;

        int currentSlot = player.inventory.currentItem;
        ItemStack currentStack = player.inventory.getStackInSlot(currentSlot);
        if (currentStack == null)
        {
            player.inventory.setInventorySlotContents(currentSlot, stack);
            return true;
        }

        return player.inventory.addItemStackToInventory(stack);
    }
}
