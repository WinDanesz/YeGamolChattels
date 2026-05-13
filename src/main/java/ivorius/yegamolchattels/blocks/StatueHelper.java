package ivorius.yegamolchattels.blocks;

import ivorius.yegamolchattels.multiblock.IvMultiBlockHelper;
import ivorius.yegamolchattels.multiblock.IvTileEntityMultiBlock;
import ivorius.yegamolchattels.items.ItemStatue;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukas on 28.09.14.
 */
public class StatueHelper
{
    public static boolean canCarveStatue(Entity statueEntity, World world, int x, int y, int z)
    {
        if (isValidStatueBlock(world, x, y, z))
        {
            Statue.BlockFragment blockFragment = getBlockFragment(world, new BlockPos(x, y, z));

            List<int[]> positions = ItemStatue.getStatuePositions(statueEntity, 0);
            List<int[]> validPositions = getValidPositions(positions, world, blockFragment, x, y, z);

            if (validPositions != null)
                return true;
        }

        return false;
    }

    public static TileEntityStatue carveStatue(ItemStack stack, Statue statue, World world, int x, int y, int z, EntityLivingBase entityLivingBase)
    {
        if (isValidStatueBlock(world, x, y, z))
        {
            Statue.BlockFragment blockFragment = getBlockFragment(world, new BlockPos(x, y, z));
            int rotation = 0;

            List<int[]> positions = ItemStatue.getStatuePositions(statue.getEntity(), rotation);
            List<int[]> validPositions = getValidPositions(positions, world, blockFragment, x, y, z);

            if (validPositions != null)
            {
                IvMultiBlockHelper multiBlockHelper = new IvMultiBlockHelper();
                if (multiBlockHelper.beginPlacing(validPositions, world, YGCBlocks.statue, 0, rotation))
                {
                    TileEntityStatue parent = null;

                    for (int[] position : multiBlockHelper)
                    {
                        IvTileEntityMultiBlock tileEntity = multiBlockHelper.placeBlock(position);

                        if (tileEntity instanceof TileEntityStatue && tileEntity.isParent())
                        {
                            parent = (TileEntityStatue) tileEntity;
                            TileEntityStatue tileEntityStatue = (TileEntityStatue) tileEntity;
                            statue.setMaterial(blockFragment);
                            tileEntityStatue.setStatue(statue);
                            tileEntityStatue.setStatueRotationYaw((entityLivingBase.rotationYaw + 180.0f) % 360.0f);
                        }
                    }

                    stack.damageItem(1, entityLivingBase);
                    return parent;
                }
            }
        }

        return null;
    }

    public static boolean isValidStatueBlock(World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        Statue.BlockFragment blockFragment = getBlockFragment(world, pos);
        return blockFragment.getState().getBlockHardness(world, pos) >= 0.0f && isValidStatueBlock(blockFragment);
    }

    private static boolean isValidStatueBlock(Statue.BlockFragment fragment)
    {
        Block block = fragment.getBlock();
        IBlockState state = fragment.getState();
        return !block.hasTileEntity(state) && (block.isOpaqueCube(state) || block == Blocks.GLASS || block == Blocks.STAINED_GLASS);
    }

    public static List<int[]> getValidPositions(List<int[]> positions, World world, Statue.BlockFragment blockFragment, int x, int y, int z)
    {
        List<int[]> validLocations = new ArrayList<>();

        for (int[] origin : positions)
        {
            for (int[] position : positions)
            {
                int posX = position[0] + x - origin[0];
                int posY = position[1] + y - origin[1];
                int posZ = position[2] + z - origin[2];

                Statue.BlockFragment comparedFragment = getBlockFragment(world, new BlockPos(posX, posY, posZ));
                if (comparedFragment.getBlock() != blockFragment.getBlock() || comparedFragment.getMetadata() != blockFragment.getMetadata())
                    break;

                validLocations.add(new int[]{posX, posY, posZ});
            }

            if (validLocations.size() == positions.size())
                return validLocations;

            validLocations.clear();
        }

        return null;
    }

    private static Statue.BlockFragment getBlockFragment(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        return new Statue.BlockFragment(state.getBlock(), state.getBlock().getMetaFromState(state));
    }
}
