package bettersnowvillages.mixin.recurrentcomplex;

import bettersnowvillages.handlers.ForgeConfigHandler;
import bettersnowvillages.handlers.ForgeConfigProvider;
import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import ivorius.reccomplex.world.gen.feature.villages.TemporaryVillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;
import java.util.function.Consumer;

@Mixin(TemporaryVillagerRegistry.class)
public abstract class TemporaryVillagerRegistry_DynamicComponentsMixin {

    @Shadow(remap = false)
    protected Set<VillagerRegistry.IVillageCreationHandler> registeredHandlers;

    @WrapOperation(
            method = "setHandlers",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Sets$SetView;forEach(Ljava/util/function/Consumer;)V", ordinal = 0),
            remap = false
    )
    private void betterSnowVillages$recurrentComplexTemporaryVillagerRegistry_setHandlersCache(Sets.SetView<VillagerRegistry.IVillageCreationHandler> instance, Consumer<TemporaryVillagerRegistry> consumer, Operation<Void> original){
        if(!ForgeConfigHandler.betterSVGen.recurrentVillageForceCache)
            original.call(instance, consumer);
    }

    @Inject(
            method = "setHandlers",
            at = @At("TAIL"),
            remap = false
    )
    private void betterSnowVillages$recurrentComplexTemporaryVillagerRegistry_setHandlersRead(Set<VillagerRegistry.IVillageCreationHandler> handlers, CallbackInfo ci){
        if(!ForgeConfigHandler.betterSVGen.recurrentVillageReadCache)
            this.registeredHandlers.forEach(handler -> ForgeConfigProvider.testLateDynamicComponent(handler.getComponentClass()));
    }
}
