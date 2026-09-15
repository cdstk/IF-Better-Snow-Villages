package bettersnowvillages.world.gen;

import bettersnowvillages.world.gen.structure.BetterSnowVillagePieces;
import bettersnowvillages.world.gen.structure.MapGenBetterSnowVillage;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BetterSnowVillagesChunkGenerator {

    public static final MapGenBetterSnowVillage BETTER_SNOW_VILLAGE = new MapGenBetterSnowVillage();

    public static void registerGenerator() {
        MinecraftForge.EVENT_BUS.register(BetterSnowVillagesChunkGenerator.class);
//        MinecraftForge.TERRAIN_GEN_BUS.register(BetterSnowVillagesChunkGenerator.class);
        BetterSnowVillagePieces.registerVillageComponents();
    }

    @SubscribeEvent
    public static void onChunkPopulatePre(PopulateChunkEvent.Pre event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        // Chance can't go here, creates broken gen
        BETTER_SNOW_VILLAGE.generate(world, event.getChunkX(), event.getChunkZ(), new ChunkPrimer());
    }

    @SubscribeEvent
    public static void onChunkPopulatePost(PopulateChunkEvent.Post event) {
        World world = event.getWorld();
        if (world.isRemote) return;

        // Don't AAAM here, is fired per chunk
        if(BETTER_SNOW_VILLAGE.generateStructure(world, event.getRand(), new ChunkPos(event.getChunkX(), event.getChunkZ()))) {

        }
    }
}
