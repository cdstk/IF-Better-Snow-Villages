package bettersnowvillages.mixin.iceandfire.aaam;

import bettersnowvillages.compat.AAAMUtil;
import com.github.alexthe666.iceandfire.world.village.MapGenSnowVillage;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = MapGenSnowVillage.Start.class)
public abstract class MapGenSnowVillage$Start_AAAMMixin extends StructureStart {

    @Inject(
            method = "<init>(Lnet/minecraft/world/World;Ljava/util/Random;III)V",
            at = @At("TAIL"),
            remap = false
    )
    private void betterSnowVillages_iceAndFireMapGenSnowVillage$Start_generateAAAM(World worldIn, Random rand, int x, int z, int size, CallbackInfo ci){
        AAAMUtil.autoMarkStructureStart(this, worldIn);
    }
}
