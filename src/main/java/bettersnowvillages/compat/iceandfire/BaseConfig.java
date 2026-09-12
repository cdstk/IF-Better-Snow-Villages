package bettersnowvillages.compat.iceandfire;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.config.ForgeConfigHandler;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class BaseConfig {

    // I don't like Reflection in this case, but it has to be done for simplicity

    private final static BaseConfig ACCESS = create();

    private final IceAndFireConfig config;

    // Used by all
    private final Field generateSnowVillages;
    private final Field generateSnowVillageChance;
    // Exact fields from older InF, was removed in latest base and rotn
    private final Field snowVillageBlacklistedDimensions;
    private final Field snowVillageWhitelistedDimensions;

    private BaseConfig() throws ReflectiveOperationException {

        Field instance = ObfuscationReflectionHelper.findField(IceAndFire.class, "CONFIG");
        instance.setAccessible(true);

        config = (IceAndFireConfig) instance.get(null);
        generateSnowVillages = ObfuscationReflectionHelper.findField(IceAndFireConfig.class, "generateSnowVillages");
        generateSnowVillageChance = ObfuscationReflectionHelper.findField(IceAndFireConfig.class, "generateSnowVillageChance");

        Field dimensions = null;
        try {
            dimensions = ObfuscationReflectionHelper.findField(IceAndFireConfig.class, "snowVillageBlacklistedDimensions");
        }
        catch (ReflectionHelper.UnableToFindFieldException ignored) {}
        snowVillageBlacklistedDimensions = dimensions;

        try {
            dimensions = ObfuscationReflectionHelper.findField(IceAndFireConfig.class, "snowVillageWhitelistedDimensions");
        }
        catch (ReflectionHelper.UnableToFindFieldException ignored) {}
        snowVillageWhitelistedDimensions = dimensions;
    }

    private static BaseConfig create() {
        try {
            return new BaseConfig();
        } catch (ReflectiveOperationException | RuntimeException exception) {
            BetterSnowVillages.LOGGER.warn("Failed to read Ice and Fire's config, using Better Snow Villages config instead.", exception);
            return null;
        }
    }

    public static boolean getSnowVillageGenConfig() {
        try {
            return (boolean) ACCESS.generateSnowVillages.get(ACCESS.config);
        }
        catch (IllegalAccessException e) {
            return ForgeConfigHandler.betterSVGen.genBetterSV;
        }
    }

    public static int getSnowVillageChanceConfig() {
        try {
            return (int) ACCESS.generateSnowVillageChance.get(ACCESS.config);
        }
        catch (IllegalAccessException e) {
            return ForgeConfigHandler.betterSVGen.genBetterSVChance;
        }
    }

    public static int[] getSnowVillageDimBlacklistConfig() {
        if(ACCESS.snowVillageBlacklistedDimensions == null)
            return IceAndFireForksUtil.EMPTY_ARRAY;

        try {
            return (int[]) ACCESS.snowVillageBlacklistedDimensions.get(ACCESS.config);
        }
        catch (IllegalAccessException e) {
            return IceAndFireForksUtil.EMPTY_ARRAY;
        }
    }

    public static int[] getSnowVillageDimWhitelistConfig() {
        if(ACCESS.snowVillageWhitelistedDimensions == null)
            return IceAndFireForksUtil.EMPTY_ARRAY;

        try {
            return (int[]) ACCESS.snowVillageWhitelistedDimensions.get(ACCESS.config);
        }
        catch (IllegalAccessException e) {
            return ForgeConfigHandler.betterSVGen.betterSVBlacklistedDimensions;
        }
    }
}
