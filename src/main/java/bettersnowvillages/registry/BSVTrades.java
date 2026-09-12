package bettersnowvillages.registry;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.config.ForgeConfigHandler;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.Level;

public class BSVTrades {

    public static String[] vanillaFishermanTrades = {
            "1, minecraft:fish, 1, 10, iceandfire:sapphire_gem, 1, 1",
            "1, iceandfire:sapphire_gem, 1, 5, iceandfire:fishing_spear,1, 1",
            "2, iceandfire:sapphire_gem, 1, 3, minecraft:fishing_rod,	1, 1",
            "2, iceandfire:sapphire_gem, 1, 5, minecraft:cooked_fish, 	1, 1",
            "2, iceandfire:sapphire_gem, 1, 5, minecraft:cooked_fish, 	1, 1",
            "3, iceandfire:sapphire_gem, 1, 2, minecraft:dye:0, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 3, minecraft:tripwire_hook, 1, 1",
            "3, iceandfire:sapphire_gem, 1, 4, minecraft:fish:3, 		1, 1"
    };

    public static String[] betterFishermanTrades = {
            "1, minecraft:string, 20, 20, iceandfire:sapphire_gem, 1, 1",
            "1, minecraft:coal,   10, 10, iceandfire:sapphire_gem, 1, 1",
            "1, minecraft:fish,   1, 10,  iceandfire:sapphire_gem, 1, 1",
            "2, minecraft:fish:1, 1, 9,   iceandfire:sapphire_gem, 1, 1",
            "1, iceandfire:sapphire_gem, 1, 4, iceandfire:fishing_spear,1, 1",
            "2, iceandfire:sapphire_gem, 1, 2, minecraft:fishing_rod,	1, 1",
            "2, iceandfire:sapphire_gem, 1, 1, minecraft:dye:0, 		1, 1",
            "2, iceandfire:sapphire_gem, 1, 1, minecraft:cooked_fish, 	5, 5",
            "3, iceandfire:sapphire_gem, 1, 1, minecraft:cooked_fish:1, 2, 2",
            "3, iceandfire:sapphire_gem, 1, 1, minecraft:tripwire_hook, 1, 1",
            "3, iceandfire:sapphire_gem, 1, 4, minecraft:fish:3, 		1, 1",
            "3, iceandfire:sapphire_gem, 4, 4, iceandfire:sea_serpent_arrow,  4, 4",
            "3, iceandfire:sapphire_gem, 40, 48, bettersnowvillages:enchanted_with_levels_book:30, 1, 1",
            "3, iceandfire:sapphire_gem, 64, 64, bettersnowvillages:enchanted_with_levels_book_treasure:30, 1, 1"
    };

    public static String[] vanillaCraftsmanTrades = {
            "1, iceandfire:sapphire_gem, 1, 4, minecraft:iron_shovel, 			1, 1",
            "2, iceandfire:sapphire_gem, 1, 3, minecraft:packed_ice, 			1, 1",
            "2, iceandfire:sapphire_gem, 1, 5, iceandfire:silver_shovel, 		1, 1",
            "2, iceandfire:sapphire_gem, 1, 10,minecraft:leather, 				1, 1",
            "3, iceandfire:sapphire_gem, 1, 4  iceandfire:dragon_ice, 			1, 1",
            "3, iceandfire:sapphire_gem, 1, 9, minecraft:diamond_shovel, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 3, minecraft:leather_boots, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 4, minecraft:leather_helmet,		1, 1",
            "3, iceandfire:sapphire_gem, 1, 6, minecraft:leather_chestplate, 	1, 1",
            "3, iceandfire:sapphire_gem, 1, 6, minecraft:leather_leggings, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 7, minecraft:diamond_shovel, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 5, iceandfire:troll_leather_frost,  1, 1"
    };

