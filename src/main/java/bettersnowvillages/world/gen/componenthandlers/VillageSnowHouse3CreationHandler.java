package bettersnowvillages.world.gen.componenthandlers;

import bettersnowvillages.world.gen.structure.BetterSnowVillagePieces;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class VillageSnowHouse3CreationHandler extends AbstractSnowVillageCreationHandler {

    @Override
    public Class<? extends StructureVillagePieces.Village> getComponentClass() {
        return BetterSnowVillagePieces.SnowHouse3.class;
    }

    @Override
    @Nullable
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z, EnumFacing facing, int type) {
        return BetterSnowVillagePieces.SnowHouse3.createPiece(startPiece, pieces, random, x, y, z, facing, type);
    }
}
