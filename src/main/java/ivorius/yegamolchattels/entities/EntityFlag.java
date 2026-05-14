package ivorius.yegamolchattels.entities;

import ivorius.yegamolchattels.items.YGCItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityFlag extends Entity
{
    private static final DataParameter<Integer> FLAG_SIZE = EntityDataManager.createKey(EntityFlag.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> FLAG_COLOR = EntityDataManager.createKey(EntityFlag.class, DataSerializers.VARINT);

    public float wind = 0.0f;
    public float simWind = 0.0f;

    public EntityFlag(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
        ignoreFrustumCheck = true;
    }

    @Override
    protected void entityInit()
    {
        dataManager.register(FLAG_SIZE, 0);
        dataManager.register(FLAG_COLOR, 0);
    }

    public void updateBounds()
    {
        float height = getFlagHeight() / 16F;
        setEntityBoundingBox(new AxisAlignedBB(posX + 0.4, posY, posZ + 0.4, posX + 0.6, posY + height, posZ + 0.6));
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        super.setPosition(x, y, z);
        updateBounds();
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!world.isRemote && (ticksExisted + 90) % 100 == 0 && !canStayAtPosition())
        {
            setDead();
            dropFlag();
        }

        wind = updateWind(wind, world);
        simWind = updateSimWind(wind, simWind);
        updateBounds();
    }

    public static float updateSimWind(float wind, float simWind)
    {
        return simWind + (wind - simWind) * 0.02f;
    }

    public static float getInterpolatedWind(float wind, float simWind, float t)
    {
        return simWind + (wind - simWind) * 0.02f * t;
    }

    public static float updateWind(float wind, World world)
    {
        float globalWind = getGlobalWind(world.getWorldTime());
        wind += (world.rand.nextFloat() * globalWind - world.rand.nextFloat() * (1.0f - globalWind)) * 0.2f;
        return MathHelper.clamp(wind, 0.0f, 1.0f);
    }

    public static float getGlobalWind(long time)
    {
        return (MathHelper.sin(time * 0.001f) + 1.0f) * 0.5f;
    }

    public boolean canStayAtPosition()
    {
        BlockPos basePos = new BlockPos(posX, posY - 0.01, posZ);
        IBlockState state = world.getBlockState(basePos);
        Block block = state.getBlock();
        boolean supported = state.getMaterial().isSolid() || block == Blocks.OAK_FENCE || block == Blocks.NETHER_BRICK_FENCE || block == Blocks.COBBLESTONE_WALL;
        if (!supported)
            return false;

        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
        for (Entity entity : entities)
        {
            if (entity instanceof EntityFlag)
                return false;
        }

        return true;
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!isDead && !world.isRemote)
        {
            setDead();
            dropFlag();
        }

        return true;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        setPosition(tag.getInteger("TileX"), tag.getInteger("TileY"), tag.getInteger("TileZ"));
        setSize(tag.getInteger("FlagSize"));
        setColor(tag.getInteger("FlagColor"));
        updateBounds();
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setInteger("TileX", MathHelper.floor(posX));
        tag.setInteger("TileY", MathHelper.floor(posY));
        tag.setInteger("TileZ", MathHelper.floor(posZ));
        tag.setInteger("FlagSize", getSize());
        tag.setInteger("FlagColor", getColor());
    }

    @Override
    public void move(net.minecraft.entity.MoverType type, double x, double y, double z)
    {
        if (!world.isRemote && x * x + y * y + z * z > 0.0D)
        {
            setDead();
            dropFlag();
        }
        else
        {
            super.move(type, x, y, z);
        }
    }

    @Override
    public void addVelocity(double x, double y, double z)
    {
        if (!world.isRemote && x * x + y * y + z * z > 0.0D)
        {
            setDead();
            dropFlag();
        }
    }

    public int getFlagHeight()
    {
        if (getSize() == 0)
            return 32;
        if (getSize() == 2)
            return 128;

        return 96;
    }

    public void dropFlag()
    {
        ItemStack drop = new ItemStack(getSize() == 2 ? YGCItems.flagLarge : YGCItems.flagSmall, 1, getColor());
        world.spawnEntity(new EntityItem(world, posX, posY, posZ, drop));
    }

    public int getSize()
    {
        return dataManager.get(FLAG_SIZE);
    }

    public void setSize(int size)
    {
        dataManager.set(FLAG_SIZE, size);
        updateBounds();
    }

    public int getColor()
    {
        return dataManager.get(FLAG_COLOR);
    }

    public void setColor(int color)
    {
        dataManager.set(FLAG_COLOR, color);
    }
}
