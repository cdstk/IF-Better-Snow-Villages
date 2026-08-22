package bettersnowvillages.mixin.iceandfire;

import bettersnowvillages.compat.BountifulUtil;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.bountiful.worldgen.worldgen.ComponentSnowVillageBountyBoard;
import bettersnowvillages.compat.waystones.worldgen.ComponentSnowVillageWaystone;
import bettersnowvillages.handlers.ForgeConfigHandler;
import com.github.alexthe666.iceandfire.world.village.SnowVillagePieces;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.waystones.WaystoneConfig;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(SnowVillagePieces.class)
public abstract class SnowVillagePieces_AddonMixin {

    @Inject(
            method = "getStructureVillageWeightedPieceList",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1),
            remap = false
    )
    private static void betterSnowVillages_iceAndFireSnowVillagePieces_getStructureVillageWeightedPieceListCustom(Random random, int size, CallbackInfoReturnable<List<SnowVillagePieces.PieceWeight>> cir, @Local List<SnowVillagePieces.PieceWeight> list){
        if(ForgeConfigHandler.ifSVGen.baseGenBountyBoard && ModLoadedUtil.BOUNTIFUL.isLoaded()) {
            list.add(new SnowVillagePieces.PieceWeight(
                    ComponentSnowVillageBountyBoard.class,
                    3,
                    random.nextFloat() <= 0.73f && BountifulUtil.getVillageGenerationConfig()
                            ? 1
                            : 0
            ));
        }
        if(ForgeConfigHandler.ifSVGen.baseGenWaystone && ModLoadedUtil.WAYSTONES.isLoaded()) {
            list.add(new SnowVillagePieces.PieceWeight(
                    ComponentSnowVillageWaystone.class,
                    3,
                    random.nextFloat() > WaystoneConfig.worldGen.villageChance
                            ? 0
                            : 1
            ));
        }
    }

    @WrapMethod(
            method = "findAndCreateComponentFactory",
            remap = false
    )
    private static SnowVillagePieces.Village betterSnowVillages_iceAndFireSnowVillagePieces_findAndCreateComponentFactoryCustom(SnowVillagePieces.Start start, SnowVillagePieces.PieceWeight weight, List<StructureComponent> structureComponents, Random rand, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType, Operation<SnowVillagePieces.Village> original){
        Class<? extends SnowVillagePieces.Village> oclass = weight.villagePieceClass;

        if(oclass == SnowVillagePieces.WoodHut.class) {
            return SnowVillagePieces.WoodHut.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        if(oclass == SnowVillagePieces.TorchNew.class) {
            return SnowVillagePieces.TorchNew.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        if(ModLoadedUtil.BOUNTIFUL.isLoaded() && oclass == ComponentSnowVillageBountyBoard.class) {
            return ComponentSnowVillageBountyBoard.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }
        if(ModLoadedUtil.WAYSTONES.isLoaded() && oclass == ComponentSnowVillageWaystone.class) {
            return ComponentSnowVillageWaystone.createPiece(start, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
        }

        return original.call(start, weight, structureComponents, rand, structureMinX, structureMinY, structureMinZ, facing, componentType);
    }
}
