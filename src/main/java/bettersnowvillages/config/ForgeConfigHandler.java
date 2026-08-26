package bettersnowvillages.config;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.registry.BSVTrades;
import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BetterSnowVillages.MODID)
public class ForgeConfigHandler {
	
	@Config.Name("Better Snow Village Generator")
	public static final BetterSnowVillageGenConfig betterSVGen = new BetterSnowVillageGenConfig();

	@Config.Name("Base Snow Village Generator")
	public static final BaseSnowVillageGenConfig ifSVGen = new BaseSnowVillageGenConfig();

	@Config.Name("Snow Villager and Myrmex Trades")
	public static final TradesConfig trades = new TradesConfig();

	@MixinConfig(name = BetterSnowVillages.MODID)
	public static class BetterSnowVillageGenConfig {

		@Config.Comment("Should better snow villages generate using Vanilla generation")
		@Config.Name("Generate Better Snow Villages")
		public boolean genBetterSV = true;

		@Config.Comment("Use vanilla Ice and Fire's config settings with the exception of the better snow village generation toggle")
		@Config.Name("Generate Better Snow Villages With Vanilla Config")
		public boolean useIFConfig = true;

		@Config.Comment("Chance to generate snow villages per chunk, 1 in N chance")
		@Config.Name("Generate Better Snow Villages Chance")
		@Config.RangeInt(min = 1)
		public int genBetterSVChance = 100;

		@Config.Comment("Snow Villages and related generation will not spawn in these dimensions")
		@Config.Name("Better Snow Village Dimension Blacklist")
		public int[] betterSVBlacklistedDimensions = new int[] {0};

		@Config.Comment("If true, treat the Snow Village Dimension Blacklist as a Whitelist instead")
		@Config.Name("Better Snow Village Dimension Use Whitelist")
		public boolean betterSVWhitelist = true;

		@Config.Comment("How far apart snow villages should spawn apart from each other")
		@Config.Name("Better Snow Village Minimum Distance")
		@Config.RangeInt(min = 0)
		public int betterSVMinDist = 300;

		@Config.Comment("Modifies Bountiful mod's Bounty Board to generate snow and ice variations in cold and snowy tagged biomes.")
		@Config.Name("Mixin: Snowy Bounty Board (Bountiful)")
		@MixinConfig.MixinToggle(lateMixin = "mixins.bettersnowvillages.bountiful.json", defaultValue = true)
		@MixinConfig.CompatHandling(
				modid = ModLoadedUtil.BOUNTIFUL_MODID,
				desired = true,
				reason = "Mod needed for this Mixin to properly work",
				warnIngame = false
		)
		@Config.RequiresMcRestart
		public boolean genBetterBoard = true;

		@Config.Comment("Modifies Village Waystones to generate snow and ice variations in cold and snowy tagged biomes.")
		@Config.Name("Mixin: Snowy Waystone (Waystones)")
		@MixinConfig.MixinToggle(lateMixin = "mixins.bettersnowvillages.waystones.json", defaultValue = true)
		@MixinConfig.CompatHandling(
				modid = ModLoadedUtil.WAYSTONES_MODID,
				desired = true,
				reason = "Mod needed for this Mixin to properly work",
				warnIngame = false
		)
		@Config.RequiresMcRestart
		public boolean genBetterWaystone = true;

		@Config.Comment({
				"List of Village Components Classes that will generate in Better Snow Villages.",
				"Format: [class, weight, min count, max count]",
				"\tNegative min counts can be specified to make a structure rarely spawn.",
				"Supports any instances of net.minecraft.world.gen.structure.StructureVillagePieces.Village"
		})
		@Config.Name("Better Snow Village Class Components")
		public String[] snowVillageClassComponents = {
				"net.minecraft.world.gen.structure.StructureVillagePieces$Church, 1, -8, 1",
				"net.minecraft.world.gen.structure.StructureVillagePieces$House3, 1, -8, 1",
				"net.minecraft.world.gen.structure.StructureVillagePieces$House4Garden, 1, -3, 1",
				"bettersnowvillages.world.gen.structure.BetterSnowVillagePieces$ExtraTorch, 50, 5, 6",
				"bettersnowvillages.world.gen.structure.BetterSnowVillagePieces$SnowWoodHut, 40, 2, 3",
				"bettersnowvillages.world.gen.structure.BetterSnowVillagePieces$SnowHouse3, 1, -8, 0",
				"bettersnowvillages.world.gen.structure.BetterSnowVillagePieces$SnowHouse4, 40, 1, 2"
		};

