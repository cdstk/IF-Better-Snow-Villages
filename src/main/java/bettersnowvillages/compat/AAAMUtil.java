package bettersnowvillages.compat;

import antiqueatlasautomarker.config.AutoMarkSetting;
import antiqueatlasautomarker.structuremarkers.StructureMarkersDataHandler;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;

public class AAAMUtil {

    public static void autoMarkStructureStart(StructureStart structureStart, World world){
        AutoMarkSetting setting = AutoMarkSetting.get(MapGenStructureIO.getStructureStartName(structureStart));
        if(setting != null && setting.enabled) {
            StructureBoundingBox box = structureStart.getBoundingBox();
            StructureMarkersDataHandler.markStructure(world, (box.maxX + box.minX) / 2, (box.maxZ + box.minZ) / 2, setting);
        }
    }
}
