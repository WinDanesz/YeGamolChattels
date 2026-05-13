package ivorius.yegamolchattels.utils;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class YGCSoundHelper
{
    public static SoundEvent resolve(String soundName)
    {
        switch (soundName)
        {
            case "random.chestopen":
                return SoundEvents.BLOCK_CHEST_OPEN;
            case "random.chestclosed":
                return SoundEvents.BLOCK_CHEST_CLOSE;
            case "dig.wood":
                return SoundEvents.BLOCK_WOOD_PLACE;
            case "minecart.inside":
                return SoundEvents.ENTITY_MINECART_RIDING;
            case "random.click":
                return SoundEvents.UI_BUTTON_CLICK;
            case "note.pling":
                return SoundEvents.BLOCK_NOTE_HARP;
            case "random.orb":
                return SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
            case "random.break":
                return SoundEvents.ENTITY_ITEM_BREAK;
            default:
                return SoundEvent.REGISTRY.getObject(new ResourceLocation(soundName));
        }
    }

    public static void play(World world, double x, double y, double z, String soundName, float volume, float pitch)
    {
        SoundEvent soundEvent = resolve(soundName);
        if (world != null && soundEvent != null)
            world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, volume, pitch);
    }
}
