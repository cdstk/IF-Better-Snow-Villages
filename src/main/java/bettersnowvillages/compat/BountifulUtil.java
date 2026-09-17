package bettersnowvillages.compat;

import ejektaflex.bountiful.Bountiful;

public class BountifulUtil {

    public static boolean getVillageGenerationConfig(){
        return Bountiful.config.getVillageGeneration();
    }

    public static float getVillageGenerationChance(){
        if(ModLoadedUtil.versionInRange(ModLoadedUtil.BOUNTIFUL, ModLoadedUtil.BOUNTIFUL_LATEST)) {
            return (float) Bountiful.config.getVillageGenerationWeight();
        }
        return 0.73f;
    }
}
