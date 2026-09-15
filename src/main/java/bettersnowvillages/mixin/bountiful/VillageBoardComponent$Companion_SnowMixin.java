package bettersnowvillages.mixin.bountiful;

import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.world.gen.structure.MapGenBetterSnowVillage;
import com.llamalad7.mixinextras.sugar.Local;
import ejektaflex.bountiful.worldgen.VillageBoardComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(VillageBoardComponent.Companion.class)
public abstract class VillageBoardComponent$Companion_SnowMixin {

    @ModifyArgs(
            method = "buildComponent",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/structure/StructureBoundingBox;getComponentToAddBoundingBox(IIIIIIIIILnet/minecraft/util/EnumFacing;)Lnet/minecraft/world/gen/structure/StructureBoundingBox;")
    )
    private void betterSnowVillages$bountifulVillageBoardComponent$Companion_buildComponentResizeOld(Args args, @Local(argsOnly = true) StructureVillagePieces.Start startPiece, @Local(name = "x") int x, @Local(name = "z") int z){
        if(ModLoadedUtil.versionInRange(ModLoadedUtil.BOUNTIFUL, ModLoadedUtil.BOUNTIFUL_BEFORELATEST) && MapGenBetterSnowVillage.isComponentOfSnowVillage(startPiece)) {
            args.set(6, 7);
            args.set(7, 6);
            args.set(8, 7);
        }
    }
}
