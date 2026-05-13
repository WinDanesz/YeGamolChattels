package net.minecraft.util;

import net.minecraft.util.text.translation.I18n;

public class StatCollector
{
    public static String translateToLocal(String key)
    {
        return I18n.translateToLocal(key);
    }

    public static String translateToLocalFormatted(String key, Object... args)
    {
        return I18n.translateToLocalFormatted(key, args);
    }
}
