package ivorius.ivtoolkit.tools;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface MCRegistry
{
    Item itemFromID(ResourceLocation itemID);

    ResourceLocation idFromItem(Item item);

    void modifyItemStackCompound(NBTTagCompound compound, ResourceLocation itemID);

    Block blockFromID(ResourceLocation blockID);

    ResourceLocation idFromBlock(Block block);

    TileEntity loadTileEntity(World world, NBTTagCompound compound);
}
