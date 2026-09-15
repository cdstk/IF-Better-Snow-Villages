package bettersnowvillages.world.gen.structure;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.charm.CharmHandler;
import bettersnowvillages.config.ForgeConfigHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MapGenBetterSnowVillage extends MapGenStructure {

    public static List<Biome> SNOW_VILLAGE_SPAWN_BIOMES = new ArrayList<>();
    private int size;
    private int distance;
    private int minTownSeparation = MIN_TOWN_SEPARATION; // 8 in Vanilla

    private static final int MIN_TOWN_SEPARATION = 8; // Where I saw 8 used in MapGenVillage that made sense

    public static final int RNG_SALT = 10387312; // https://minecraft.wiki/w/Structure_set#Default_structure_sets

    public static boolean isBiomeColdAndSnowy(Set<BiomeDictionary.Type> types) {
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

    public MapGenBetterSnowVillage() {
//        this.distance = 32;
//        this.minTownSeparation = 8;
        this.onConfigRefresh();
    }

    public MapGenBetterSnowVillage(Map<String, String> map) {
        this();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("size")) {
                this.size = MathHelper.getInt(entry.getValue(), this.size, 0);
            }
            else if (entry.getKey().equals("distance")) {
                this.distance = MathHelper.getInt(entry.getValue(), this.distance, 1);
            }
            else if (entry.getKey().equals("separation")) {
                this.minTownSeparation = MathHelper.getInt(entry.getValue(), this.minTownSeparation, 1);
            }
        }
    }

    public void onConfigRefresh() {
//        this.distance = IceAndFireForksUtil.getSnowVillageChanceConfig(ForgeConfigHandler.betterSVGen.useIFConfig);
        this.minTownSeparation = 1 + (IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig) >> 4);
//        this.distance = Math.max(
//                1 + (IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig) >> 4),
//                IceAndFireForksUtil.getSnowVillageChanceConfig(ForgeConfigHandler.betterSVGen.useIFConfig)
//        );
        // Previous tried:
        // Map 1 / n chunk chance -> n chunk distance
        // Map Min block distance -> n min chunk separation
        // Result was far to rare compared to Vanilla Ice and Fire
        this.minTownSeparation = Math.min(MIN_TOWN_SEPARATION, this.minTownSeparation); // Capped at Vanilla Village
        this.distance = 1 + Math.max(this.minTownSeparation, IceAndFireForksUtil.getSnowVillageMinimumDistance(ForgeConfigHandler.betterSVGen.useIFConfig) >> 4);
    }

    @Override
    public String getStructureName() {
        return BetterSnowVillages.MODID + ":BetterSnowVillage";
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        if(!IceAndFireForksUtil.getSnowVillageGenConfig(false))
            return false;
        // Chance would go here, but getNearestStructurePos() calls this
        if(!MapGenBetterSnowVillage.isVillageGenAllowedInDim(this.world.provider.getDimension()))
            return false;

        int targetChunkX = chunkX;
        int targetChunkZ = chunkZ;

        if (chunkX < 0) {
            chunkX -= this.distance - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= this.distance - 1;
        }

        int xDistBetween = chunkX / this.distance;
        int zDistBetween = chunkZ / this.distance;
        Random random = this.world.setRandomSeed(xDistBetween, zDistBetween, RNG_SALT);
        xDistBetween = xDistBetween * this.distance;
        zDistBetween = zDistBetween * this.distance;
        xDistBetween = xDistBetween + random.nextInt(this.distance - this.minTownSeparation);
        zDistBetween = zDistBetween + random.nextInt(this.distance - this.minTownSeparation);

        if (targetChunkX == xDistBetween && targetChunkZ == zDistBetween) {
            boolean validBiome = this.world.getBiomeProvider().areBiomesViable(targetChunkX * 16 + 8, targetChunkZ * 16 + 8, 0, SNOW_VILLAGE_SPAWN_BIOMES);

            if (validBiome) {
                return true;
            }
        }

        return false;
    }

    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored, int distanceStep) {
        distanceStep = Math.max(1 + this.minTownSeparation, distanceStep);
        // Use Vanilla distance instead of configurable
        this.world = worldIn;
        return findNearestStructurePosBySpacing(
                worldIn,
                this,
                pos,
                distanceStep,
                this.minTownSeparation,
                RNG_SALT,
                false,
                100,
                findUnexplored
        );
    }

    @Override
    @Nullable
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        return this.getNearestStructurePos(worldIn, pos, findUnexplored, this.distance);
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        int x = (chunkX * 16) + 8;
        int z = (chunkZ * 16) + 8;
        BlockPos position = new BlockPos(x, 0, z);

        MapGenBetterSnowVillage.Start start = new MapGenBetterSnowVillage.Start(this.world, this.rand, chunkX, chunkZ, this.size);

        StructureBoundingBox boundingBox = start.getBoundingBox();
        boundingBox.minY = 1;
        boundingBox.maxY = 512;
        start.markCharmSnowVillageChunks(this.world, position);
        return start;
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
