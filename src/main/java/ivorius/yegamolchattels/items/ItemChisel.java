/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://lukas.axxim.net
 */

package ivorius.yegamolchattels.items;

import ivorius.ivtoolkit.blocks.BlockArea;
import ivorius.ivtoolkit.blocks.BlockCoord;
import ivorius.ivtoolkit.blocks.IvBlockCollection;
import ivorius.yegamolchattels.YeGamolChattels;
import ivorius.yegamolchattels.blocks.StatueHelper;
import ivorius.yegamolchattels.blocks.TileEntityMicroBlock;
import ivorius.yegamolchattels.blocks.YGCBlocks;
import ivorius.yegamolchattels.gui.YGCGuiHandler;
import ivorius.yegamolchattels.utils.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by lukas on 11.07.14.
 */
public class ItemChisel extends ItemTool implements MicroblockSelector
{
    private int carvingDistance;
    private float fragmentPickupChance;

    public boolean canCarveStatues;

    public ItemChisel(int carvingDistance, float fragmentPickupChance, float damage, ToolMaterial material, Set damageVSBlocks, boolean canCarveStatues)
    {
        super(material, damageVSBlocks);
        this.carvingDistance = carvingDistance;
        this.fragmentPickupChance = fragmentPickupChance;
        this.canCarveStatues = canCarveStatues;
    }

    public int getCarvingDistance()
    {
        return carvingDistance;
    }

    public float getFragmentPickupChance()
    {
        return fragmentPickupChance;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemStack = player.getHeldItem(hand);
        if (InventoryHelper.getInventorySlotContainItem(player.inventory, YGCItems.clubHammer) >= 0)
        {
            if (showMicroblockSelection(player, itemStack))
            {
                int clubHammerSlot = InventoryHelper.getInventorySlotContainItem(player.inventory, YGCItems.clubHammer);
                return chiselAway(pos.getX(), pos.getY(), pos.getZ(), player, itemStack, player.inventory.getStackInSlot(clubHammerSlot), carvingDistance, fragmentPickupChance) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            }
            else
            {
                if (StatueHelper.isValidStatueBlock(world, pos.getX(), pos.getY(), pos.getZ()))
                {
                    if (!world.isRemote) // Some entities start with random sizes
                    {
                        player.openGui(YeGamolChattels.instance, YGCGuiHandler.statueCarvingGuiID, world, pos.getX(), pos.getY(), pos.getZ());
                    }

                    return EnumActionResult.SUCCESS;
                }
            }
        }
        else
        {
            if (!world.isRemote)
                player.sendMessage(new TextComponentTranslation("item.ygcChisel.noHammer"));
        }

        return EnumActionResult.FAIL;
    }

    public static boolean chiselAway(int x, int y, int z, EntityPlayer player, ItemStack usedStack, ItemStack clubHammer, int range, float fragmentPickupChance)
    {
        List<BlockData> hitFragmentDatas = chiselAway(player, x, y, z, range);

        if (hitFragmentDatas != null && hitFragmentDatas.size() > 0)
        {
            for (BlockData data : hitFragmentDatas)
            {
                if (itemRand.nextFloat() < fragmentPickupChance)
                {
                    usedStack.damageItem(1, player);
                    clubHammer.damageItem(1, player);

                    ItemStack fragment = new ItemStack(YGCItems.blockFragment);
                    ItemBlockFragment.setFragment(fragment, data);
                    player.inventory.addItemStackToInventory(fragment);
                    player.inventory.markDirty();
                }
            }

            return true;
        }

        return false;
    }

