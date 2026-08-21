package bettersnowvillages.world.gen.structure;

import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.handlers.ForgeConfigHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class WorldGenBetterSnowVillage extends WorldGenerator {

    public static boolean isVillageGenAllowedInDim(int id) {
        for (int i : IceAndFireForksUtil.getSnowVillageDimensionsConfig(ForgeConfigHandler.betterSVGen.useIFConfig)) {
            if (i == id) return IceAndFireForksUtil.getSnowVillageDimWhitelistConfig(ForgeConfigHandler.betterSVGen.useIFConfig);
        }
        return !IceAndFireForksUtil.getSnowVillageDimWhitelistConfig(ForgeConfigHandler.betterSVGen.useIFConfig);
    }

    // This should be threadsafe as threads do generate next to each other in IF RLC
    private BlockPos lastSnowVillage = null;

    public synchronized void updatePosition(BlockPos newPos) {
        this.lastSnowVillage = newPos;
    }

    public synchronized BlockPos getLastPosition() {
        return this.lastSnowVillage;
    }

    public WorldGenBetterSnowVillage() {

    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int new_size = 32;
        int chunkX = position.getX() >> 4;
        int chunkZ = position.getZ() >> 4;
        new WorldGenBetterSnowVillage.Start(worldIn, rand, chunkX, chunkZ, 0)
                .generateStructure(worldIn, rand,
                        new StructureBoundingBox(
                                position.getX() - new_size, position.getZ() - new_size,
                                position.getX() + new_size, position.getZ() + new_size
        ));
        updatePosition(position);
        return true;
    }

    public boolean canSpawnStructureAtCoords(World worldIn, Random rand, BlockPos position) {
        if(!IceAndFireForksUtil.getSnowVillageGenConfig(false))
            return false;
        if(rand.nextInt(IceAndFireForksUtil.getSnowVillageChanceConfig(ForgeConfigHandler.betterSVGen.useIFConfig)) != 0)
            return false;
        if(!isVillageGenAllowedInDim(worldIn.provider.getDimension()))
            return false;

        Biome biome = worldIn.getBiome(position);
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);

        double spawnCheck = IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig)
                * IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig);

        boolean isCold = types.contains(BiomeDictionary.Type.COLD);
        boolean isSnowy = types.contains(BiomeDictionary.Type.SNOWY);

        if (isCold && isSnowy) {
            BlockPos lastPos = getLastPosition();
            if(lastPos == null || lastPos.distanceSq(position) >= spawnCheck) {
                return true;
            }
        }

        return false;
    }

    public static class Start extends StructureStart {

        private boolean hasMoreThanTwoComponents;

        public Start() {}

        public Start(World worldIn, Random rand, int x, int z, int size) {
            super(x, z);
            List<StructureVillagePieces.PieceWeight> list = BetterSnowVillagePieces.getStructureVillageWeightedPieceList(rand, size);
            StructureVillagePieces.Start start = new BetterSnowVillagePieces.SnowWell(worldIn.getBiomeProvider(), 3, rand, (x << 4) + 2, (z << 4) + 2, list, size);
            this.components.add(start);
            start.buildComponent(start, this.components, rand);
            List<StructureComponent> pendingRoads = start.pendingRoads;
            List<StructureComponent> pendingHouses = start.pendingHouses;

            while (!pendingRoads.isEmpty() || !pendingHouses.isEmpty()) {
                if (pendingRoads.isEmpty()) {
                    int i = rand.nextInt(pendingHouses.size());
                    StructureComponent structurecomponent = pendingHouses.remove(i);
                    structurecomponent.buildComponent(start, this.components, rand);
                } else {
                    int j = rand.nextInt(pendingRoads.size());
                    StructureComponent structurecomponent2 = pendingRoads.remove(j);
                    structurecomponent2.buildComponent(start, this.components, rand);
                }
            }

            this.updateBoundingBox();
            int k = 0;

            for (StructureComponent structurecomponent1 : this.components) {
                if (!(structurecomponent1 instanceof StructureVillagePieces.Road)) {
                    ++k;
                }
            }

            this.hasMoreThanTwoComponents = k > 2;
        }

        public boolean isSizeableStructure() {
            return this.hasMoreThanTwoComponents;
        }

        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }

        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}
