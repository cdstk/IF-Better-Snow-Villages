package bettersnowvillages.compat.iceandfire.wrapper;

import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import net.minecraft.entity.passive.EntityVillager;

import java.util.Random;

public class IaFVillagerRegistryWrapper {

    public static void setRandomSnowProfession(EntityVillager villager, Random rand) {
        villager.setProfession(IafVillagerRegistry.INSTANCE.professions.get(rand.nextInt(IafVillagerRegistry.INSTANCE.professions.size())));
    }
}