    public static List<BlockData> chiselAway(Entity entity, int hoverX, int hoverY, int hoverZ, int range)
    {
        World world = entity.world;
        MicroBlockFragment hoveredFragment = getHoveredFragment(entity, hoverX, hoverY, hoverZ);

        if (hoveredFragment != null)
        {
            TileEntity tileEntity = world.getTileEntity(new BlockPos(hoveredFragment.coord.x, hoveredFragment.coord.y, hoveredFragment.coord.z));

            if (!(tileEntity instanceof TileEntityMicroBlock))
            {
                convertToMicroBlock(world, hoveredFragment.coord);
                tileEntity = world.getTileEntity(new BlockPos(hoveredFragment.coord.x, hoveredFragment.coord.y, hoveredFragment.coord.z));
            }

            if (tileEntity instanceof TileEntityMicroBlock)
            {
                TileEntityMicroBlock tileEntityMicroBlock = (TileEntityMicroBlock) tileEntity;
                IvBlockCollection collection = tileEntityMicroBlock.getBlockCollection();

                List<BlockData> returnList = new ArrayList<>();
                for (BlockCoord carveCoord : new BlockArea(hoveredFragment.getInternalCoord().subtract(range, range, range), hoveredFragment.getInternalCoord().add(range, range, range)))
                {
                    Block hitInternalBlock = collection.getBlock(carveCoord);
                    if (hitInternalBlock != Blocks.AIR)
                    {
                        byte hitInternalMeta = collection.getMetadata(carveCoord);

                        collection.setBlockAndMetadata(carveCoord, Blocks.AIR, (byte) 0);
                        returnList.add(new BlockData(hitInternalBlock, hitInternalMeta));
                    }
                }

                if (tileEntityMicroBlock.validateBeingMicroblock())
                    tileEntityMicroBlock.markCacheInvalid();

                return returnList;
            }
        }

        return null;
    }

    public static void convertToMicroBlock(World world, BlockCoord coord)
    {
        Block block = coord.getBlock(world);
        if (ItemClubHammer.isMicroblockable(world, coord.x, coord.y, coord.z) || block == Blocks.AIR)
        {
            byte metadata = (byte) coord.getMetadata(world);

            world.setBlockState(new BlockPos(coord.x, coord.y, coord.z), YGCBlocks.microBlock.getDefaultState(), 3);
            TileEntity tileEntity = world.getTileEntity(new BlockPos(coord.x, coord.y, coord.z));

            TileEntityMicroBlock tileEntityMicroBlock = (TileEntityMicroBlock) tileEntity;
            IvBlockCollection blockCollection = tileEntityMicroBlock.getBlockCollection();

            if (block != Blocks.AIR) // Default val
            {
                for (BlockCoord internalCoord : blockCollection)
                    blockCollection.setBlockAndMetadata(internalCoord, block, metadata);
            }
        }
    }

    public static MicroBlockFragment getHoveredFragment(Entity entity, int hoverX, int hoverY, int hoverZ)
    {
        float partialTicks = 1.0f;
        double entityX = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
        double eyeHeight = entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getEyeHeight() : 0.0D;
        double entityY = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + eyeHeight;
        double entityZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;

        if (!entity.world.isRemote && entity instanceof EntityPlayer && entity.isSneaking())
            entityY -= 0.1f; // TODO Find a way not to hardcode this

        Vec3 entityPos = Vec3.createVectorHelper(entityX, entityY, entityZ);
        return getHoveredFragment(entity.world, hoverX, hoverY, hoverZ, entityPos, Vec3.createVectorHelper(entity.getLookVec().x, entity.getLookVec().y, entity.getLookVec().z));
    }

    public static MicroBlockFragment getHoveredFragment(World world, int hoverX, int hoverY, int hoverZ, Vec3 entityPos, Vec3 entityLook)
    {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(hoverX, hoverY, hoverZ));
        Block origBlock = world.getBlockState(new BlockPos(hoverX, hoverY, hoverZ)).getBlock();

        IvBlockCollection collection = null;
        if (tileEntity instanceof TileEntityMicroBlock)
        {
            TileEntityMicroBlock tileEntityMicroBlock = (TileEntityMicroBlock) tileEntity;
            collection = tileEntityMicroBlock.getBlockCollection();
        }
        else if (ItemClubHammer.isMicroblockable(world, hoverX, hoverY, hoverZ))
        {
            collection = new IvBlockCollection(TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_X, TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Y, TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Z);

            for (BlockCoord coord : collection)
                collection.setBlockAndMetadata(coord, origBlock, (byte) origBlock.getMetaFromState(world.getBlockState(new BlockPos(hoverX, hoverY, hoverZ))));
        }

