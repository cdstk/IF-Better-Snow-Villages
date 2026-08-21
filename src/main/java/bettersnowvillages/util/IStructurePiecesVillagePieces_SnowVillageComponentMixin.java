package bettersnowvillages.util;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public interface IStructurePiecesVillagePieces_SnowVillageComponentMixin {

    void bettersnowvillages$spawnSnowVillagers(World worldIn, StructureBoundingBox structurebb, int x, int y, int z, int count);
}
