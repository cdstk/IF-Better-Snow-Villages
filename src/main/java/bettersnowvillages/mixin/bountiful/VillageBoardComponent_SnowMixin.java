package bettersnowvillages.mixin.bountiful;

import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.QuarkUtil;
import bettersnowvillages.compat.bountiful.worldgen.worldgen.ComponentSnowVillageBountyBoard;
import bettersnowvillages.mixin.vanilla.StructureVillagePieces$Village_AccessorMixin;
import bettersnowvillages.world.gen.structure.MapGenBetterSnowVillage;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import ejektaflex.bountiful.worldgen.VillageBoardComponent;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.common.BiomeDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(VillageBoardComponent.class)
public abstract class VillageBoardComponent_SnowMixin {

    // v2.2.2, does 2.2.3 offset and rotation
    @Unique
    private Rotation betterSnowVillages$boardRotation = Rotation.NONE;
    @Unique
    private BlockPos betterSnowVillages$boardShift = new BlockPos(0,0,0);

    @Inject(
            method = "<init>(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Start;ILnet/minecraft/world/gen/structure/StructureBoundingBox;Lnet/minecraft/util/EnumFacing;)V", remap = false,
            at = @At("TAIL")
    )
    private void betterSnowVillages$bountifulVillageBoardComponent_initOldSnow(StructureVillagePieces.Start start, int type, StructureBoundingBox boundingBox, EnumFacing facing, CallbackInfo ci){
        if(ModLoadedUtil.versionInRange(ModLoadedUtil.BOUNTIFUL, ModLoadedUtil.BOUNTIFUL_BEFORELATEST)) {
            //Template rotation and position tweaking
            if (facing != null) {
                switch (facing) {
                    case SOUTH:
                        this.betterSnowVillages$boardRotation = Rotation.CLOCKWISE_180;
                        this.betterSnowVillages$boardShift = new BlockPos(6,0,6);
                        break;
                    case WEST:
                        this.betterSnowVillages$boardRotation = Rotation.COUNTERCLOCKWISE_90;
                        this.betterSnowVillages$boardShift = new BlockPos(0,0,6);
                        break;
                    case EAST:
                        this.betterSnowVillages$boardRotation = Rotation.CLOCKWISE_90;
                        this.betterSnowVillages$boardShift = new BlockPos(6,0,0);
                        break;
                }
            }
        }
    }

    @ModifyArg(
            method = "addComponentParts",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/structure/template/TemplateManager;getTemplate(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/world/gen/structure/template/Template;"),
            index = 1
    )
    private ResourceLocation betterSnowVillages$bountifulVillageBoardComponent_addComponentPartsSnow(ResourceLocation id, @Local(argsOnly = true) World world, @Local PlacementSettings settings, @Local LocalRef<BlockPos> placePos){
        boolean isSnowVillage = false;
        BlockPos targetPos = placePos.get();
        if(this instanceof StructureVillagePieces$Village_AccessorMixin) { // BC idk Kotlin Mixin
            isSnowVillage = MapGenBetterSnowVillage.isComponentOfSnowVillage(((StructureVillagePieces$Village_AccessorMixin) this).betterSnowVillages$accessorStartPiece());
        }
        if(isSnowVillage && world.getBiomeProvider().areBiomesViable(targetPos.getX(), targetPos.getZ(), 0, MapGenBetterSnowVillage.SNOW_VILLAGE_SPAWN_BIOMES)) {
            if(ModLoadedUtil.versionInRange(ModLoadedUtil.BOUNTIFUL, ModLoadedUtil.BOUNTIFUL_BEFORELATEST)) {
                settings.setRotation(this.betterSnowVillages$boardRotation);
                placePos.set(targetPos.add(this.betterSnowVillages$boardShift));
            }
            if (ModLoadedUtil.QUARK.isLoaded() && QuarkUtil.isSnowBrickEnabled()) {
                Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(world.getBiome(targetPos));
                return types.contains(BiomeDictionary.Type.FOREST)
                        ? ComponentSnowVillageBountyBoard.VANILlA_BOARD_ID
                        : ComponentSnowVillageBountyBoard.QUARK_BOARD_ID;
            }
            else {
                return  ComponentSnowVillageBountyBoard.VANILlA_BOARD_ID;
            }
        }
        return id;
    }
}