        if (collection != null)
        {
            Vec3 rayStart = getPositionInBlockCollection(collection, new BlockCoord(hoverX, hoverY, hoverZ), entityPos);
            MovingObjectPosition hitPosition = collection.rayTrace(rayStart, entityLook);
            if (hitPosition != null)
            {
                BlockCoord hitCoord = new BlockCoord(hitPosition.blockX, hitPosition.blockY, hitPosition.blockZ);
                Block hitInternalBlock = collection.getBlock(hitCoord);

                if (hitInternalBlock != Blocks.AIR)
                {
                    return new MicroBlockFragment(new BlockCoord(hoverX, hoverY, hoverZ), hitCoord, ForgeDirection.getOrientation(hitPosition.sideHit), hitPosition.hitVec);
                }
            }
        }

        return null;
    }

    public static Vec3 getPositionInBlockCollection(IvBlockCollection blockCollection, BlockCoord referenceCoord, Vec3 pos)
    {
        return Vec3.createVectorHelper((pos.xCoord - referenceCoord.x) * blockCollection.width, (pos.yCoord - referenceCoord.y) * blockCollection.height, (pos.zCoord - referenceCoord.z) * blockCollection.length);
    }

    @Override
    public boolean showMicroblockSelection(EntityLivingBase renderEntity, ItemStack stack)
    {
        return !(canCarveStatues && renderEntity.isSneaking());
    }

    @Override
    public float microblockSelectionSize(ItemStack stack)
    {
        return 0.52f + carvingDistance;
    }

    public static class MicroBlockFragment
    {
        private BlockCoord coord;
        private BlockCoord internalCoord;
        private ForgeDirection internalSide;
        private Vec3 hitPoint;

        public MicroBlockFragment(BlockCoord coord, BlockCoord internalCoord, ForgeDirection internalSide, Vec3 hitPoint)
        {
            this.coord = coord;
            this.internalCoord = internalCoord;
            this.internalSide = internalSide;
            this.hitPoint = hitPoint;
        }

        public BlockCoord getCoord()
        {
            return coord;
        }

        public BlockCoord getInternalCoord()
        {
            return internalCoord;
        }

        public ForgeDirection getInternalSide()
        {
            return internalSide;
        }

        public Vec3 getHitPoint()
        {
            return hitPoint;
        }

        public MicroBlockFragment getOpposite()
        {
            int blockX = coord.x;
            int blockY = coord.y;
            int blockZ = coord.z;
            int internalX = internalCoord.x + internalSide.offsetX;
            int internalY = internalCoord.y + internalSide.offsetY;
            int internalZ = internalCoord.z + internalSide.offsetZ;

            if (internalX < 0)
            {
                internalX = TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_X - 1;
                blockX--;
            }
            else if (internalX >= TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_X)
            {
                internalX = 0;
                blockX++;
            }

            if (internalY < 0)
            {
                internalY = TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Y - 1;
                blockY--;
            }
            else if (internalY >= TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Y)
            {
                internalY = 0;
                blockY++;
            }

            if (internalZ < 0)
            {
                internalZ = TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Z - 1;
                blockZ--;
            }
            else if (internalZ >= TileEntityMicroBlock.MICROBLOCKS_PER_BLOCK_Z)
            {
                internalZ = 0;
                blockZ++;
            }

            return new MicroBlockFragment(new BlockCoord(blockX, blockY, blockZ), new BlockCoord(internalX, internalY, internalZ), internalSide.getOpposite(), hitPoint);
        }

        @Override
        public String toString()
        {
            return "MicroBlockFragment{" +
                    "coord=" + coord +
                    ", internalCoord=" + internalCoord +
                    ", internalSide=" + internalSide +
                    ", hitPoint=" + hitPoint +
                    '}';
        }
    }

    public static class BlockData
    {
        public Block block;
        public byte meta;

        public BlockData(Block block, byte meta)
        {
            this.block = block;
            this.meta = meta;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BlockData blockData = (BlockData) o;

            return block == blockData.block && meta == blockData.meta;
        }

        @Override
        public int hashCode()
        {
            int result = block.hashCode();
            result = 31 * result + (int) meta;
            return result;
        }

        @Override
        public String toString()
        {
            return "BlockData{" +
                    "block=" + block +
                    ", meta=" + meta +
                    '}';
        }
    }
}
