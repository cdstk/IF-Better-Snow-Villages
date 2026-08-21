package bettersnowvillages.world.gen.componenthandlers;

import bettersnowvillages.world.gen.structure.BetterSnowVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class VillageSnowWellCreationHandler extends AbstractSnowVillageCreationHandler {

    @Override
    public Class<? extends StructureVillagePieces.Village> getComponentClass() {
        return BetterSnowVillagePieces.SnowWell.class;
    }
}
