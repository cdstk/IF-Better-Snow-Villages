package bettersnowvillages.compat.iceandfire.wrapper;

import com.github.alexthe666.iceandfire.core.ModVillagers;
import com.github.alexthe666.iceandfire.entity.EntitySnowVillager;

import java.util.Random;

public class ModVillagersWrapper {

    public static void setRandomSnowProfession(EntitySnowVillager villager, Random rand) {
        villager.setProfession(ModVillagers.INSTANCE.professions.get(rand.nextInt(ModVillagers.INSTANCE.professions.size())));
    }
}
