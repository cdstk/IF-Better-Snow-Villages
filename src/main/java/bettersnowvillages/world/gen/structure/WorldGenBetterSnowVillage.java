package bettersnowvillages.world.gen.structure;

import bettersnowvillages.compat.CharmHandler;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.config.ForgeConfigHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WorldGenBetterSnowVillage extends WorldGenerator {

    public static boolean isBiomeTypesValid(Set<BiomeDictionary.Type> types) {
        return types.contains(BiomeDictionary.Type.COLD) && types.contains(BiomeDictionary.Type.SNOWY);
    }

    public static boolean isComponentOfSnowVillage(StructureVillagePieces.Start start) {
        return start instanceof BetterSnowVillagePieces.SnowWell;
    }

    public static boolean isVillageGenAllowedInDim(int id) {
        for (int i : IceAndFireForksUtil.getSnowVillageDimBlacklistConfig(ForgeConfigHandler.betterSVGen.useIFConfig)) {
            if (i == id) return false;
        }
        for (int i : IceAndFireForksUtil.getSnowVillageDimWhitelistConfig(ForgeConfigHandler.betterSVGen.useIFConfig)) {
            if (i == id) return true;
        }
        return ModLoadedUtil.ICEANDFIRE.fork != ModLoadedUtil.INFLoadedContainer.FORK.RLCRAFT;
    }

    public static IBlockState getBasicSnowyPalletSwap(IBlockState blockState) {
        if(blockState == Blocks.COBBLESTONE.getDefaultState()) {
            blockState = IceAndFireForksUtil.getFrozenCobblestone().getDefaultState();
        }
        else if(blockState == Blocks.PLANKS.getDefaultState()) {
            blockState = Blocks.SNOW.getDefaultState();
        }
        else if(blockState == Blocks.LOG.getDefaultState()) {
            blockState = Blocks.PACKED_ICE.getDefaultState();
        }
        return blockState;
    }

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
        WorldGenBetterSnowVillage.Start start = new WorldGenBetterSnowVillage.Start(worldIn, rand, chunkX, chunkZ, 0);
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
        if(!isVillageGenAllowedInDim(worldIn.provider.getDimension()))
            return false;

        Biome biome = worldIn.getBiome(position);
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);


        if (isBiomeTypesValid(types)) {
            return true;
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
                    StructureComponent house = pendingHouses.remove(i);
                    house.buildComponent(start, this.components, rand);
                } else {
                    int j = rand.nextInt(pendingRoads.size());
                    StructureComponent road = pendingRoads.remove(j);
                    road.buildComponent(start, this.components, rand);
                }
            }

            this.updateBoundingBox();
            int k = 0;

            for (StructureComponent structureComponent : this.components) {
                if (!(structureComponent instanceof StructureVillagePieces.Road)) {
                    ++k;
                }
            }

            this.hasMoreThanTwoComponents = k > 2;
        }

        public void markCharmSnowVillageChunks(World world, BlockPos blockPos) {
            if(!ModLoadedUtil.CHARM.isLoaded()) return;

            ArrayList<ChunkPos> chunks = new ArrayList<>();

            int minChunkX = boundingBox.minX >> 4;
            int maxChunkX = boundingBox.maxX >> 4;
            int minChunkZ = boundingBox.minZ >> 4;
            int maxChunkZ = boundingBox.maxZ >> 4;

            for (int x = minChunkX; x <= maxChunkX; x++) {
                for (int z = minChunkZ; z <= maxChunkZ; z++) {
                    chunks.add(new ChunkPos(x, z));
                }
            }

            CharmHandler.addSnowVillageChunks(world.provider.getDimension(), (long) blockPos.toString().hashCode(), chunks);
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
