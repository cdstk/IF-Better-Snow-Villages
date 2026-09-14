package bettersnowvillages.compat;

import bettersnowvillages.compat.iceandfire.BaseConfig;
import bettersnowvillages.compat.iceandfire.INFRLCHandler;
import bettersnowvillages.compat.iceandfire.wrapper.IafBlockRegistryWrapper;
import bettersnowvillages.compat.iceandfire.wrapper.ModBlocksWrapper;
import bettersnowvillages.config.ForgeConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IceAndFireForksUtil {

    public static final int[] EMPTY_ARRAY = new int[0];

    private static Boolean isModernRegistry = null;
    private static Boolean mausoleumAntiGrief = null;

    public static boolean getModernRegistry() {
        if(isModernRegistry == null) {
            isModernRegistry = ModLoadedUtil.versionInRange(ModLoadedUtil.ICEANDFIRE, "[1.9.1,)");
        }
        return isModernRegistry;
    }

    // Was going to make it a dread mob but that still laggy af
    public static EntityLiving getInfestedSnowVillageMob(World world) {
        return new EntityZombieVillager(world);
    }

    public static boolean isBlockInsideMausoleum(World world, BlockPos pos) {
        if (ModLoadedUtil.ICEANDFIRE.fork == ModLoadedUtil.INFLoadedContainer.FORK.RLCRAFT) {
            if(mausoleumAntiGrief == null) {
                mausoleumAntiGrief = ModLoadedUtil.versionInRange(ModLoadedUtil.ICEANDFIRE, "[2.1.0,)");
            }
            return mausoleumAntiGrief && INFRLCHandler.isBlockInsideMausoleum(world, pos);
        }
        return false;
    }

    public static boolean getSnowVillageGenConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            if (ModLoadedUtil.ICEANDFIRE.fork == ModLoadedUtil.INFLoadedContainer.FORK.RLCRAFT) {
                return INFRLCHandler.getSnowVillageGenConfig();
            }
            return BaseConfig.getSnowVillageGenConfig();
        }
        else {
            return ForgeConfigHandler.betterSVGen.genBetterSV;
        }
    }

    public static int getSnowVillageChanceConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            if (ModLoadedUtil.ICEANDFIRE.fork == ModLoadedUtil.INFLoadedContainer.FORK.RLCRAFT) {
                return INFRLCHandler.getSnowVillageChanceConfig();
            }
            return BaseConfig.getSnowVillageChanceConfig();
        }
        else {
            return ForgeConfigHandler.betterSVGen.genBetterSVChance;
        }
    }

    public static int[] getSnowVillageDimBlacklistConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            switch (ModLoadedUtil.ICEANDFIRE.fork) {
                case RLCRAFT: return INFRLCHandler.getSnowVillageDimBlacklistConfig();
                case ROTN: return EMPTY_ARRAY;
            }
            return BaseConfig.getSnowVillageDimBlacklistConfig();
        }
        else {
            return ForgeConfigHandler.betterSVGen.betterSVWhitelist
                    ? EMPTY_ARRAY
                    : ForgeConfigHandler.betterSVGen.betterSVBlacklistedDimensions;
        }
    }

    public static int[] getSnowVillageDimWhitelistConfig(boolean useVanillaIF) {
        if(useVanillaIF) {
            switch (ModLoadedUtil.ICEANDFIRE.fork) {
                case RLCRAFT: return INFRLCHandler.getSnowVillageDimWhitelistConfig();
                case ROTN: return EMPTY_ARRAY;
            }
            return BaseConfig.getSnowVillageDimWhitelistConfig();
        }
        else {
            return ForgeConfigHandler.betterSVGen.betterSVWhitelist
                    ? ForgeConfigHandler.betterSVGen.betterSVBlacklistedDimensions
                    : EMPTY_ARRAY;
        }
    }

    public static int getSnowVillageMinimumDistance(boolean useVanillaIF) {
        if(useVanillaIF) {
            if (ModLoadedUtil.ICEANDFIRE.fork == ModLoadedUtil.INFLoadedContainer.FORK.RLCRAFT) {
                return INFRLCHandler.getSnowVillageMinimumDistance();
            }
            return 0;
        }
        else {
            return ForgeConfigHandler.betterSVGen.betterSVMinDist;
        }
    }

    public static Block getFrozenCobblestone() {
        return getModernRegistry()
                ? IafBlockRegistryWrapper.getFrozenCobblestone()
                : ModBlocksWrapper.getFrozenCobblestone();
    }

    public static Block getFrozenGravel() {
        return getModernRegistry()
                ? IafBlockRegistryWrapper.getFrozenGravel()
                : ModBlocksWrapper.getFrozenGravel();
    }

    public static Block getFrozenGrassPath() {
        return getModernRegistry()
                ? IafBlockRegistryWrapper.getFrozenGrassPath()
                : ModBlocksWrapper.getFrozenGrassPath();
    }

    public static Block getDragonIceSpikes() {
        return getModernRegistry()
                ? IafBlockRegistryWrapper.getDragonIceSpikes()
                : ModBlocksWrapper.getDragonIceSpikes();
    }
}
