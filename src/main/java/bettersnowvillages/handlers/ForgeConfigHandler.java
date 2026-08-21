package bettersnowvillages.handlers;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.ModLoadedUtil;
import fermiumbooter.annotations.MixinConfig;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BetterSnowVillages.MODID)
public class ForgeConfigHandler {
	
	@Config.Comment("Server-Side Options")
	@Config.Name("Better Snow Village Generator")
	public static final BetterSnowVillageGenConfig betterSVGen = new BetterSnowVillageGenConfig();

	@Config.Comment("Client-Side Options")
	@Config.Name("Base Snow Village Generator")
	public static final BaseSnowVillageGenConfig ifSVGen = new BaseSnowVillageGenConfig();

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
		public int betterSVMinDist = 0;

		@Config.Comment({
				"Modpack development option.",
				"Recurrent Complex dynamically loads Village Components compared to vanilla Forge registering them on startup.",
				"This will automatically check RC's custom Class Loader to catch when Components are registered",
				"Else Recurrent Classes can be manually determined using structureID (file name), generationID (json file id field)"
		})
		@Config.Name("Mixin: Recurrent Complex Village Reader (Recurrent Complex)")
		@MixinConfig.MixinToggle(lateMixin = "mixins.bettersnowvillages.recurrentcomplex.json", defaultValue = true)
		@MixinConfig.CompatHandling(
				modid = ModLoadedUtil.RECURRENTCOMPLEX_MODID,
				desired = true,
				reason = "Mod needed for this Mixin to properly work",
				warnIngame = false
		)
		@Config.RequiresMcRestart
		public boolean recurrentVillageReader = true;

		@Config.Comment({
				"Modpack development option.",
				"Forces Recurrent Complex's Village Component cache to not be cleared."
		})
		@Config.Name("Recurrent Complex Village Reader - Force Hold Cache")
		public boolean recurrentVillageForceCache = false;

		@Config.Comment({
				"Modpack development option.",
				"Run an additional check for Components when Recurrent Complex cleans its cache."
		})
		@Config.Name("Recurrent Complex Village Reader - Read Cache")
		public boolean recurrentVillageReadCache = false;

		@Config.Comment({
				"List of Village Components registered to Forge.",
				"Components can be blacklisted from generating in Better Snow Villages if set to 'false'",
				"This list can be automatically refreshed with loaded components if the 1st entry is blank."
		})
		@Config.Name("Better Snow Village Vanilla Components")
		public String[] snowVillageComponents = {
				"com.github.alexthe666.iceandfire.world.village.ComponentAnimalFarm, true",
				"net.blay09.mods.waystones.worldgen.ComponentVillageWaystone, true",
				"ejektaflex.bountiful.worldgen.VillageBoardComponent, true",
				"ivorius.reccomplex.dynamic.vanillagen.VillageMarketplace_vanilla_85673491, true",

				"ivorius.reccomplex.dynamic.vanillagen.VillageHouseRich_vanilla_b8d93a75, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaLibrary_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaGlassworks_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaTannery_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaChandlery_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageGuardTower_vanilla_542fa4df, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageWoodMill_vanilla_ad1ad891, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaStonemason_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageTriplets_vanilla_85673491, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageSavannaKitchen_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageForgeLarge_vanilla_85673491, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageInn_vanilla_c948bef2, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageChurch_vanilla_85673491, false",
				"ivorius.reccomplex.dynamic.vanillagen.VillageHouseRich1_vanilla_c6a47f55, false"
		};
	}

	@MixinConfig(name = BetterSnowVillages.MODID)
	public static class BaseSnowVillageGenConfig {

		@Config.Comment("Example client side config option")
		@Config.Name("Example Client Option")
		public boolean exampleClientOption = true;
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