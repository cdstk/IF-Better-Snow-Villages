package bettersnowvillages.compat;

import bettersnowvillages.config.ForgeConfigHandler;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.block.IDreadBlock;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IceAndFireForksUtil {

    public static boolean isBlockInsideMausoleum(World world, BlockPos pos) {
        return IDreadBlock.isBlockInsideMausoleum(world, pos);
    }

    // Was going to make it a dread mob but that still laggy af
    public static EntityLiving getInfestedSnowVillageMob(World world) {
        return new EntityZombieVillager(world);
    }

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
