package bettersnowvillages.mixin.recurrentcomplex;

import bettersnowvillages.handlers.ForgeConfigProvider;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import ivorius.reccomplex.world.gen.feature.villages.GenericVillagePiece;
import ivorius.reccomplex.world.gen.feature.villages.VanillaGenerationClassFactory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VanillaGenerationClassFactory.class)
public abstract class VanillaGenerationClassFactory_LateVillageMixin {

    @WrapMethod(
            method = "createClass",
            remap = false
    )
    private Class<? extends GenericVillagePiece> betterSnowVillages$recurrentComplexVanillaGenerationClassFactory_createClassReadDynamic(String structureID, String generationID, Operation<Class<? extends GenericVillagePiece>> original){
        Class<? extends GenericVillagePiece> clazz = original.call(structureID, generationID);

        if(clazz != null)
            ForgeConfigProvider.testLateDynamicComponent(clazz);

        return clazz;
    }
}
