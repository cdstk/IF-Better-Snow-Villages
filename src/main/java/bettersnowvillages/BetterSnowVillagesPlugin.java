package bettersnowvillages;

import fermiumbooter.FermiumRegistryAPI;
import net.minecraftforge.fml.relauncher.CoreModManager;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class BetterSnowVillagesPlugin implements IFMLLoadingPlugin {

	public BetterSnowVillagesPlugin() {
		MixinBootstrap.init();
		//Replaced by @MixinConfig.MixinToggle:

		//False for Vanilla/Coremod mixins, true for regular mod mixins
		//FermiumRegistryAPI.enqueueMixin(false, "mixins.bettersnowvillages.vanilla.json");

		//FermiumRegistryAPI.enqueueMixin(true, "mixins.bettersnowvillages.jei.json", () -> Loader.isModLoaded("jei"));
		//--> Replaced by @MixinConfig.MixinToggle in ForgeConfigHandler. This way is still an option for more complicated conditions

		FermiumRegistryAPI.enqueueMixin(false, "mixins.bettersnowvillages.vanilla.json");
		FermiumRegistryAPI.enqueueMixin(true, "mixins.bettersnowvillages.iceandfire.json");
	}

	@Override
	public String[] getASMTransformerClass()
	{
		return new String[0];
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		if(Boolean.FALSE.equals(data.get("runtimeDeobfuscationEnabled"))) {
			MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
			CoreModManager.getReparseableCoremods().removeIf(s ->
					StringUtils.containsIgnoreCase(s, "fermiumbooter")
                    || StringUtils.containsIgnoreCase(s, "iceandfire")
			);
		}
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}