    public static String[] betterCraftsmanTrades = {
            "2, iceandfire:troll_skull,         1, 1, iceandfire:sapphire_block, 1, 1",
            "2, iceandfire:cyclops_skull,       1, 1, iceandfire:sapphire_block, 1, 1",
            "3, iceandfire:seaserpent_skull,    1, 1, iceandfire:sapphire_block, 4, 4",
            "3, iceandfire:hydra_skull,         1, 1, iceandfire:sapphire_block, 4, 4",
            "1, iceandfire:sapphire_gem, 1, 4, minecraft:iron_shovel, 			1, 1",
            "2, iceandfire:sapphire_gem, 1, 3, minecraft:packed_ice, 			1, 1",
            "2, iceandfire:sapphire_gem, 1, 5, iceandfire:silver_shovel, 		1, 1",
            "2, iceandfire:sapphire_gem, 1, 10,minecraft:leather, 				1, 1",
            "2, iceandfire:sapphire_gem, 3, 3, iceandfire:amphithere_arrow,  5, 5",
            "2, iceandfire:sapphire_gem, 3, 3, iceandfire:stymphalian_arrow,  5, 5",
            "3, iceandfire:sapphire_gem, 1, 4  iceandfire:dragon_ice, 			1, 1",
            "3, iceandfire:sapphire_gem, 1, 9, minecraft:diamond_shovel, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 3, minecraft:leather_boots, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 4, minecraft:leather_helmet,		1, 1",
            "3, iceandfire:sapphire_gem, 1, 6, minecraft:leather_chestplate, 	1, 1",
            "3, iceandfire:sapphire_gem, 1, 6, minecraft:leather_leggings, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 7, minecraft:diamond_shovel, 		1, 1",
            "3, iceandfire:sapphire_gem, 1, 5, iceandfire:troll_leather_frost,  1, 1",
            "3, iceandfire:sapphire_block, 1, 1, iceandfire:dragonbone_arrow,  8, 8",
            "3, iceandfire:ruby_gem, 40, 48, bettersnowvillages:enchanted_with_levels_book:30, 1, 1",
            "3, iceandfire:ruby_gem, 64, 64, bettersnowvillages:enchanted_with_levels_book_treasure:30, 1, 1"
    };

    public static String[] vanillaShamanTrades = {
            "1, minecraft:blaze_powder, 	2, 3, 	iceandfire:sapphire_gem, 1, 1",
            "1, minecraft:ghast_tear, 		1, 4, 	iceandfire:sapphire_gem, 1, 1",
            "1, iceandfire:dragonbone, 		1, 8, 	iceandfire:sapphire_gem, 1, 1",
            "2, minecraft:brewing_stand, 	1, 9, 	iceandfire:sapphire_gem, 1, 1",
            "1, iceandfire:sapphire_gem, 1, 2, iceandfire:manuscript, 		1, 1",
            "2, iceandfire:sapphire_gem, 2, 5, minecraft:ender_eye, 		1, 1",
            "2, iceandfire:sapphire_gem, 2, 5, iceandfire:witherbone, 		1, 1",
            "2, iceandfire:sapphire_gem, 2, 3, iceandfire:wither_shard, 	1, 1",
            "3, iceandfire:sapphire_gem, 1, 5, iceandfire:ice_dragon_flesh, 1, 1",
            "3, iceandfire:sapphire_gem, 1, 12, iceandfire:ice_dragon_blood, 1, 1",
            "3, iceandfire:sapphire_gem, 1, 5, iceandfire:dragon_flute, 	1, 1"
    };

