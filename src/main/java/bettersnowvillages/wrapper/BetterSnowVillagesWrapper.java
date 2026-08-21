package bettersnowvillages.wrapper;

import com.github.alexthe666.iceandfire.entity.EntityDreadGhoul;
import com.github.alexthe666.iceandfire.entity.EntitySnowVillager;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;

import java.util.Random;

public class BetterSnowVillagesWrapper {

    public static EntityLiving newInfestedMobInstance(World world) {
        return new EntityDreadGhoul(world);
    }

    public static EntityVillager newSnowVillagerInstance(World world) {
        return new EntitySnowVillager(world);
    }

    public static void setRandomSnowProfession(EntityVillager villager, Random rand) {
        if(villager instanceof EntitySnowVillager)
            IafVillagerRegistry.INSTANCE.setRandomProfession((EntitySnowVillager) villager, rand);
    }
}
