package bettersnowvillages.compat;

import antiqueatlasautomarker.api.StructureMarkersAPI;
import bettersnowvillages.config.ForgeConfigHandler;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

public class AAAMUtil {

    public static boolean hasAPI() {
        return ModLoadedUtil.versionInRange(ModLoadedUtil.AAAM, "[1.6.0,)");
    }

    public static void markNormalSnowVillage(StructureStart structureStart, World world) {
        String[] config = ForgeConfigHandler.ifSVGen.aaamNormal.split(";");
        if(config[0].trim().isEmpty()) return;

        String label = config.length < 2 || config[1].trim().equals("DEFAULT") ? "SnowVillage" : config[1].trim();

        StructureBoundingBox box = structureStart.getBoundingBox();
        StructureMarkersAPI.markStructure(
                world,
                (box.maxX + box.minX)/2, (box.maxZ + box.minZ) / 2,
                config[0].trim(), label, ""
        );
    }

    public static void markBetterSnowVillage(StructureStart structureStart, World world, String structureName) {
        String[] config = ForgeConfigHandler.ifSVGen.aaamBSV.split(";");
        if(config[0].isEmpty()) return;

        String label = config.length < 2 || config[1].trim().equals("DEFAULT") ? structureName : config[1].trim();
        if ("DEFAULT".equals(label)) label = structureName;

        StructureBoundingBox box = structureStart.getBoundingBox();
        StructureMarkersAPI.markStructure(
                world,
                (box.maxX + box.minX)/2, (box.maxZ + box.minZ) / 2,
                config[0].trim(), label, ""
        );
    }
}