    public static String[] betterShamanTrades = {
            "1, minecraft:blaze_powder, 	2, 3, 	iceandfire:sapphire_gem, 1, 1",
            "1, minecraft:ghast_tear, 		1, 4, 	iceandfire:sapphire_gem, 1, 1",
            "1, iceandfire:dragonbone, 		1, 8, 	iceandfire:sapphire_gem, 1, 1",
            "2, minecraft:brewing_stand, 	1, 4, 	iceandfire:sapphire_gem, 1, 1",
            "3, iceandfire:dread_shard, 	1, 4, 	iceandfire:sapphire_block, 1, 1",
            "3, iceandfire:dread_key, 	1, 1, 	iceandfire:sapphire_gem, 1, 1",
            "1, iceandfire:sapphire_gem, 1, 2, iceandfire:manuscript, 		1, 1",
            "2, iceandfire:sapphire_gem, 2, 5, minecraft:ender_eye, 		1, 1",
            "2, iceandfire:sapphire_gem, 6, 10, iceandfire:ambrosia, 		1, 1",
            "2, iceandfire:sapphire_gem, 2, 5, iceandfire:witherbone, 		1, 1",
            "2, iceandfire:sapphire_gem, 2, 3, iceandfire:wither_shard, 	1, 1",
            "3, iceandfire:sapphire_gem, 1, 5, iceandfire:ice_dragon_flesh, 1, 1",
            "3, iceandfire:sapphire_gem, 1, 12, iceandfire:ice_dragon_blood, 1, 1",
            "3, iceandfire:sapphire_gem, 1, 5, iceandfire:dragon_flute, 	1, 1",
            "3, iceandfire:sapphire_gem, 4, 4, iceandfire:hydra_arrow,  4, 4",
            "3, iceandfire:amethyst_gem, 40, 48, bettersnowvillages:enchanted_with_levels_book:30, 1, 1",
            "3, iceandfire:amethyst_gem, 64, 64, bettersnowvillages:enchanted_with_levels_book_treasure:30, 1, 1"
    };

    public static String[] vanillaDesertWorkerTrades = {
            "1, minecraft:dirt, 		32, 64, 	iceandfire:myrmex_desert_resin, 1, 2",
            "1, minecraft:sand, 		32, 64, 	iceandfire:myrmex_desert_resin, 1, 2",
            "2, minecraft:deadbush, 	5, 10, 		iceandfire:myrmex_desert_resin, 1, 2",
            "3, minecraft:iron_ore, 	10, 15, 	iceandfire:myrmex_desert_resin, 1, 4",
            "4, minecraft:sugar, 		5, 8, 		iceandfire:myrmex_desert_resin, 1, 2",
            "4, iceandfire:myrmex_desert_resin, 1, 5, minecraft:bone, 5, 15"
    };
    public static String[] betterDesertWorkerTrades = vanillaDesertWorkerTrades;

    public static String[] vanillaJungleWorkerTrades = {
            "1, minecraft:dirt, 		32, 64, 	iceandfire:myrmex_jungle_resin, 1, 2",
            "1, minecraft:melon, 		10, 20, 	iceandfire:myrmex_jungle_resin, 1, 2",
            "2, minecraft:leaves:3, 	48, 64, 	iceandfire:myrmex_jungle_resin, 1, 1",
            "3, minecraft:gold_ore, 	7, 10, 		iceandfire:myrmex_jungle_resin, 1, 4",
            "4, minecraft:sugar, 		5, 8, 		iceandfire:myrmex_jungle_resin, 1, 2",
            "4, iceandfire:myrmex_jungle_resin, 1, 5, minecraft:bone, 5, 15"
    };
    public static String[] betterJungleWorkerTrades = vanillaJungleWorkerTrades;

    public static String[] vanillaDesertSoldierTrades = {
            "5, iceandfire:troll_tusk, 		1, 1, iceandfire:myrmex_desert_resin, 5, 15",
            "1, iceandfire:myrmex_desert_resin, 3, 5, minecraft:bone, 			1, 15",
            "1, iceandfire:myrmex_desert_resin, 3, 7, minecraft:feather, 		1, 3",
            "1, iceandfire:myrmex_desert_resin, 3, 5, minecraft:string, 		1, 3",
            "2, iceandfire:myrmex_desert_resin, 3, 7, minecraft:gunpowder, 		1, 3",
            "2, iceandfire:myrmex_desert_resin, 20, 50, minecraft:rabbit, 		1, 1",
            "2, iceandfire:myrmex_desert_resin, 20, 50, minecraft:dye:2, 		1, 3",
            "3, iceandfire:myrmex_desert_resin, 20, 50, minecraft:iron_nugget, 	1, 3",
            "3, iceandfire:myrmex_desert_resin, 20, 50, minecraft:chicken, 		1, 1",
            "4, iceandfire:myrmex_desert_resin, 20, 50, minecraft:gold_nugget, 	1, 3",
            "4, iceandfire:myrmex_desert_resin, 20, 50, iceandfire:silver_nugget, 1, 3"
    };
    public static String[] betterDesertSoldierTrades = vanillaDesertSoldierTrades;

