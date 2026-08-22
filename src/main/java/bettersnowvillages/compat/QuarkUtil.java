package bettersnowvillages.compat;

import vazkii.quark.building.feature.SnowBricks;

public class QuarkUtil {

    public static boolean isSnowBrickEnabled() {
        return SnowBricks.enableWalls && SnowBricks.enableStairsAndSlabs && SnowBricks.snow_bricks != null;
    }
}
