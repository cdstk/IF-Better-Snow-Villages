package bettersnowvillages.compat;

import ivorius.reccomplex.world.gen.feature.villages.VanillaGenerationClassFactory;

public class RecurrentComplexUtil {

    public static ClassLoader getVanillaGenerationClassFactor() {
        return VanillaGenerationClassFactory.instance();
    }
}
