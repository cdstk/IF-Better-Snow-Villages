package bettersnowvillages.mixin.vanilla;

import bettersnowvillages.world.gen.structure.VillageNBTComponent;
import bettersnowvillages.world.gen.structure.VillageNBTPieceWeight;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(StructureVillagePieces.class)
public abstract class StructureVillagePieces_NBTComponentMixin {

    @Inject(
            method = "findAndCreateComponentFactory",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void betterSnowVillages_vanillaStructureVillagePieces_findAndCreateComponentFactoryStructureNBT(StructureVillagePieces.Start start, StructureVillagePieces.PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType, CallbackInfoReturnable<StructureVillagePieces.Village> cir){
        if(weight instanceof VillageNBTPieceWeight) {
            VillageNBTPieceWeight nbtPiece = (VillageNBTPieceWeight) weight;
            StructureBoundingBox boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(
                    structureMinX, structureMinY, structureMinZ,
                    0, 0, 0,
                    nbtPiece.bbXMax, nbtPiece.bbYMax, nbtPiece.bbZMax,
                    facing
            );

            VillageNBTComponent.alignBoundingBox(nbtPiece, boundingBox, facing);

            if (boundingBox.minY > 10) {
                if (StructureComponent.findIntersecting(structureComponents, boundingBox) == null) {
                    cir.setReturnValue(new VillageNBTComponent(nbtPiece.resourceLocation, nbtPiece, start, componentType, boundingBox, facing));
                }
            }
        }
    }
}
