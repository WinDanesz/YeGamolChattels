package ivorius.yegamolchattels.achievements;

import net.minecraft.entity.player.EntityPlayer;

public class YGCAchievementList
{
    public static final AchievementKey refinedPlank = new AchievementKey("refinedPlank");
    public static final AchievementKey wardrobeCrafted = new AchievementKey("wardrobeCrafted");
    public static final AchievementKey wardrobeSecret = new AchievementKey("wardrobeSecret");
    public static final AchievementKey grandfatherClockCrafted = new AchievementKey("grandfatherClockCrafted");
    public static final AchievementKey weaponRackCrafted = new AchievementKey("weaponRackCrafted");
    public static final AchievementKey weaponRackVariant = new AchievementKey("weaponRackVariant");
    public static final AchievementKey largeFlagCrafted = new AchievementKey("largeFlagCrafted");
    public static final AchievementKey largeBannerCrafted = new AchievementKey("largeBannerCrafted");
    public static final AchievementKey smallGongPlayed = new AchievementKey("smallGongPlayed");
    public static final AchievementKey mediumGongPlayed = new AchievementKey("mediumGongPlayed");
    public static final AchievementKey largeGongPlayed = new AchievementKey("largeGongPlayed");
    public static final AchievementKey gongSecret = new AchievementKey("gongSecret");
    public static final AchievementKey ghostKilled = new AchievementKey("ghostKilled");
    public static final AchievementKey zombieStatueReanimated = new AchievementKey("zombieStatueReanimated");
    public static final AchievementKey superExpensiveStatue = new AchievementKey("superExpensiveStatue");

    public static void init()
    {
    }

    public static void trigger(EntityPlayer player, AchievementKey achievement)
    {
    }

    public static class AchievementKey
    {
        private final String id;

        public AchievementKey(String id)
        {
            this.id = id;
        }

        public String getId()
        {
            return id;
        }
    }
}
