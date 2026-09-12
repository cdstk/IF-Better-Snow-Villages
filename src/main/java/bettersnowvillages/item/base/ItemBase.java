package bettersnowvillages.item.base;

import bettersnowvillages.BetterSnowVillages;
import net.minecraft.item.Item;

public abstract class ItemBase extends Item {

    public ItemBase(String modid, String name) {
        super();
        this.setRegistryName(modid, name);
        this.setTranslationKey(modid + "." + name);
    }

    public ItemBase(String name) {
        this(BetterSnowVillages.MODID, name);
    }
}