		@Config.Comment({
				"List of Village Components from vanilla Resource Packs that will generate in Better Snow Villages.",
				"Format: [namespace:path,     weight, min count, max count,     bounding box width, height, length,     offset x, y, z]",
				"\tNegative min counts can be specified to make a structure rarely spawn.",
				"\tOffsets can be used to blend into the terrain better such as making flush with the ground.",
				"Supports any vanilla NBT formatted structure that fits inside a single Bounding Box. "
		})
		@Config.Name("Better Snow Village Resource Pack Components")
		public String[] snowVillageNBTComponents = {
				"minecraft:igloo/igloo_top, 						40, 1, 1, 	7, 5, 8, 	0, -1, 0",
				"bettersnowvillages:snowy_animal_pen_1_cow, 		3, 0, 1, 	9, 6, 8,	0, 0, 0",
				"bettersnowvillages:snowy_animal_pen_2_sheep, 		3, 0, 1, 	9, 6, 8, 	0, -1, 0",
				"bettersnowvillages:snowy_butchers_shop_2_empty,	40, 1, 1, 	5, 5, 9, 	0, -1, 0",
				"bettersnowvillages:snowy_farm_1_wheat, 			3, 0, 1, 	7, 6, 6,	0, 0, 0",
				"bettersnowvillages:snowy_farm_2_wheat, 			3, 0, 1, 	9, 6, 7,	0, 0, 0",
				"bettersnowvillages:snowy_medium_house_1_random, 	4, -2, 1, 	8, 6, 7, 	0, -1, 0",
				"bettersnowvillages:snowy_medium_house_3_random, 	4, -2, 1, 	7, 5, 5, 	0, -1, 0",
				"bettersnowvillages:snowy_small_house_1_random, 	8, -2, 1, 	7, 5, 6, 	0, -1, 0",
				"bettersnowvillages:snowy_small_house_4_random, 	8, -2, 1, 	7, 5, 8, 	0, -1, 0",
				"bettersnowvillages:snowy_small_house_5_random, 	8, -2, 1, 	5, 5, 7, 	0, -1, 0",
				"bettersnowvillages:snowy_small_house_8_random, 	8, -2, 1, 	5, 5, 6, 	0, -1, 0",
				"bettersnowvillages:snowy_tannery_1_empty, 			1, -2, 1, 	9, 9, 8,	0, 0, 0",
				"bettersnowvillages:snowy_temple_1_empty, 			1, -2, 1, 	7, 14, 10,	0, 0, 0",
		};

		@Config.Comment({
				"List of Village Component Classes registered to Forge.",
				"Components can be blacklisted from generating in Better Snow Villages if set to 'false'",
				"This list can be automatically refreshed with loaded components if the 1st entry is blank.",
				"Recurrent Classes can be manually determined using structureID (file name), generationID (json file id field)"
		})
		@Config.Name("Better Snow Village Vanilla Components")
		public String[] snowVillageComponents = {
				"com.github.alexthe666.iceandfire.world.village.ComponentAnimalFarm, true",
				"ejektaflex.bountiful.worldgen.VillageBoardComponent, true",
				"net.blay09.mods.waystones.worldgen.ComponentVillageWaystone, true",
				"ivorius.reccomplex.dynamic.vanillagen.VillageMarketplace_vanilla_85673491, true",
				"ivorius.reccomplex.dynamic.vanillagen.P_VillageFarmhouse_vanilla_226d02c8, true",

				"ivorius.reccomplex.dynamic.vanillagen.VillageChurch_vanilla_85673491, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageForgeLarge_vanilla_85673491, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageGuardTower_vanilla_542fa4df, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageHouseRich_vanilla_b8d93a75, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageHouseRich1_vanilla_c6a47f55, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageInn_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageLargeLibrary_vanilla_85673491, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaChandlery_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaGlassworks_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaKitchen_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaLibrary_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaStonemason_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaTannery_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageTriplets_vanilla_85673491, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageWoodMill_vanilla_ad1ad891, false",

				"ivorius.reccomplex.dynamic.vanillagen.P_AncientTeleporter_vanilla_dd3cfed9, false",
				"ivorius.reccomplex.dynamic.vanillagen.P_SkyCastle_vanilla_34b2fef7, false",
				"ivorius.reccomplex.dynamic.vanillagen.P_VillageBlacksmith_vanilla_6439e62e, false",
				"ivorius.reccomplex.dynamic.vanillagen.P_VillageHouse_vanilla_df295ef9, false",
				"ivorius.reccomplex.dynamic.vanillagen.P_VillageHouse2_VillageHouseLarge, false",
				"ivorius.reccomplex.dynamic.vanillagen.P_VillageInn_vanilla_9a6341f4, false",
				"ivorius.reccomplex.dynamic.vanillagen.P_VillageLibrary_vanilla_5e410e18, false",
				"ivorius.reccomplex.dynamic.vanillagen.P_Village2Story_vanilla_f432962b, false",

				"ivorius.reccomplex.dynamic.vanillagen.RLCraft_Vineyard_vanilla_115c0a78, false",
				"ivorius.reccomplex.dynamic.vanillagen.SeasonsGreenhouse_vanilla_a51c750, false",
				"ivorius.reccomplex.dynamic.vanillagen.ThreeTowers_vanilla_3966158a, false"
		};
	}

