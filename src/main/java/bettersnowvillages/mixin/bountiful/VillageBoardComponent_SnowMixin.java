package bettersnowvillages.mixin.bountiful;

import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.QuarkUtil;
import bettersnowvillages.compat.bountiful.worldgen.worldgen.ComponentSnowVillageBountyBoard;
import com.llamalad7.mixinextras.sugar.Local;
import ejektaflex.bountiful.worldgen.VillageBoardComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Set;

@Mixin(VillageBoardComponent.class)
public abstract class VillageBoardComponent_SnowMixin {

    @ModifyArg(
            method = "addComponentParts",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/structure/template/TemplateManager;getTemplate(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/world/gen/structure/template/Template;"),
            index = 1
    )
    private ResourceLocation betterSnowVillages$bountifulVillageBoardComponent_addComponentPartsSnow(ResourceLocation id, @Local(argsOnly = true) World world, @Local BlockPos pos){
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(world.getBiome(pos));
        if(types.contains(BiomeDictionary.Type.COLD) && types.contains(BiomeDictionary.Type.SNOWY)) {
            if (ModLoadedUtil.QUARK.isLoaded() && QuarkUtil.isSnowBrickEnabled()) {
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
