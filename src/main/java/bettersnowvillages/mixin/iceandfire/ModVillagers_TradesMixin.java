package bettersnowvillages.mixin.iceandfire;

import bettersnowvillages.registry.BSVTrades;
import com.github.alexthe666.iceandfire.core.ModVillagers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ModVillagers.class)
public abstract class ModVillagers_TradesMixin {

    // Based on https://github.com/Krutoy242/Enigmatica2Expert-Extended/blob/master/scripts/mixin/iceandfire.zs

    @WrapOperation(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;addTrade(I[Lnet/minecraft/entity/passive/EntityVillager$ITradeList;)Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;addTrade(I[Lnet/minecraft/entity/passive/EntityVillager$ITradeList;)Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;addTrade(I[Lnet/minecraft/entity/passive/EntityVillager$ITradeList;)Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;", ordinal = 24)),
            remap = false
    )
    private VillagerRegistry.VillagerCareer betterSnowVillages_iceAndFireIafVillagerRegistry_initClearTrades(VillagerRegistry.VillagerCareer instance, int i, EntityVillager.ITradeList[] level, Operation<VillagerRegistry.VillagerCareer> original){
        return instance;
    }

    @WrapOperation(
            method = "init",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;addTrade(I[Lnet/minecraft/entity/passive/EntityVillager$ITradeList;)Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;addTrade(I[Lnet/minecraft/entity/passive/EntityVillager$ITradeList;)Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;", ordinal = 26)),
            remap = false
    )
    private VillagerRegistry.VillagerCareer betterSnowVillages_iceAndFireIafVillagerRegistry_initClearTrades1(VillagerRegistry.VillagerCareer instance, int i, EntityVillager.ITradeList[] level, Operation<VillagerRegistry.VillagerCareer> original){
        return instance;
    }

    @WrapOperation(
            method = "init",
            at = @At(value = "NEW", target = "(Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerProfession;Ljava/lang/String;)Lnet/minecraftforge/fml/common/registry/VillagerRegistry$VillagerCareer;"),
            remap = false
    )
    private VillagerRegistry.VillagerCareer betterSnowVillages_iceAndFireIafVillagerRegistry_initTradesConfig(VillagerRegistry.VillagerProfession parent, String name, Operation<VillagerRegistry.VillagerCareer> original){
        VillagerRegistry.VillagerCareer career = original.call(parent, name);

        return BSVTrades.initTrades(career, name);
    }
}
