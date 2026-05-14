package ivorius.yegamolchattels.entities;

import ivorius.yegamolchattels.items.YGCItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.material.Material;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityBanner extends EntityHanging
{
    private static final DataParameter<Integer> BANNER_SIZE = EntityDataManager.createKey(EntityBanner.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> BANNER_COLOR = EntityDataManager.createKey(EntityBanner.class, DataSerializers.VARINT);
    private int cachedBannerSize = 1;
    private int cachedBannerColor;

    public float wind;
    public float simWind;

    public EntityBanner(World world)
    {
        super(world);
        setSize(1);
        setColor(0);
    }

    public EntityBanner(World world, int x, int y, int z)
    {
        this(world, x, y, z, 0, 1, 0);
    }

    public EntityBanner(World world, int x, int y, int z, int direction, int bannerSize, int bannerColor)
    {
        super(world, new BlockPos(x, y, z));
        setSize(bannerSize);
        setColor(bannerColor);
        updateFacingWithBoundingBox(EnumFacing.byHorizontalIndex(direction & 3));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(BANNER_SIZE, cachedBannerSize);
        dataManager.register(BANNER_COLOR, cachedBannerColor);
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
        return getSize() == 2 ? 48 : 16;
    }

    @Override
    public int getHeightPixels()
    {
        return getSize() == 2 ? 64 : 16;
    }

    @Override
    public void onBroken(Entity brokenEntity)
    {
        ItemStack drop = new ItemStack(getSize() == 2 ? YGCItems.bannerLarge : YGCItems.bannerSmall, 1, getColor());
        world.spawnEntity(new EntityItem(world, posX, posY, posZ, drop));
    }

    @Override
    public void playPlaceSound()
    {
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setInteger("BannerColor", getColor());
        tag.setInteger("BannerSize", getSize());
        super.writeEntityToNBT(tag);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        setColor(tag.getInteger("BannerColor"));
        setSize(tag.getInteger("BannerSize"));
        super.readEntityFromNBT(tag);
    }

    @Override
    public boolean onValidSurface()
    {
        if (!world.getCollisionBoxes(this, getEntityBoundingBox()).isEmpty())
            return false;

        int widthBlocks = Math.max(1, getWidthPixels() / 16);
        int heightBlocks = Math.max(1, getHeightPixels() / 16);
        int offsetX = facingDirection == EnumFacing.NORTH || facingDirection == EnumFacing.SOUTH
                ? MathHelper.floor(posX - getWidthPixels() / 32.0F)
                : hangingPosition.getX();
        int offsetY = MathHelper.floor(posY - getHeightPixels() / 32.0F);
        int offsetZ = facingDirection == EnumFacing.EAST || facingDirection == EnumFacing.WEST
                ? MathHelper.floor(posZ - getWidthPixels() / 32.0F)
                : hangingPosition.getZ();

        for (int x = 0; x < widthBlocks; x++)
        {
            for (int y = heightBlocks - 1; y < heightBlocks; y++)
            {
                Material material = (facingDirection == EnumFacing.NORTH || facingDirection == EnumFacing.SOUTH)
                        ? world.getBlockState(new BlockPos(offsetX + x, offsetY + y, hangingPosition.getZ())).getMaterial()
                        : world.getBlockState(new BlockPos(hangingPosition.getX(), offsetY + y, offsetZ + x)).getMaterial();

                if (!material.isSolid())
                    return false;
            }
        }

        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
        for (Entity entity : entities)
        {
            if (entity instanceof EntityHanging)
                return false;
        }

        return true;
    }

    public int getSize()
    {
        return dataManager != null ? dataManager.get(BANNER_SIZE) : cachedBannerSize;
    }

    public void setSize(int size)
    {
        cachedBannerSize = size;
        if (dataManager != null)
            dataManager.set(BANNER_SIZE, size);
    }

    public int getColor()
    {
        return dataManager != null ? dataManager.get(BANNER_COLOR) : cachedBannerColor;
    }

    public void setColor(int color)
    {
        cachedBannerColor = color;
        if (dataManager != null)
            dataManager.set(BANNER_COLOR, color);
    }
}
