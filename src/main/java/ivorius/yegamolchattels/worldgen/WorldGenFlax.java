package ivorius.yegamolchattels.worldgen;

import ivorius.yegamolchattels.blocks.YGCBlocks;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.Set;

public class WorldGenFlax implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        int worldX = chunkX * 16;
        int worldZ = chunkZ * 16;
        Biome biome = world.getBiome(new BlockPos(worldX + 8, 0, worldZ + 8));
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);

        if (types.size() == 1 && types.contains(BiomeDictionary.Type.PLAINS))
        {
            int flaxPlants = 0;
            while (random.nextFloat() < 0.8f && flaxPlants < 10)
                flaxPlants++;

            for (int flower = 0; flower < flaxPlants; ++flower)
            {
                int x = worldX + random.nextInt(16) + 8;
                int z = worldZ + random.nextInt(16) + 8;
                int y = random.nextInt(world.getHeight(x, z) + 32);
                BlockPos pos = new BlockPos(x, y, z);

                if (world.isAirBlock(pos) && YGCBlocks.flax_plant.canPlaceBlockAt(world, pos))
                {
                    int age = 7 - (random.nextFloat() < 0.1f ? 1 : 0);
                    IBlockState state = ((BlockCrops) YGCBlocks.flax_plant).withAge(age);
                    world.setBlockState(pos, state, 2);
                }
            }
        }
    }
}
