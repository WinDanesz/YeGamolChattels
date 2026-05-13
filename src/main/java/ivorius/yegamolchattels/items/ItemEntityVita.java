/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by lukas on 27.07.14.
 */
public class ItemEntityVita extends Item
{
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab))
            return;

        for (ResourceLocation mobID : EntityList.getEntityNameList())
        {
            Class<? extends Entity> entityClass = EntityList.getClass(mobID);
            if (entityClass != null && canClassBeValidVita(entityClass))
                items.add(createVitaItemStack(this, mobID.toString()));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
        String base = getTranslationKey(par1ItemStack) + ".base";

        String entityName = getEntityID(par1ItemStack);
        String localizedEntityName = entityName != null && entityName.length() > 0
                ? net.minecraft.util.text.translation.I18n.translateToLocal("entity." + entityName + ".name")
                : net.minecraft.util.text.translation.I18n.translateToLocal("tile.ygcStatue.unknown");

        return net.minecraft.util.text.translation.I18n.translateToLocalFormatted(base, localizedEntityName);
    }

    public int getColorFromItemstack(ItemStack par1ItemStack, int par2)
    {
        return getEntityID(par1ItemStack).hashCode() | 0xff000000;
    }

    public static NBTTagCompound getEntityTag(ItemStack stack)
    {
        return stack.hasTagCompound() ? stack.getTagCompound().getCompoundTag("vitaEntity") : new NBTTagCompound();
    }

    public static String getEntityID(ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("vitaEntity"))
            return getEntityTag(stack).getString("id");

        return stack.hasTagCompound() ? stack.getTagCompound().getString("vitaEntityID") : "";
    }

    public static Entity createEntity(ItemStack stack, World world)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("vitaEntity"))
            return EntityList.createEntityFromNBT(getEntityTag(stack), world);
        else
            return EntityList.createEntityByIDFromName(new ResourceLocation(getEntityID(stack)), world);
    }

    public static void setEntityByID(ItemStack stack, String entityName)
    {
        stack.setTagInfo("vitaEntityID", new NBTTagString(entityName));
    }

    public static void setEntity(ItemStack stack, Entity entity)
    {
        NBTTagCompound compound = new NBTTagCompound();
        entity.writeToNBTOptional(compound);
        stack.setTagInfo("vitaEntity", compound);
    }

    public static ItemStack createVitaItemStack(Item item, String entityName)
    {
        ItemStack stack = new ItemStack(item);
        setEntityByID(stack, entityName);
        return stack;
    }

    public static ItemStack createVitaItemStackAsNewbornEntity(Item item, Entity entity)
    {
        ItemStack stack = new ItemStack(item);
        setEntity(stack, entity);

        Entity savedEntity = createEntity(stack, entity.world);

        if (savedEntity != null)
        {
            savedEntity.isDead = false;

            if (savedEntity instanceof EntityLivingBase)
            {
                EntityLivingBase entityLiving = (EntityLivingBase) savedEntity;
                entityLiving.deathTime = 0;
                entityLiving.hurtTime = 0;
                entityLiving.extinguish();
                entityLiving.setHealth(entityLiving.getMaxHealth());
            }

            setEntity(stack, savedEntity);

            return stack;
        }

        return null;
    }

    public static boolean canClassBeValidVita(Class<? extends Entity> entityClass)
    {
        return EntityLiving.class.isAssignableFrom(entityClass);
    }
}
