package bettersnowvillages.handlers;

import bettersnowvillages.BetterSnowVillages;
import bettersnowvillages.compat.ModLoadedUtil;
import bettersnowvillages.compat.RecurrentComplexUtil;
import com.google.common.collect.Lists;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ForgeConfigProvider {

    private static final Set<Class<?>> disabledSnowVillageComponents = new HashSet<>();

    public static void removeDisabledSnowVillageComponents(List<StructureVillagePieces.PieceWeight> list) {
        list.removeIf(pieceWeight -> disabledSnowVillageComponents.contains(pieceWeight.villagePieceClass));
    }

    public static void testLateDynamicComponent(Class<?> clazz) {
        for(String config : ForgeConfigHandler.betterSVGen.snowVillageComponents) {
            String[] split = config.split(",");
            if(split.length > 1 && split[1].trim().equalsIgnoreCase("false")) {
                if(clazz.getName().equals(split[0].trim())) {
                    ForgeConfigProvider.disabledSnowVillageComponents.add(clazz);
                    break;
                }
            }
        }
    }

    public static void initDynamicDefaults() {
        boolean sync = false;
        // Read user modifications
        if(ForgeConfigHandler.betterSVGen.snowVillageComponents.length > 0 && ForgeConfigHandler.betterSVGen.snowVillageComponents[0].isEmpty()) {
            List<String> componentClasses = new ArrayList<>();
            List<StructureVillagePieces.PieceWeight> list = Lists.newArrayList();

            VillagerRegistry.addExtraVillageComponents(list, new Random(), 0);
            list.removeIf(pieceWeight -> (pieceWeight).villagePiecesLimit == 0);

            list.forEach(pieceWeight -> {
                String enabled = pieceWeight.villagePieceClass.getName().contains("ivorius.reccomplex.dynamic.vanillagen.")
                        ? ", false"
                        : ", true";
                componentClasses.add(pieceWeight.villagePieceClass.getName() + enabled);
            });
            ForgeConfigHandler.betterSVGen.snowVillageComponents = componentClasses.toArray(new String[0]);
            sync = true;
        }

        // Save
        if(sync)
            ConfigManager.sync(BetterSnowVillages.MODID, Config.Type.INSTANCE);

        ForgeConfigProvider.disabledSnowVillageComponents.clear();
        Arrays.stream(ForgeConfigHandler.betterSVGen.snowVillageComponents).forEach(config -> {
            String[] split = config.split(",");
            if(split.length > 1 && split[1].trim().equalsIgnoreCase("false")) {
                String className = split[0].trim();
                boolean parseFailed = true;
                try {
                    ForgeConfigProvider.disabledSnowVillageComponents.add(Class.forName(className));
                    parseFailed = false;
                }
                catch (ClassNotFoundException ignored) {

                }

                if(parseFailed && ModLoadedUtil.RECURRENTCOMPLEX.isLoaded()) {
                    try {
                        ForgeConfigProvider.disabledSnowVillageComponents.add(RecurrentComplexUtil.getVanillaGenerationClassFactor().loadClass(className));
                    }
                    catch (ClassNotFoundException classNotFoundException) {
                        BetterSnowVillages.LOGGER.warn("Failed to parse class with Recurrent Complex Class Loader");
                    }
                }

                if(parseFailed)
                    BetterSnowVillages.LOGGER.warn("Failed to parse class: {}", className);
            }
        });
    }
}
