package bettersnowvillages.mixin.waystones;

import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.QuarkUtil;
import bettersnowvillages.compat.waystones.worldgen.ComponentSnowVillageWaystone;
import bettersnowvillages.world.gen.structure.WorldGenBetterSnowVillage;
import com.llamalad7.mixinextras.sugar.Local;
import net.blay09.mods.waystones.worldgen.ComponentVillageWaystone;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.BiomeDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Set;

@Mixin(ComponentVillageWaystone.class)
public abstract class ComponentVillageWaystone_SnowMixin extends StructureVillagePieces.Village {

    @ModifyArg(
            method = "addComponentParts",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/structure/template/TemplateManager;getTemplate(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/world/gen/structure/template/Template;"),
            index = 1
    )
    private ResourceLocation betterSnowVillages$waystonesComponentVillageWaystone_addComponentPartsSnow(ResourceLocation id, @Local(argsOnly = true) World world, @Local BlockPos pos){
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(world.getBiome(pos));
        if(WorldGenBetterSnowVillage.isBiomeTypesValid(types) && WorldGenBetterSnowVillage.isComponentOfSnowVillage(this.startPiece)) {
            if (ModLoadedUtil.QUARK.isLoaded() && QuarkUtil.isSnowBrickEnabled()) {
                return types.contains(BiomeDictionary.Type.FOREST)
                        ? ComponentSnowVillageWaystone.VANILlA_WAYSTONE_ID
                        : ComponentSnowVillageWaystone.QUARK_WAYSTONE_ID;
            }
            else {
                return  ComponentSnowVillageWaystone.VANILlA_WAYSTONE_ID;
            }
        }
        return id;
    }
}