    public static String[] vanillaJungleSoldierTrades = {
            "5, iceandfire:troll_tusk, 		1, 1, iceandfire:myrmex_jungle_resin, 5, 15",
            "1, iceandfire:myrmex_jungle_resin, 3, 5, minecraft:bone, 			1, 15",
            "1, iceandfire:myrmex_jungle_resin, 3, 7, minecraft:feather, 		1, 3",
            "1, iceandfire:myrmex_jungle_resin, 3, 5, minecraft:string, 		1, 3",
            "2, iceandfire:myrmex_jungle_resin, 3, 7, minecraft:gunpowder, 		1, 3",
            "2, iceandfire:myrmex_jungle_resin, 20, 50, minecraft:rabbit, 		1, 1",
            "2, iceandfire:myrmex_jungle_resin, 20, 50, minecraft:dye:2, 		1, 3",
            "3, iceandfire:myrmex_jungle_resin, 20, 50, minecraft:iron_nugget, 	1, 3",
            "3, iceandfire:myrmex_jungle_resin, 20, 50, minecraft:chicken, 		1, 1",
            "4, iceandfire:myrmex_jungle_resin, 20, 50, minecraft:gold_nugget, 	1, 3",
            "4, iceandfire:myrmex_jungle_resin, 20, 50, iceandfire:silver_nugget, 1, 3"
    };
    public static String[] betterJungleSoldierTrades = vanillaJungleSoldierTrades;

    public static String[] vanillaDesertSentinelTrades = {
            "1, minecraft:spider_eye, 		1, 1, iceandfire:myrmex_desert_resin, 1, 2",
            "1, minecraft:redstone, 		3, 4, iceandfire:myrmex_desert_resin, 1, 4",
            "3, minecraft:poisonous_potato, 1, 1, iceandfire:myrmex_desert_resin, 10, 20",
            "4, minecraft:fish:3, 			1, 1, iceandfire:myrmex_desert_resin, 5, 10",
            "2, iceandfire:myrmex_desert_resin, 3, 5, minecraft:egg, 		1, 1",
            "2, iceandfire:myrmex_desert_resin, 3, 7, minecraft:porkchop, 	1, 1",
            "2, iceandfire:myrmex_desert_resin, 3, 5, minecraft:beef, 		1, 1",
            "2, iceandfire:myrmex_desert_resin, 3, 7, minecraft:mutton, 	1, 1",
            "5, iceandfire:myrmex_desert_resin, 20, 50, minecraft:skull, 	1, 1"
    };
    public static String[] betterDesertSentinelTrades = vanillaDesertSentinelTrades;

    public static String[] vanillaJungleSentinelTrades = {
            "1, minecraft:spider_eye, 		1, 1, iceandfire:myrmex_jungle_resin, 1, 2",
            "1, minecraft:redstone, 		3, 4, iceandfire:myrmex_jungle_resin, 1, 4",
            "3, minecraft:poisonous_potato, 1, 1, iceandfire:myrmex_jungle_resin, 10, 20",
            "4, minecraft:fish:3, 			1, 1, iceandfire:myrmex_jungle_resin, 5, 10",
            "2, iceandfire:myrmex_jungle_resin, 3, 5, minecraft:egg, 		1, 1",
            "2, iceandfire:myrmex_jungle_resin, 3, 7, minecraft:porkchop, 	1, 1",
            "2, iceandfire:myrmex_jungle_resin, 3, 5, minecraft:beef, 		1, 1",
            "2, iceandfire:myrmex_jungle_resin, 3, 7, minecraft:mutton, 	1, 1",
            "5, iceandfire:myrmex_jungle_resin, 20, 50, minecraft:skull, 	1, 1"
    };
    public static String[] betterJungleSentinelTrades = vanillaJungleSentinelTrades;

