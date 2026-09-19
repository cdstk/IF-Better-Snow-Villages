package bettersnowvillages.mixin.vanilla;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StructureComponent.class)
public abstract class StructureComponent_SnowLayerMixin {

    @WrapOperation(
            method = "replaceAirAndLiquidDownwards",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAirBlock(Lnet/minecraft/util/math/BlockPos;)Z")
    )
    private boolean betterSnowVillages_vanillaStructureComponent_replaceAirAndLiquidDownwardsSnowLayer(World world, BlockPos pos, Operation<Boolean> original){
        boolean isAirBlock = original.call(world, pos);
        if(!isAirBlock) {
            if((Object)this instanceof StructureVillagePieces.Village) {
                return  world.getBlockState(pos).getBlock() == Blocks.SNOW_LAYER;
            }
        }
        return isAirBlock;
    }
}
