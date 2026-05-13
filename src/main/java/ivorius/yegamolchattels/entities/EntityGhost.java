package ivorius.yegamolchattels.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityGhost extends EntityMob
{
    private double destX;
    private double destY;
    private double destZ;

    public EntityGhost(World world)
    {
        super(world);
        noClip = true;
        findNewDestination();
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.01D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
    }

    @Override
    public void onLivingUpdate()
    {
        noClip = true;

        if (!world.isRemote && world.isDaytime() && getBrightness() > 0.5F && world.canSeeSky(new BlockPos(posX, posY, posZ)))
            setFire(8);

        EntityLivingBase attackTarget = getAttackTarget();
        if (attackTarget == null && getDistanceSq(destX, destY, destZ) < 25.0D)
            findNewDestination();

        double targetX = attackTarget != null ? attackTarget.posX : destX;
        double targetY = attackTarget != null ? attackTarget.posY : destY;
        double targetZ = attackTarget != null ? attackTarget.posZ : destZ;
        Vec3d direction = new Vec3d(targetX - posX, targetY - posY, targetZ - posZ);
        double length = Math.sqrt(direction.x * direction.x + direction.y * direction.y + direction.z * direction.z);
        if (length > 0.001D)
        {
            double moveSpeed = 0.01D;
            motionX = (motionX + direction.x / length * moveSpeed) * 0.9D;
            motionY = (motionY + direction.y / length * moveSpeed) * 0.9D;
            motionZ = (motionZ + direction.z / length * moveSpeed) * 0.9D;
            move(net.minecraft.entity.MoverType.SELF, motionX, motionY, motionZ);
            faceForward(targetX, targetY, targetZ);
        }

        super.onLivingUpdate();
    }

    public void findNewDestination()
    {
        destX = posX + (rand.nextFloat() - 0.5F) * 50.0F;
        destY = (((posY + (rand.nextFloat() - 0.5F) * 50.0F) - 70F) * 0.9F) + 70F;
        destZ = posZ + (rand.nextFloat() - 0.5F) * 50.0F;
    }

    private void faceForward(double targetX, double targetY, double targetZ)
    {
        double distX = targetX - posX;
        double distY = targetY - posY;
        double distZ = targetZ - posZ;
        double horizontal = Math.sqrt(distX * distX + distZ * distZ);
        rotationPitch = updateRotation(rotationPitch, (float) (-(Math.atan2(distY, horizontal) * 180.0D / Math.PI)), 10.0f);
        rotationYaw = updateRotation(rotationYaw, (float) (Math.atan2(distZ, distX) * 180.0D / Math.PI) - 90.0F, 20.0f);
    }

    private float updateRotation(float current, float target, float maxChange)
    {
        float delta = MathHelper.wrapDegrees(target - current);
        if (delta > maxChange)
            delta = maxChange;
        if (delta < -maxChange)
            delta = -maxChange;
        return current + delta;
    }

    @Override
    protected Item getDropItem()
    {
        return null;
    }

    @Override
    public boolean isOnLadder()
    {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (source == DamageSource.IN_WALL)
            return false;

        return super.attackEntityFrom(source, amount);
    }

    public float getCurrentDestX()
    {
        return (float) destX;
    }

    public float getCurrentDestY()
    {
        return (float) destY;
    }

    public float getCurrentDestZ()
    {
        return (float) destZ;
    }

    public void setCurrentDestX(float destX)
    {
        this.destX = destX;
    }

    public void setCurrentDestY(float destY)
    {
        this.destY = destY;
    }

    public void setCurrentDestZ(float destZ)
    {
        this.destZ = destZ;
    }
}