	public static class BaseSnowVillageGenConfig {

		@Config.Comment("Adds Bountiful mod's Bounty Board to the vanilla Ice and Fire Snow Villages.")
		@Config.Name("Mod Compat: Bountiful Bounty Board")
		public boolean baseGenBountyBoard = true;

		@Config.Comment("Adds Waystones to the vanilla Ice and Fire Snow Villages, requires Waystones mod.")
		@Config.Name("Mod Compat: Waystone")
		public boolean baseGenWaystone = true;
	}
	@MixinConfig(name = BetterSnowVillages.MODID)
	public static class TradesConfig {

		@Config.Comment({
				"Must be set to \"custom\" in order to use custom values.",
				"Else this switch is used to keep values up to date via checking mod version number."
		})
		@Config.Name("Master Switch: Regenerate Config Values")
		@Config.RequiresMcRestart
		public String regenerateConfig = BetterSnowVillages.VERSION;

		@Config.Comment({
				"Allows for modifying Myrmex and Snow Village trades.",
				"Based on a ZenUtils mixin example"
		})
		@Config.Name("Mixin: Modify Trades (Ice and Fire)")
		@MixinConfig.MixinToggle(lateMixin = "mixins.bettersnowvillages.iceandfire.trades.json", defaultValue = true)
		@Config.RequiresMcRestart
		public boolean modifyTrades = true;

		@Config.Comment({
				"List of trades for the Snow Villager, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Fisherman Snow Villager Trades")
		@Config.RequiresMcRestart
		public String[] fishermanSnowVillagerTrades = BSVTrades.betterFishermanTrades;

		@Config.Comment({
				"List of trades for the Snow Villager, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Craftsman Snow Villager Trades")
		@Config.RequiresMcRestart
		public String[] craftsmanSnowVillagerTrades = BSVTrades.betterCraftsmanTrades;

		@Config.Comment({
				"List of trades for the Snow Villager, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Shaman Snow Villager Trades")
		@Config.RequiresMcRestart
		public String[] shamanSnowVillagerTrades = BSVTrades.betterShamanTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Desert Myrmex Worker Trades")
		@Config.RequiresMcRestart
		public String[] desertMyrmexWorkerTrades = BSVTrades.betterDesertWorkerTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Jungle Myrmex Worker Trades")
		@Config.RequiresMcRestart
		public String[] jungleMyrmexWorkerTrades = BSVTrades.betterJungleWorkerTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Desert Myrmex Soldier Trades")
		@Config.RequiresMcRestart
		public String[] desertMyrmexSoldierTrades = BSVTrades.betterDesertSoldierTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Jungle Myrmex Soldier Trades")
		@Config.RequiresMcRestart
		public String[] jungleMyrmexSoldierTrades = BSVTrades.betterJungleSoldierTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Desert Myrmex Sentinel Trades")
		@Config.RequiresMcRestart
		public String[] desertMyrmexSentinelTrades = BSVTrades.betterDesertSentinelTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Jungle Myrmex Sentinel Trades")
		@Config.RequiresMcRestart
		public String[] jungleMyrmexSentinelTrades = BSVTrades.betterJungleSentinelTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Desert Myrmex Royal Trades")
		@Config.RequiresMcRestart
		public String[] desertMyrmexRoyalTrades = BSVTrades.betterDesertRoyalTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Jungle Myrmex Royal Trades")
		@Config.RequiresMcRestart
		public String[] jungleMyrmexRoyalTrades = BSVTrades.betterJungleRoyalTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Desert Myrmex Queen Trades")
		@Config.RequiresMcRestart
		public String[] desertMyrmexQueenTrades = BSVTrades.vanillaDesertQueenTrades;

		@Config.Comment({
				"List of trades for the Myrmex, input and output items can be specified alongside count.",
				"Format: [career level, buy item (modid:itemid:meta), min buy, max buy, sell item, min sell, max sell]"
		})
		@Config.Name("Jungle Myrmex Queen Trades")
		@Config.RequiresMcRestart
		public String[] jungleMyrmexQueenTrades = BSVTrades.betterJungleQueenTrades;
	}

	@Mod.EventBusSubscriber(modid = BetterSnowVillages.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(BetterSnowVillages.MODID)) {
				ConfigManager.sync(BetterSnowVillages.MODID, Config.Type.INSTANCE);

				ForgeConfigProvider.initDynamicDefaults();
			}
		}
	}
}