package bettersnowvillages.wrapper;

import bettersnowvillages.compat.IceAndFireForksUtil;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.iceandfire.wrapper.IaFVillagerRegistryWrapper;
import bettersnowvillages.compat.iceandfire.wrapper.ModVillagersWrapper;
import com.github.alexthe666.iceandfire.entity.EntitySnowVillager;
import com.github.alexthe666.iceandfire.entity.IafVillagerRegistry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.World;

import java.util.Random;

public class BetterSnowVillagesWrapper {

    public static EntityLiving newInfestedMobInstance(World world) {
        return IceAndFireForksUtil.getInfestedSnowVillageMob(world);
    }

    public static EntityVillager newSnowVillagerInstance(World world) {
        return new EntitySnowVillager(world);
    }

    public static void setRandomSnowProfession(EntityVillager villager, Random rand) {
        if(villager instanceof EntitySnowVillager) {
            if (ModLoadedUtil.ICEANDFIRE.fork == ModLoadedUtil.INFLoadedContainer.FORK.RLCRAFT) {
                IafVillagerRegistry.INSTANCE.setRandomProfession((EntitySnowVillager) villager, rand);
            }
            else if (IceAndFireForksUtil.getModernRegistry()) {
                IaFVillagerRegistryWrapper.setRandomSnowProfession(villager, rand);
            }
            else {
                ModVillagersWrapper.setRandomSnowProfession((EntitySnowVillager) villager, rand);
            }
        }
    }
}
