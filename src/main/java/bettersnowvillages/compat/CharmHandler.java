package bettersnowvillages.compat;

import bettersnowvillages.world.gen.structure.BetterSnowVillagePieces;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.world.CharmWorld;
import svenhjol.charm.world.decorator.inner.VillageInnerDecorator;
import svenhjol.charm.world.decorator.outer.Barrels;
import svenhjol.charm.world.decorator.outer.Crops;
import svenhjol.charm.world.decorator.outer.Erosion;
import svenhjol.charm.world.decorator.outer.Flowers;
import svenhjol.charm.world.decorator.outer.Lights;
import svenhjol.charm.world.decorator.outer.Mobs;
import svenhjol.charm.world.decorator.outer.Mushrooms;
import svenhjol.charm.world.decorator.outer.Pumpkins;
import svenhjol.charm.world.decorator.outer.Trees;
import svenhjol.charm.world.feature.VillageDecorations;
import svenhjol.meson.decorator.MesonOuterDecorator;
import svenhjol.meson.event.StructureEventBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CharmHandler {

    // Similar to Charm's Vanilla Village Decorator map
    // Integer - Village Dimension
    // Chunk Pos - Village Chunk Position
    // RNG Seed - Village Block Position to be Hashed
    private static final Map<Integer, Map<ChunkPos, Long>> SNOW_VILLAGE_CHUNKS = new HashMap<>();
    private static final List<ChunkPos> ZOMBIE_SNOW_VILLAGES = new ArrayList<>();

    public static void registerSnowVillageDecorations() {
        if(Charm.hasModule(CharmWorld.class) && Charm.hasFeature(VillageDecorations.class)) {
            MinecraftForge.EVENT_BUS.register(CharmHandler.class);
        }
    }

    public static void addSnowVillageChunks(int dimension, Long rngSeed, Collection<ChunkPos> chunkPositions) {
        SNOW_VILLAGE_CHUNKS.computeIfAbsent(dimension, dim -> new HashMap<>());

        chunkPositions.forEach(chunkPos -> SNOW_VILLAGE_CHUNKS.get(dimension).put(chunkPos, rngSeed));
    }

    public static class SnowWellCarpetRemover extends VillageInnerDecorator {
        public SnowWellCarpetRemover(StructureVillagePieces.Village structure, World world, StructureBoundingBox box) {
            super(structure, world, box);
        }

        public void generate() {
            if (VillageDecorations.carpet) {
                for(int xx = 1; xx <= 4; ++xx) {
                    for(int zz = 1; zz <= 4; ++zz) {
                        this.add(Blocks.AIR.getDefaultState(), xx, 16, zz, EnumFacing.NORTH);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDecorate(DecorateBiomeEvent.Decorate event) {
        World world = event.getWorld();
        int dim = world.provider.getDimension();
        if(!SNOW_VILLAGE_CHUNKS.containsKey(dim)) return;

        ChunkPos chunk = event.getChunkPos();
        Map<ChunkPos, Long> chunkRNG = SNOW_VILLAGE_CHUNKS.get(dim);
        if(!chunkRNG.containsKey(chunk)) return;
        if(!event.getType().equals(DecorateBiomeEvent.Decorate.EventType.FLOWERS)) return;

        BlockPos pos = new BlockPos(chunk.x << 4, 0, chunk.z << 4);
        Random eventRand = event.getRand();
        Random villageRand = new Random();
        Long villageSeed = chunkRNG.get(chunk);
        villageRand.setSeed(villageSeed);

        List<ChunkPos> chunks = new ArrayList<>();
        List<MesonOuterDecorator> decorators = new ArrayList<>();
        // Charm doesn't actually use this in villages
//        chunkRNG.forEach((chunkPos, rngSeed) -> {
//            if(rngSeed.equals(villageSeed))
//                chunks.add(chunkPos);
//        });

        if(villageRand.nextDouble() <= VillageDecorations.flowersChance) decorators.add(new Flowers(world, pos, villageRand, chunks));
        if (villageRand.nextDouble() <= VillageDecorations.lightsChance) decorators.add(new Lights(world, pos, eventRand, chunks));
        if (villageRand.nextDouble() <= VillageDecorations.mobsChance) decorators.add(new Mobs(world, pos, eventRand, chunks));
        if (villageRand.nextDouble() <= VillageDecorations.cropsChance) decorators.add(new Crops(world, pos, eventRand, chunks));
        if (villageRand.nextDouble() <= VillageDecorations.barrelsChance) decorators.add(new Barrels(world, pos, eventRand, chunks));
        if (villageRand.nextDouble() <= VillageDecorations.pumpkinsChance) decorators.add(new Pumpkins(world, pos, eventRand, chunks));
        if (villageRand.nextDouble() <= VillageDecorations.treesChance) decorators.add(new Trees(world, pos, eventRand, chunks));
        if (villageRand.nextDouble() <= VillageDecorations.mushroomsChance) decorators.add(new Mushrooms(world, pos, eventRand, chunks));
        if (VillageDecorations.zombieVillageErosion && ZOMBIE_SNOW_VILLAGES.contains(chunk)) {
            decorators.add(new Erosion(world, pos, villageRand, chunks));
        }

        decorators.forEach(MesonOuterDecorator::generate);
        chunkRNG.remove(chunk);
    }

    @SubscribeEvent
    public static void onAddComponentParts(StructureEventBase.Post event) {
        if (event.getComponent() instanceof StructureVillagePieces.Village && !event.getWorld().isRemote) {
            World world = event.getWorld();
            StructureBoundingBox box = event.getBox();
            StructureVillagePieces.Village component = (StructureVillagePieces.Village)event.getComponent();
            VillageInnerDecorator decorator = null;
//            if (component.getClass() == StructureVillagePieces.Church.class) decorator = new VillageInnerDecorator.Church(component, world, box);
//            if (component.getClass() == StructureVillagePieces.Field2.class) decorator = new VillageInnerDecorator.Field1(component, world, box);
//            if (component.getClass() == StructureVillagePieces.Field1.class) decorator = new VillageInnerDecorator.Field2(component, world, box);
//            if (component.getClass() == StructureVillagePieces.Hall.class) decorator = new VillageInnerDecorator.Hall(component, world, box);
//            if (component.getClass() == StructureVillagePieces.House1.class) decorator = new VillageInnerDecorator.House1(component, world, box);
//            if (component.getClass() == StructureVillagePieces.House2.class) decorator = new VillageInnerDecorator.House2(component, world, box);

            if (component.getClass() == BetterSnowVillagePieces.SnowHouse3.class) decorator = new VillageInnerDecorator.House3(component, world, box);
            if (component.getClass() == BetterSnowVillagePieces.SnowHouse4.class) decorator = new VillageInnerDecorator.House4(component, world, box);
            if (component.getClass() == BetterSnowVillagePieces.SnowWoodHut.class) decorator = new VillageInnerDecorator.WoodHut(component, world, box);

            if (component.getClass() == BetterSnowVillagePieces.SnowWell.class) decorator = new SnowWellCarpetRemover(component, world, box);

            if (decorator != null) {
                if (decorator.isZombieInfested()) {
                    ZOMBIE_SNOW_VILLAGES.add(decorator.getChunkPos());
                }
                decorator.generate();
            }
        }

    }
}