    public static String[] vanillaDesertRoyalTrades = {
            "1, iceandfire:manuscript, 		1, 1, 	iceandfire:myrmex_desert_resin, 4, 6",
            "2, minecraft:gold_ingot, 		2, 4, 	iceandfire:myrmex_desert_resin, 2, 4",
            "2, iceandfire:silver_ingot, 	2, 4, 	iceandfire:myrmex_desert_resin, 2, 4",
            "3, iceandfire:myrmex_desert_resin, 5, 10, minecraft:rabbit_foot, 		1, 1",
            "4, iceandfire:myrmex_desert_resin, 10, 15, minecraft:ender_pearl,		1, 1",
            "4, iceandfire:myrmex_desert_resin, 5, 8, iceandfire:wither_shard, 		1, 1",
            "5, iceandfire:myrmex_desert_resin, 6, 15, minecraft:magma_cream, 		1, 1",
            "5, iceandfire:myrmex_desert_resin, 7, 15, minecraft:quartz, 			1, 1",
            "6, iceandfire:myrmex_desert_resin, 15, 20, minecraft:golden_carrot, 	1, 1",
            "7, iceandfire:myrmex_desert_resin, 20, 30, minecraft:emerald, 			1, 1"
    };
    public static String[] betterDesertRoyalTrades = vanillaDesertRoyalTrades;

    public static String[] vanillaJungleRoyalTrades = {
            "1, iceandfire:manuscript, 		1, 1, 	iceandfire:myrmex_jungle_resin, 4, 6",
            "2, minecraft:gold_ingot, 		2, 4, 	iceandfire:myrmex_jungle_resin, 2, 4",
            "2, iceandfire:silver_ingot, 	2, 4, 	iceandfire:myrmex_jungle_resin, 2, 4",
            "3, iceandfire:myrmex_jungle_resin, 5, 10, minecraft:rabbit_foot, 1, 1",
            "4, iceandfire:myrmex_jungle_resin, 10, 15, minecraft:ender_pearl, 1, 1",
            "4, iceandfire:myrmex_jungle_resin, 5, 8, iceandfire:wither_shard, 1, 1",
            "5, iceandfire:myrmex_jungle_resin, 6, 15, minecraft:magma_cream, 1, 1",
            "5, iceandfire:myrmex_jungle_resin, 7, 15, minecraft:quartz, 1, 1",
            "6, iceandfire:myrmex_jungle_resin, 15, 20, minecraft:golden_carrot, 1, 1",
            "7, iceandfire:myrmex_jungle_resin, 20, 30, minecraft:emerald, 1, 1"
    };
    public static String[] betterJungleRoyalTrades = vanillaJungleRoyalTrades;

    public static String[] vanillaDesertQueenTrades = {
            "1, iceandfire:myrmex_desert_resin, 1, 10,  iceandfire:myrmex_desert_egg:0, 1, 1",
            "2, iceandfire:myrmex_desert_resin, 10, 20, iceandfire:myrmex_desert_egg:1,	1, 1",
            "3, iceandfire:myrmex_desert_resin, 20, 30, iceandfire:myrmex_desert_egg:2, 1, 1",
            "4, iceandfire:myrmex_desert_resin, 30, 40, iceandfire:myrmex_desert_egg:3, 1, 1",
            "5, iceandfire:myrmex_desert_resin, 50, 64, iceandfire:myrmex_desert_egg:4, 1, 1"
    };
    public static String[] betterDesertQueenTrades = vanillaDesertQueenTrades;

    public static String[] vanillaJungleQueenTrades = {
            "1, iceandfire:myrmex_jungle_resin, 1, 10,  iceandfire:myrmex_desert_egg:0, 1, 1",
            "2, iceandfire:myrmex_jungle_resin, 10, 20, iceandfire:myrmex_desert_egg:1,	1, 1",
            "3, iceandfire:myrmex_jungle_resin, 20, 30, iceandfire:myrmex_desert_egg:2, 1, 1",
            "4, iceandfire:myrmex_jungle_resin, 30, 40, iceandfire:myrmex_desert_egg:3, 1, 1",
            "5, iceandfire:myrmex_jungle_resin, 50, 64, iceandfire:myrmex_desert_egg:4, 1, 1"
    };
    public static String[] betterJungleQueenTrades = vanillaJungleQueenTrades;

