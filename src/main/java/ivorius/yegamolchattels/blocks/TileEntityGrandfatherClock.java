/***************************************************************************************************
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 **************************************************************************************************/

package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvTileEntityHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityGrandfatherClock extends TileEntity implements ITickable
{
    public int ticksAlive;

    public long clockTimeShown;
    public long clockTimeMotion;
    public long pendulumTimeShown;
    public long pendulumTimeMotion;

    public int delayUntilSound = 0;

    @Override
    public void update()
    {
        if (world == null || pos == null)
            return;

        if (world.provider.isSurfaceWorld())
        {
            clockTimeShown = world.getWorldTime();
            pendulumTimeShown = ticksAlive * 20;
        }
        else
        {
            clockTimeMotion += world.rand.nextLong() % 301L;
            if (clockTimeMotion > 1500L)
                clockTimeMotion = 1500L;
            if (clockTimeMotion < -1500L)
                clockTimeMotion = -1500L;
            clockTimeShown += clockTimeMotion;

            pendulumTimeMotion += world.rand.nextLong() % 21L;
            if (pendulumTimeMotion > 100L)
                pendulumTimeMotion = 100L;
            if (pendulumTimeMotion < -100L)
                pendulumTimeMotion = -100L;
            pendulumTimeShown += pendulumTimeMotion;
        }

        if (!world.isRemote && (getBlockMetadata() & 1) == 0)
        {
            if (delayUntilSound <= 0)
            {
                if (world.getWorldTime() % (24000 / 6) == 0)
                {
                    playSound(SoundEvents.BLOCK_NOTE_HARP, 0.2f, 0.01f);
                    playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 0.01f);
                    playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.3f, 0.1f);
                    delayUntilSound = 10;
                }
                else if (ticksAlive % 40 == 0)
                {
                    playSound(SoundEvents.UI_BUTTON_CLICK, 0.1f, 0.5f);
                    delayUntilSound = 10;
                }
                else if (ticksAlive % 40 == 20)
                {
                    playSound(SoundEvents.UI_BUTTON_CLICK, 0.1f, 0.6f);
                    delayUntilSound = 10;
                }
            }
            else
                delayUntilSound--;
        }

        ticksAlive++;
    }

    private void playSound(net.minecraft.util.SoundEvent soundEvent, float volume, float pitch)
    {
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f, soundEvent, SoundCategory.BLOCKS, volume, pitch);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.writeToNBT(par1nbtTagCompound);

        par1nbtTagCompound.setInteger("ticksAlive", ticksAlive);
        return par1nbtTagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.readFromNBT(par1nbtTagCompound);

        ticksAlive = par1nbtTagCompound.getInteger("ticksAlive");
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return (SPacketUpdateTileEntity) IvTileEntityHelper.getStandardDescriptionPacket(this);
    }
}
