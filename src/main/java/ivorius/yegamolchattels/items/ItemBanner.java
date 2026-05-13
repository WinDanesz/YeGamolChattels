package ivorius.yegamolchattels.items;

import ivorius.yegamolchattels.entities.EntityBanner;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBanner extends Item
{
    public final int bannerSize;
    public final String namePrefix;

    public ItemBanner(int bannerSize, String namePrefix)
    {
        setHasSubtypes(true);
        setMaxDamage(0);
        this.bannerSize = bannerSize;
        this.namePrefix = namePrefix;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing == EnumFacing.DOWN || facing == EnumFacing.UP)
            return EnumActionResult.FAIL;

        ItemStack stack = player.getHeldItem(hand);
        BlockPos spawnPos = bannerSize == 2 ? pos.down(2) : pos;
        EntityBanner banner = new EntityBanner(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), facing.getHorizontalIndex(), bannerSize, stack.getMetadata());

        if (!player.canPlayerEdit(spawnPos, facing, stack))
            return EnumActionResult.FAIL;

        if (banner.onValidSurface())
        {
            if (!world.isRemote)
                world.spawnEntity(banner);
            stack.shrink(1);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.FAIL;
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return super.getTranslationKey(stack) + ".dye" + stack.getMetadata();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab))
            return;

        for (int i = 0; i < 16; ++i)
            items.add(new ItemStack(this, 1, i));
    }
}
