package bettersnowvillages.client;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.registry.BSVItemRegistry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = BetterSnowVillages.MODID, value = Side.CLIENT)
public class ClientModRegistry {

    @SubscribeEvent
    public static void registerModelEvent(ModelRegistryEvent event) {
        registerModel(BSVItemRegistry.enchantedRandomlyBook, "inventory");
        registerModel(BSVItemRegistry.enchantedWithLevelsBook, "inventory");
        registerModel(BSVItemRegistry.enchantedWithLevelsBookTreasure, "inventory");
    }

    private static void registerModel(Item item, String variant) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), variant));
    }
}
