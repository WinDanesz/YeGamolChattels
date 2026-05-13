package ivorius.yegamolchattels.math;

import net.minecraft.util.MathHelper;

import java.util.Random;

public class IvMathHelper
{
    public static double mix(double value1, double value2, double progress)
    {
        return value1 + (value2 - value1) * progress;
    }

    public static float mix(float value1, float value2, float progress)
    {
        return value1 + (value2 - value1) * progress;
    }

    public static float nearValue(float value, float dest, float mulSpeed, float plusSpeed)
    {
        value += (dest - value) * mulSpeed;

        if (value > dest)
        {
            value -= plusSpeed;
            if (value < dest)
                value = dest;
        }
        else if (value < dest)
        {
            value += plusSpeed;
            if (value > dest)
                value = dest;
        }

        return value;
    }

    public static int randomLinearNumber(Random random, float number)
    {
        return MathHelper.floor_float(number) + (random.nextFloat() < (number % 1.0f) ? 1 : 0);
    }
}
