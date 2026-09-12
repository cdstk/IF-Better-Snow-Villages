package bettersnowvillages.registry;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.config.ForgeConfigHandler;
import bettersnowvillages.item.ItemEnchantedRandomlyBook;
import bettersnowvillages.item.ItemEnchantedWithLevelsBook;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber(modid = BetterSnowVillages.MODID)
public class BSVItemRegistry {

    @GameRegistry.ObjectHolder(BetterSnowVillages.MODID + ":enchanted_randomly_book")
    public static Item enchantedRandomlyBook = new ItemEnchantedRandomlyBook(BetterSnowVillages.MODID, "enchanted_randomly_book");
    @GameRegistry.ObjectHolder(BetterSnowVillages.MODID + ":enchanted_with_levels_book")
    public static Item enchantedWithLevelsBook = new ItemEnchantedWithLevelsBook(BetterSnowVillages.MODID, "enchanted_with_levels_book", false);
    @GameRegistry.ObjectHolder(BetterSnowVillages.MODID + ":enchanted_with_levels_book_treasure")
    public static Item enchantedWithLevelsBookTreasure = new ItemEnchantedWithLevelsBook(BetterSnowVillages.MODID, "enchanted_with_levels_book_treasure", true);

    @SubscribeEvent
    public static void registerItemEvent(RegistryEvent.Register<Item> event) {
        if(ForgeConfigHandler.item.enableEnchantedRandomlyBook)
            event.getRegistry().register(enchantedRandomlyBook);
        if(ForgeConfigHandler.item.enableEnchantedWithLevelsBook)
            event.getRegistry().registerAll(enchantedWithLevelsBook, enchantedWithLevelsBookTreasure);
    }
}
