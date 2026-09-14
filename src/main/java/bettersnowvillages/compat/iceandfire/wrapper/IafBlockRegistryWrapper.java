package bettersnowvillages.compat.iceandfire.wrapper;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.block.Block;

public class IafBlockRegistryWrapper {

    public static Block getFrozenCobblestone() {
        return IafBlockRegistry.frozenCobblestone;
    }

    public static Block getFrozenGravel() {
        return IafBlockRegistry.frozenGravel;
    }

    public static Block getFrozenGrassPath() {
        return IafBlockRegistry.frozenGrassPath;
    }

    public static Block getDragonIceSpikes() {
        return IafBlockRegistry.dragon_ice_spikes;
    }
}
