package bettersnowvillages.world.gen.structure;

import bettersnowvillages.compat.AAAMUtil;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.config.ForgeConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class WorldGenBetterSnowVillage extends WorldGenerator {

    private volatile BlockPos lastSnowVillage = null;

    public WorldGenBetterSnowVillage() {

    }

    @Override
    public  boolean generate(World worldIn, Random rand, BlockPos position) {
        double spawnCheck = IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig)
                * IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig);

        if(this.lastSnowVillage != null && this.lastSnowVillage.distanceSq(position) < spawnCheck) {
            return false;
        }
        this.lastSnowVillage = position;

        int chunkX = position.getX() >> 4;
        int chunkZ = position.getZ() >> 4;
        MapGenBetterSnowVillage.Start start = new MapGenBetterSnowVillage.Start(worldIn, rand, chunkX, chunkZ, 0);

        if(ModLoadedUtil.AAAM.isLoaded()) {
            AAAMUtil.autoMarkStructureStart(start, worldIn);
        }

        StructureBoundingBox boundingBox = start.getBoundingBox();
        boundingBox.minY = 1;
        boundingBox.maxY = 512;
        start.generateStructure(worldIn, rand, boundingBox);
        start.markCharmSnowVillageChunks(worldIn, position);
        return true;
    }

    public boolean canSpawnStructureAtCoords(World worldIn, Random rand, BlockPos position) {
        if(!IceAndFireForksUtil.getSnowVillageGenConfig(false))
            return false;
        if(rand.nextInt(IceAndFireForksUtil.getSnowVillageChanceConfig(ForgeConfigHandler.betterSVGen.useIFConfig)) != 0)
            return false;
        if(!MapGenBetterSnowVillage.isVillageGenAllowedInDim(worldIn.provider.getDimension()))
            return false;

        if(worldIn.getBiomeProvider().areBiomesViable(position.getX(), position.getZ(), 0, MapGenBetterSnowVillage.SNOW_VILLAGE_SPAWN_BIOMES)) {
            return true;
        }

        return false;
    }
}
