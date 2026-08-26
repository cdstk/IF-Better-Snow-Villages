package bettersnowvillages.config;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.config.worldgen.ClassGenInfo;
import bettersnowvillages.config.worldgen.NBTGenInfo;
import bettersnowvillages.registry.BSVTrades;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ForgeConfigProvider {

    private static final Set<String> disabledSnowVillageComponentClassNames = new HashSet<>();
    private static final Map<Class<? extends StructureVillagePieces.Village>, ClassGenInfo> classGenInfo = new HashMap<>();
    private static final Map<ResourceLocation, NBTGenInfo> nbtGenInfo = new HashMap<>();

    public static void removeDisabledSnowVillageComponents(List<StructureVillagePieces.PieceWeight> list) {
        list.removeIf(pieceWeight -> disabledSnowVillageComponentClassNames.contains(pieceWeight.villagePieceClass.getName()));
    }

    public static Map<Class<? extends StructureVillagePieces.Village>, ClassGenInfo> getSVClassGen() {
        return classGenInfo;
    }

    public static Map<ResourceLocation, NBTGenInfo> getSVNBTGen() {
        return nbtGenInfo;
    }

    public static void initDynamicDefaults() {
        boolean sync;
        // Read user modifications
        sync = resetSnowVillageComponentsConfig();
        sync = sync || resetTradesConfig();

        // Save
        if(sync)
            ConfigManager.sync(BetterSnowVillages.MODID, Config.Type.INSTANCE);

        ForgeConfigProvider.disabledSnowVillageComponentClassNames.clear();
        Arrays.stream(ForgeConfigHandler.betterSVGen.snowVillageComponents).forEach(config -> {
            String[] split = config.split(",");
            if(split.length > 1 && split[1].trim().equalsIgnoreCase("false")) {
                ForgeConfigProvider.disabledSnowVillageComponentClassNames.add(split[0].trim());
            }
        });

        ForgeConfigProvider.classGenInfo.clear();
        Arrays.stream(ForgeConfigHandler.betterSVGen.snowVillageClassComponents).forEach(line -> {
            String[] config = line.split(",");
            if(config.length >= 4) {
                try {
                    Class<?> clazz = Class.forName(config[0].trim());
                    int max = Integer.parseInt(config[3].trim());
                    if(max > 0 && StructureVillagePieces.Village.class.isAssignableFrom(clazz)) {
                        ForgeConfigProvider.classGenInfo.put(
                                (Class<? extends StructureVillagePieces.Village>) clazz,
                                new ClassGenInfo(
                                        Integer.parseInt(config[1].trim()),
                                        Integer.parseInt(config[2].trim()),
                                        max
                                )
                        );
                    }

                } catch (ClassNotFoundException e) {
                    BetterSnowVillages.LOGGER.warn("Failed to parse class: {}", config[0].trim());
                }
                catch (NumberFormatException e) {
                    BetterSnowVillages.LOGGER.warn("Failed to parse integer in: {}, {}, {}", config[1], config[2], config[3]);
                }
            }
        });

        ForgeConfigProvider.nbtGenInfo.clear();
        Arrays.stream(ForgeConfigHandler.betterSVGen.snowVillageNBTComponents).forEach(line -> {
            String[] config = line.split(",");
            if(config.length >= 7) {
                String[] structureID = config[0].trim().split(":");
                if(structureID.length >= 2) {
                    try {
                        int max = Integer.parseInt(config[3].trim());
                        if (max > 0) {
                            if(config.length >= 10) {
                                ForgeConfigProvider.nbtGenInfo.put(
                                        new ResourceLocation(structureID[0].trim(), structureID[1].trim()),
                                        new NBTGenInfo(
                                                Integer.parseInt(config[1].trim()),
                                                Integer.parseInt(config[2].trim()),
                                                max,
                                                Integer.parseInt(config[4].trim()),
                                                Integer.parseInt(config[5].trim()),
                                                Integer.parseInt(config[6].trim()),
                                                Integer.parseInt(config[7].trim()),
                                                Integer.parseInt(config[8].trim()),
                                                Integer.parseInt(config[9].trim())
                                        )
                                );
                            }
                            else {
                                ForgeConfigProvider.nbtGenInfo.put(
                                        new ResourceLocation(structureID[0].trim(), structureID[1].trim()),
                                        new NBTGenInfo(
                                                Integer.parseInt(config[1].trim()),
                                                Integer.parseInt(config[2].trim()),
                                                max,
                                                Integer.parseInt(config[4].trim()),
                                                Integer.parseInt(config[5].trim()),
                                                Integer.parseInt(config[6].trim())
                                        )
                                );
                            }
                        }
                    }
                    catch (NumberFormatException e) {
                        BetterSnowVillages.LOGGER.warn("Failed to parse integer in: {}", line);
                    }
                }
            }
        });
    }

    private static boolean shouldConfigCategoryReset(String savedVersion) {
        return !savedVersion.isEmpty()
                && !savedVersion.equals("custom")
                && !savedVersion.equals(BetterSnowVillages.VERSION);
    }

    private static boolean resetSnowVillageComponentsConfig() {
        if(ForgeConfigHandler.betterSVGen.snowVillageComponents.length > 0 && ForgeConfigHandler.betterSVGen.snowVillageComponents[0].isEmpty()) {
            List<String> componentClasses = new ArrayList<>();
            List<StructureVillagePieces.PieceWeight> list = new ArrayList<>();

            VillagerRegistry.addExtraVillageComponents(list, new Random(), 0);
            list.forEach(pieceWeight -> componentClasses.add(pieceWeight.villagePieceClass.getName() + ", true"));
            ForgeConfigHandler.betterSVGen.snowVillageComponents = componentClasses.toArray(new String[0]);
            return true;
        }
        return false;
    }

    private static boolean resetTradesConfig() {
        boolean sync = false;
        if(ForgeConfigHandler.trades.regenerateConfig.equalsIgnoreCase("vanilla")) {
            ForgeConfigHandler.trades.fishermanSnowVillagerTrades = BSVTrades.vanillaFishermanTrades;
            ForgeConfigHandler.trades.craftsmanSnowVillagerTrades = BSVTrades.vanillaCraftsmanTrades;
            ForgeConfigHandler.trades.shamanSnowVillagerTrades = BSVTrades.vanillaShamanTrades;

            ForgeConfigHandler.trades.desertMyrmexWorkerTrades = BSVTrades.vanillaDesertWorkerTrades;
            ForgeConfigHandler.trades.desertMyrmexSoldierTrades = BSVTrades.vanillaDesertSoldierTrades;
            ForgeConfigHandler.trades.desertMyrmexSentinelTrades = BSVTrades.vanillaDesertSentinelTrades;
            ForgeConfigHandler.trades.desertMyrmexRoyalTrades = BSVTrades.vanillaDesertRoyalTrades;
            ForgeConfigHandler.trades.desertMyrmexQueenTrades = BSVTrades.vanillaDesertQueenTrades;

            ForgeConfigHandler.trades.jungleMyrmexWorkerTrades = BSVTrades.vanillaJungleWorkerTrades;
            ForgeConfigHandler.trades.jungleMyrmexSoldierTrades = BSVTrades.vanillaJungleSoldierTrades;
            ForgeConfigHandler.trades.jungleMyrmexSentinelTrades = BSVTrades.vanillaJungleSentinelTrades;
            ForgeConfigHandler.trades.jungleMyrmexRoyalTrades = BSVTrades.vanillaJungleRoyalTrades;
            ForgeConfigHandler.trades.jungleMyrmexQueenTrades = BSVTrades.vanillaJungleQueenTrades;

            ForgeConfigHandler.trades.regenerateConfig = "custom";
            sync = true;
        }
        else if(shouldConfigCategoryReset(ForgeConfigHandler.trades.regenerateConfig)) {
            ForgeConfigHandler.trades.fishermanSnowVillagerTrades = BSVTrades.betterFishermanTrades;
            ForgeConfigHandler.trades.craftsmanSnowVillagerTrades = BSVTrades.betterCraftsmanTrades;
            ForgeConfigHandler.trades.shamanSnowVillagerTrades = BSVTrades.betterShamanTrades;

            ForgeConfigHandler.trades.desertMyrmexWorkerTrades = BSVTrades.betterDesertWorkerTrades;
            ForgeConfigHandler.trades.desertMyrmexSoldierTrades = BSVTrades.betterDesertSoldierTrades;
            ForgeConfigHandler.trades.desertMyrmexSentinelTrades = BSVTrades.betterDesertSentinelTrades;
            ForgeConfigHandler.trades.desertMyrmexRoyalTrades = BSVTrades.betterDesertRoyalTrades;
            ForgeConfigHandler.trades.desertMyrmexQueenTrades = BSVTrades.betterDesertQueenTrades;

            ForgeConfigHandler.trades.jungleMyrmexWorkerTrades = BSVTrades.betterJungleWorkerTrades;
            ForgeConfigHandler.trades.jungleMyrmexSoldierTrades = BSVTrades.betterJungleSoldierTrades;
            ForgeConfigHandler.trades.jungleMyrmexSentinelTrades = BSVTrades.betterJungleSentinelTrades;
            ForgeConfigHandler.trades.jungleMyrmexRoyalTrades = BSVTrades.betterJungleRoyalTrades;
            ForgeConfigHandler.trades.jungleMyrmexQueenTrades = BSVTrades.betterJungleQueenTrades;

            ForgeConfigHandler.trades.regenerateConfig = BetterSnowVillages.VERSION;
            sync = true;
        }

        return sync;
    }
}