    public static VillagerRegistry.VillagerCareer initTrades(VillagerRegistry.VillagerCareer villagerCareer, String careerName){
        switch (careerName) {
            case "fisherman": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.fishermanSnowVillagerTrades); break;
            case "craftsman": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.craftsmanSnowVillagerTrades); break;
            case "shaman": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.shamanSnowVillagerTrades); break;
            case "desert_myrmex_worker": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.desertMyrmexWorkerTrades); break;
            case "jungle_myrmex_worker": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.jungleMyrmexWorkerTrades); break;
            case "desert_myrmex_soldier": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.desertMyrmexSoldierTrades); break;
            case "jungle_myrmex_soldier": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.jungleMyrmexSoldierTrades); break;
            case "desert_myrmex_sentinel": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.desertMyrmexSentinelTrades); break;
            case "jungle_myrmex_sentinel": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.jungleMyrmexSentinelTrades); break;
            case "desert_myrmex_royal": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.desertMyrmexRoyalTrades); break;
            case "jungle_myrmex_royal": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.jungleMyrmexRoyalTrades); break;
            case "desert_myrmex_queen": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.desertMyrmexQueenTrades); break;
            case "jungle_myrmex_queen": applyTradesConfig(villagerCareer, ForgeConfigHandler.trades.jungleMyrmexQueenTrades); break;
        }

        return villagerCareer;
    }
    
    public static void applyTradesConfig(VillagerRegistry.VillagerCareer career, String[] tradesConfig) {
        for(String line : tradesConfig) {
            String[] config = line.split(",");
            if(config.length < 7) continue;

            int level;
            int inMin;
            int inMax;
            int outMin;
            int outMax;

            try {
                level = Integer.parseInt(config[0].trim());
                inMin = Integer.parseInt(config[2].trim());
                inMax = Integer.parseInt(config[3].trim());
                outMin = Integer.parseInt(config[5].trim());
                outMax = Integer.parseInt(config[6].trim());
            } catch (NumberFormatException ignored) {
                BetterSnowVillages.LOGGER.log(Level.INFO, "Could not parse a number in Ice and Fire trade config: {}", line);
                continue;
            }

            addTrade(career, level, config[1].trim(), inMin, inMax, config[4].trim(), outMin, outMax);
        }
    }
    
    public static void addTrade(VillagerRegistry.VillagerCareer career, int lvl, String inpId, int inpMin, int inpMax, String outId, int outMin, int outMax) {
        String[] inpL = inpId.split(":");
        Item inpItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(inpL[0].trim(), inpL[1].trim()));
        if(inpItem == null) {
            BetterSnowVillages.LOGGER.log(Level.INFO, "Skipping Ice and Fire trade. No input item: {}", inpId);
            return;
        }

        String[] outL = outId.split(":");
        Item outItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(outL[0].trim(), outL[1].trim()));
        if(outItem == null) {
            BetterSnowVillages.LOGGER.log(Level.INFO, "Skipping Ice and Fire trade. No output item: {}", outId);
            return;
        }

        int inpMeta = 0;
        int outMeta = 0;

        try {
            if(inpL.length > 2) inpMeta = Integer.parseInt(inpL[2].trim());
            if(outL.length > 2) outMeta = Integer.parseInt(outL[2].trim());
        } catch (NumberFormatException ignored) {}

        career.addTrade(lvl, new EntityMyrmexBase.BasicTrade(
                new ItemStack(inpItem, 1, inpMeta),
                new ItemStack(outItem, 1, outMeta),
                new EntityVillager.PriceInfo(inpMin, inpMax),
                new EntityVillager.PriceInfo(outMin, outMax)
        ));
//        BetterSnowVillages.LOGGER.log(Level.INFO, "Added Ice and Fire trade: ({}), {} [{}, {}] => {} [{}, {}]", career.getName(), inpId, inpMin, inpMax, outId, outMin, outMax);
    }
}
