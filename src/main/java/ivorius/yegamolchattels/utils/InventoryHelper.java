package ivorius.yegamolchattels.utils;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;

public class InventoryHelper
{
    public static int getInventorySlotContainItem(InventoryPlayer inventory, Item item)
    {
        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem() == item)
                return i;
        }

        return -1;
    }
}
