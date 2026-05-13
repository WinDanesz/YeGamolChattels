package ivorius.yegamolchattels.entities;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class EntityFakePlayer extends EntityMob
{
    private GameProfile playerProfile;

    public EntityFakePlayer(World world)
    {
        super(world);
        setSize(0.6F, 1.8F);
    }

    public EntityFakePlayer(World world, GameProfile playerProfile)
    {
        this(world);
        this.playerProfile = playerProfile;
    }

    public String getPlayerUsername()
    {
        return playerProfile != null ? playerProfile.getName() : null;
    }

    public GameProfile getPlayerProfile()
    {
        return playerProfile;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag)
    {
        super.writeEntityToNBT(tag);

        if (playerProfile != null)
        {
            if (playerProfile.getId() != null)
                tag.setUniqueId("PlayerUUID", playerProfile.getId());
            tag.setString("PlayerName", playerProfile.getName());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag)
    {
        super.readEntityFromNBT(tag);

        if (tag.hasKey("PlayerName"))
        {
            UUID uuid = tag.hasUniqueId("PlayerUUID") ? tag.getUniqueId("PlayerUUID") : null;
            playerProfile = new GameProfile(uuid, tag.getString("PlayerName"));
        }
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    public static GameProfile createGameProfile(String playerUsername)
    {
        return playerUsername == null || playerUsername.isEmpty() ? null : new GameProfile(null, playerUsername);
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getLocationSkin()
    {
        String username = getPlayerUsername();
        return username != null ? AbstractClientPlayer.getLocationSkin(username) : AbstractClientPlayer.getLocationSkin("Steve");
    }
}
