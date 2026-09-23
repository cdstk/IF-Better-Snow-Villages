package bettersnowvillages.compat.iceandfire;

import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.config.ForgeConfigHandler;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.IDreadBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class INFRLCHandler {

    public static boolean isBlockInsideMausoleum(World world, BlockPos pos) {
        return IDreadBlock.isBlockInsideMausoleum(world, pos);
    }

    public static boolean getSnowVillageGenConfig() {
        return IceAndFireConfig.WORLDGEN.generateSnowVillages;
    }

    public static int getSnowVillageChanceConfig() {
        return IceAndFireConfig.WORLDGEN.generateSnowVillageChance;
    }

    public static int[] getSnowVillageDimBlacklistConfig() {
        return IceAndFireConfig.WORLDGEN.snowVillageWhitelist
                ? IceAndFireForksUtil.EMPTY_ARRAY
                : IceAndFireConfig.WORLDGEN.snowVillageBlacklistedDimensions;
    }

    public static int[] getSnowVillageDimWhitelistConfig() {
        return IceAndFireConfig.WORLDGEN.snowVillageWhitelist
                ? IceAndFireConfig.WORLDGEN.snowVillageBlacklistedDimensions
                : IceAndFireForksUtil.EMPTY_ARRAY;
    }

    public static int getSnowVillageMinimumDistance() {
        return IceAndFireConfig.WORLDGEN.worldGenDistance;
    }
}
