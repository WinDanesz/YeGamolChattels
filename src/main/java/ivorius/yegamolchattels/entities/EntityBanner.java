package ivorius.yegamolchattels.entities;

import ivorius.yegamolchattels.items.YGCItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityBanner extends EntityHanging
{
    private int bannerSize;
    private int bannerColor;

    public float wind;
    public float simWind;

    public EntityBanner(World world)
    {
        super(world);
        bannerSize = 1;
        bannerColor = 0;
    }

    public EntityBanner(World world, int x, int y, int z)
    {
        this(world, x, y, z, 0, 1, 0);
    }

    public EntityBanner(World world, int x, int y, int z, int direction, int bannerSize, int bannerColor)
    {
        super(world, new BlockPos(x, y, z));
        this.bannerSize = bannerSize;
        this.bannerColor = bannerColor;
        updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(direction & 3));
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        wind = EntityFlag.updateWind(wind, world);
        simWind = EntityFlag.updateSimWind(wind, simWind);
    }

    @Override
    public int getWidthPixels()
    {
        return bannerSize == 2 ? 48 : 16;
    }

    @Override
    public int getHeightPixels()
    {
        return bannerSize == 2 ? 64 : 16;
    }

    @Override
    public void onBroken(Entity brokenEntity)
    {
        ItemStack drop = new ItemStack(bannerSize == 2 ? YGCItems.bannerLarge : YGCItems.bannerSmall, 1, bannerColor);
        world.spawnEntity(new EntityItem(world, posX, posY, posZ, drop));
    }

    @Override
    public void playPlaceSound()
    {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setInteger("BannerColor", bannerColor);
        tag.setInteger("BannerSize", bannerSize);
        super.writeEntityToNBT(tag);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        bannerColor = tag.getInteger("BannerColor");
        bannerSize = tag.getInteger("BannerSize");
        super.readEntityFromNBT(tag);
    }

    public int getSize()
    {
        return bannerSize;
    }

    public void setSize(int size)
    {
        bannerSize = size;
    }

    public int getColor()
    {
        return bannerColor;
    }

    public void setColor(int color)
    {
        bannerColor = color;
    }
}
