package bettersnowvillages.world.gen.componenthandlers;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class AbstractSnowVillageCreationHandler implements VillagerRegistry.IVillageCreationHandler {

    /** Prevent generating in Vanilla Villages, manage base piece weights in Structure Generator **/
    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int size) {
        return new StructureVillagePieces.PieceWeight(this.getComponentClass(), 0, 0);
    }

    @Override
    public abstract Class<? extends StructureVillagePieces.Village> getComponentClass();

    /** Wells and Roads have builtin generators, else anything else needs to implement one **/
    @Override
    @Nullable
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z, EnumFacing facing, int type) {
        return null;
    }
}
