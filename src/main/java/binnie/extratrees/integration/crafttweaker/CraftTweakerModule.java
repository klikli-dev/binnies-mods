package binnie.extratrees.integration.crafttweaker;

import binnie.Constants;
import binnie.extratrees.integration.crafttweaker.handlers.BreweryRecipeHandler;
import binnie.extratrees.integration.crafttweaker.handlers.DistilleryRecipeHandler;
import binnie.extratrees.integration.crafttweaker.handlers.FruitPressRecipeHandler;
import binnie.extratrees.integration.crafttweaker.handlers.LumbermillRecipeHandler;
import binnie.extratrees.modules.ExtraTreesModuleUIDs;
import binnie.modules.BinnieModule;
import binnie.modules.Module;
import com.google.common.collect.ImmutableSet;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Set;

@BinnieModule(moduleID = ExtraTreesModuleUIDs.CRAFT_TWEAKER, moduleContainerID = Constants.EXTRA_TREES_MOD_ID, name = "Craft Tweaker", unlocalizedDescription = "extratrees.module.crafttweaker")
public class CraftTweakerModule extends Module {
	public static final String MOD_ID = "crafttweaker";

	@Override
	public void init(FMLInitializationEvent event) {
		initCT();
	}

	@Optional.Method(modid = "crafttweaker")
	private void initCT(){
		MineTweakerAPI.registerClass(BreweryRecipeHandler.class);
		MineTweakerAPI.registerClass(DistilleryRecipeHandler.class);
		MineTweakerAPI.registerClass(FruitPressRecipeHandler.class);
		MineTweakerAPI.registerClass(LumbermillRecipeHandler.class);
	}

	@Override
	public boolean isAvailable() {
		return Loader.isModLoaded(MOD_ID);
	}

	@Override
	public Set<String> getDependencyUids() {
		return ImmutableSet.of(ExtraTreesModuleUIDs.MACHINES);
	}
}
