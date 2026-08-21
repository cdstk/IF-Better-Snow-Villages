package bettersnowvillages;

import bettersnowvillages.handlers.ForgeConfigProvider;
import bettersnowvillages.proxy.CommonProxy;
import bettersnowvillages.world.gen.BetterSnowVillagesStructureGenerator;
import bettersnowvillages.world.gen.structure.BetterSnowVillagePieces;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = BetterSnowVillages.MODID,
        version = BetterSnowVillages.VERSION,
        name = BetterSnowVillages.NAME,
        dependencies =
                "required-after:fermiumbooter@[1.3.0,);" +
                "required-after:iceandfire;"
)
public class BetterSnowVillages {
    public static final String MODID = "bettersnowvillages";
    public static final String VERSION = "0.0.0";
    public static final String NAME = "Better Snow Villages";
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean completedLoading = false;
	
    @SidedProxy(clientSide = "bettersnowvillages.proxy.ClientProxy", serverSide = "bettersnowvillages.proxy.CommonProxy")
    public static CommonProxy PROXY;
	
	@Instance(MODID)
	public static BetterSnowVillages instance;

	@Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BetterSnowVillages.PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        BetterSnowVillagePieces.registerVillagePieces();
        GameRegistry.registerWorldGenerator(new BetterSnowVillagesStructureGenerator(), 0);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ForgeConfigProvider.initDynamicDefaults();
        completedLoading = true;
    }
}