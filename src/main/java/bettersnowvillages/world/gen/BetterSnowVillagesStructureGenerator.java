package bettersnowvillages.world.gen;

import bettersnowvillages.world.gen.structure.WorldGenBetterSnowVillage;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class BetterSnowVillagesStructureGenerator implements IWorldGenerator {

    private static final WorldGenBetterSnowVillage SNOW_VILLAGE = new WorldGenBetterSnowVillage();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = (chunkX * 16) + 8;
        int z = (chunkZ * 16) + 8;
        BlockPos position = new BlockPos(x, 0, z);
        if(SNOW_VILLAGE.canSpawnStructureAtCoords(world, random, position))
            SNOW_VILLAGE.generate(world, random, position);
    }
}
