package bettersnowvillages.compat;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionRange;

// Nischhelm Style
public class ModLoadedUtil {

    public static final String AAAM_MODID = "antiqueatlasautomarker";
    public static final String BOUNTIFUL_MODID = "bountiful";
    public static final String CHARM_MODID = "charm";
    public static final String ICEANDFIRE_MODID = "iceandfire";
    public static final String QUARK_MODID = "quark";
//    public static final String RECURRENTCOMPLEX_MODID = "reccomplex";
    public static final String WAYSTONES_MODID = "waystones";

    public static LoadedContainer AAAM = new LoadedContainer(AAAM_MODID);
    public static LoadedContainer BOUNTIFUL = new LoadedContainer(BOUNTIFUL_MODID);
    public static LoadedContainer CHARM = new LoadedContainer(CHARM_MODID);
    public static INFLoadedContainer ICEANDFIRE = new INFLoadedContainer(ICEANDFIRE_MODID);
    public static LoadedContainer QUARK = new LoadedContainer(QUARK_MODID);
//    public static LoadedContainer RECURRENTCOMPLEX = new LoadedContainer(RECURRENTCOMPLEX_MODID);
    public static LoadedContainer WAYSTONES = new LoadedContainer(WAYSTONES_MODID);

    // Nischhelm style
    public static boolean versionInRange(LoadedContainer container, String version) {
        if (!container.isLoaded()) return false;
        VersionRange range;
        try {
            range = VersionRange.createFromVersionSpec(version);
        } catch (Exception e) {
            return false;
        }
        return range.containsVersion(container.getVersion());
    }

    public static class LoadedContainer{
        private Boolean isLoaded = null;
        private DefaultArtifactVersion version;
        private final String key;
        private LoadedContainer(String key){
            this.key = key;
        }
        public boolean isLoaded(){
            if(this.isLoaded == null) isLoaded = Loader.isModLoaded(key);
            return isLoaded;
        }
        public DefaultArtifactVersion getVersion(){
            if(version == null) version = new DefaultArtifactVersion(Loader.instance().getIndexedModList().get(key).getVersion());
            return version;
        }
    }

    public static class INFLoadedContainer extends LoadedContainer{
        public enum FORK {
            BASE,
            RLCRAFT,
            ROTN
        }

        public final FORK fork;
        private Boolean isLightingFork = null;
        private INFLoadedContainer(String key) {
            super(key);
            if(this.isLoaded()) {
                ModContainer modContainer = Loader.instance().getIndexedModList().get(key);
                if(modContainer.getName().equals("Ice and Fire: RotN Edition")) {
                    this.fork = FORK.ROTN;
                }
                else if(modContainer.getMetadata().authorList.contains("Kotlin-Programmer")) {
                    this.fork = FORK.RLCRAFT;
                }
                else {
                    this.fork = FORK.BASE;
                }
            }
            else {
                this.fork = FORK.BASE;
            }
        }
//        // Based on RLCombat's check
//        public boolean isLightningFork(){
//            if(isLightingFork == null) {
//                isLightingFork = false;
//                String[] arrOfStr = Loader.instance().getIndexedModList().get("iceandfire").getVersion().split("\\.");
//                try {
//                    int i = Integer.parseInt(String.valueOf(arrOfStr[0]));
//                    if (i >= 2) isLightingFork = true;
//                } catch (Exception ignored) {}
//            }
//            return isLightingFork;
//        }
    }
}
