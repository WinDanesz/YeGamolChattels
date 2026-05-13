package ivorius.yegamolchattels.items;

import ivorius.ivtoolkit.blocks.BlockCoord;
import ivorius.ivtoolkit.blocks.IvBlockCollection;
import ivorius.yegamolchattels.blocks.TileEntityMicroBlock;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockFragment extends Item implements MicroblockSelector
{
    public static void setFragment(ItemStack stack, ItemChisel.BlockData blockData)
    {
        stack.setTagInfo("blockFragment", new NBTTagString(String.valueOf(Block.REGISTRY.getNameForObject(blockData.block))));
        stack.setTagInfo("blockFragmentMeta", new NBTTagByte(blockData.meta));
    }

    public static ItemChisel.BlockData getFragment(ItemStack stack)
    {
        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("blockFragment"))
            return new ItemChisel.BlockData(Blocks.AIR, (byte) 0);

        return new ItemChisel.BlockData((Block) Block.REGISTRY.getObject(new ResourceLocation(stack.getTagCompound().getString("blockFragment"))), stack.getTagCompound().getByte("blockFragmentMeta"));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return addBlock(pos.getX(), pos.getY(), pos.getZ(), player, player.getHeldItem(hand)) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }

    public static boolean addBlock(int x, int y, int z, EntityPlayer player, ItemStack usedStack)
    {
        if (addBlock(player, x, y, z, getFragment(usedStack)))
        {
            usedStack.shrink(1);
            player.inventory.markDirty();
            return true;
        }

        return false;
    }

    public static boolean addBlock(Entity entity, int hoverX, int hoverY, int hoverZ, ItemChisel.BlockData blockFragment)
    {
        World world = entity.world;
        ItemChisel.MicroBlockFragment hoveredFragment = ItemChisel.getHoveredFragment(entity, hoverX, hoverY, hoverZ);

        if (hoveredFragment == null)
            return false;

        hoveredFragment = hoveredFragment.getOpposite();
        BlockCoord fragmentCoord = hoveredFragment.getCoord();
        BlockPos pos = new BlockPos(fragmentCoord.x, fragmentCoord.y, fragmentCoord.z);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityMicroBlock))
        {
            ItemChisel.convertToMicroBlock(world, fragmentCoord);
            tileEntity = world.getTileEntity(pos);
        }

        if (tileEntity instanceof TileEntityMicroBlock)
        {
            TileEntityMicroBlock tileEntityMicroBlock = (TileEntityMicroBlock) tileEntity;
            IvBlockCollection collection = tileEntityMicroBlock.getBlockCollection();
            Block hitInternalBlock = collection.getBlock(hoveredFragment.getInternalCoord());
            if (hitInternalBlock == Blocks.AIR)
            {
                collection.setBlockAndMetadata(hoveredFragment.getInternalCoord(), blockFragment.block, blockFragment.meta);
                if (tileEntityMicroBlock.validateBeingMicroblock())
                    tileEntityMicroBlock.markCacheInvalid();

                return true;
            }
        }

        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> list, ITooltipFlag flag)
    {
        ItemChisel.BlockData data = getFragment(stack);
        if (data != null)
            list.add(ItemMicroBlock.getLocalizedName(data));
    }

    @Override
    public boolean showMicroblockSelection(EntityLivingBase renderEntity, ItemStack stack)
    {
        return true;
    }

    @Override
    public float microblockSelectionSize(ItemStack stack)
    {
        return 0.52f;
    }
}
