package bettersnowvillages.mixin.vanilla;

import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureVillagePieces.Village.class)
public interface StructureVillagePieces$Village_AccessorMixin {

    @Accessor(value = "startPiece", remap = false)
    StructureVillagePieces.Start betterSnowVillages$accessorStartPiece();
}
