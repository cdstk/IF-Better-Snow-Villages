package bettersnowvillages.compat;

import bettersnowvillages.handlers.ForgeConfigHandler;
import com.github.alexthe666.iceandfire.IceAndFireConfig;

public class IceAndFireForksUtil {

    public static boolean getSnowVillageGenConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            return IceAndFireConfig.WORLDGEN.generateSnowVillages;
        }
        else {
            return ForgeConfigHandler.betterSVGen.genBetterSV;
        }
    }

    public static int getSnowVillageChanceConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            return IceAndFireConfig.WORLDGEN.generateSnowVillageChance;
        }
        else {
            return ForgeConfigHandler.betterSVGen.genBetterSVChance;
        }
    }

    public static int[] getSnowVillageDimensionsConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            return IceAndFireConfig.WORLDGEN.snowVillageBlacklistedDimensions;
        }
        else {
            return ForgeConfigHandler.betterSVGen.betterSVBlacklistedDimensions;
        }
    }

    public static boolean getSnowVillageDimWhitelistConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            return IceAndFireConfig.WORLDGEN.snowVillageWhitelist;
        }
        else {
            return ForgeConfigHandler.betterSVGen.betterSVWhitelist;
        }
    }

    public static int getSnowVillageMinimumDistance(boolean useVanillaIF) {
        if(useVanillaIF) {
            return IceAndFireConfig.WORLDGEN.worldGenDistance;
        }
        else {
            return ForgeConfigHandler.betterSVGen.betterSVMinDist;
        }
    }
}
