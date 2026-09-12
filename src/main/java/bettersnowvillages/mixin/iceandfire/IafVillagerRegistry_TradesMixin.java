package bettersnowvillages.mixin.iceandfire;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.config.ForgeConfigHandler;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(IafVillagerRegistry.class)
public abstract class IafVillagerRegistry_TradesMixin {

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

        switch (name) {
            case "fisherman": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.fishermanSnowVillagerTrades); break;
            case "craftsman": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.craftsmanSnowVillagerTrades); break;
            case "shaman": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.shamanSnowVillagerTrades); break;
            case "desert_myrmex_worker": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.desertMyrmexWorkerTrades); break;
            case "jungle_myrmex_worker": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.jungleMyrmexWorkerTrades); break;
            case "desert_myrmex_soldier": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.desertMyrmexSoldierTrades); break;
            case "jungle_myrmex_soldier": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.jungleMyrmexSoldierTrades); break;
            case "desert_myrmex_sentinel": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.desertMyrmexSentinelTrades); break;
            case "jungle_myrmex_sentinel": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.jungleMyrmexSentinelTrades); break;
            case "desert_myrmex_royal": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.desertMyrmexRoyalTrades); break;
            case "jungle_myrmex_royal": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.jungleMyrmexRoyalTrades); break;
            case "desert_myrmex_queen": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.desertMyrmexQueenTrades); break;
            case "jungle_myrmex_queen": betterSnowVillages$applyMyrmexConfig(career, ForgeConfigHandler.trades.jungleMyrmexQueenTrades); break;
        }

        return career;
    }

    @Unique
    private void betterSnowVillages$applyMyrmexConfig(VillagerRegistry.VillagerCareer career, String[] tradesConfig) {
        for(String line : tradesConfig) {
            String[] config = line.split(",");
            if(config.length < 7) continue;

            int level;
            int inMin;
            int inMax;
            int outMin;
            int outMax;

            try {
                level = Integer.parseInt(config[0].trim());
                inMin = Integer.parseInt(config[2].trim());
                inMax = Integer.parseInt(config[3].trim());
                outMin = Integer.parseInt(config[5].trim());
                outMax = Integer.parseInt(config[6].trim());
            } catch (NumberFormatException ignored) {
                BetterSnowVillages.LOGGER.log(Level.INFO, "Could not parse a number in Ice and Fire trade config: {}", line);
                continue;
            }

            this.betterSnowVillages$addMyrmexTrade(career, level, config[1].trim(), inMin, inMax, config[4].trim(), outMin, outMax);
        }
    }

    @Unique
    private void betterSnowVillages$addMyrmexTrade(VillagerRegistry.VillagerCareer career, int lvl, String inpId, int inpMin, int inpMax, String outId, int outMin, int outMax) {
        String[] inpL = inpId.split(":");
        Item inpItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(inpL[0].trim(), inpL[1].trim()));
        if(inpItem == null) {
            BetterSnowVillages.LOGGER.log(Level.INFO, "Skipping Ice and Fire trade. No input item: {}", inpId);
            return;
        }

        String[] outL = outId.split(":");
        Item outItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(outL[0].trim(), outL[1].trim()));
        if(outItem == null) {
            BetterSnowVillages.LOGGER.log(Level.INFO, "Skipping Ice and Fire trade. No output item: {}", outId);
            return;
        }

        int inpMeta = 0;
        int outMeta = 0;

        try {
            if(inpL.length > 2) inpMeta = Integer.parseInt(inpL[2].trim());
            if(outL.length > 2) outMeta = Integer.parseInt(outL[2].trim());
        } catch (NumberFormatException ignored) {}

        career.addTrade(lvl, new EntityMyrmexBase.BasicTrade(
                new ItemStack(inpItem, 1, inpMeta),
                new ItemStack(outItem, 1, outMeta),
                new EntityVillager.PriceInfo(inpMin, inpMax),
                new EntityVillager.PriceInfo(outMin, outMax)
        ));
//        BetterSnowVillages.LOGGER.log(Level.INFO, "Added Ice and Fire trade: ({}), {} [{}, {}] => {} [{}, {}]", career.getName(), inpId, inpMin, inpMax, outId, outMin, outMax);
    }
}
