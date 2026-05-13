package ivorius.ivtoolkit.tools;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class MCRegistryDefault implements MCRegistry
{
    public static final MCRegistryDefault INSTANCE = new MCRegistryDefault();

    @Override
    public Item itemFromID(ResourceLocation itemID)
    {
        return Item.REGISTRY.getObject(itemID);
    }

    @Override
    public ResourceLocation idFromItem(Item item)
    {
        return Item.REGISTRY.getNameForObject(item);
    }

    @Override
    public void modifyItemStackCompound(NBTTagCompound compound, ResourceLocation itemID)
    {
    }

    @Override
    public Block blockFromID(ResourceLocation blockID)
    {
        return Block.REGISTRY.getObject(blockID);
    }

    @Override
    public ResourceLocation idFromBlock(Block block)
    {
        return Block.REGISTRY.getNameForObject(block);
    }

    @Override
    public TileEntity loadTileEntity(World world, NBTTagCompound compound)
    {
        return TileEntity.create(world, compound);
    }
}
