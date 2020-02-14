package binnie.botany.modules;

import binnie.Constants;
import binnie.botany.Botany;
import binnie.botany.api.gardening.EnumAcidity;
import binnie.botany.api.gardening.EnumMoisture;
import binnie.botany.farming.CircuitGarden;
import binnie.botany.items.ItemInsulatedTube;
import binnie.core.Mods;
import binnie.modules.BinnieModule;
import binnie.modules.Module;
import com.google.common.collect.ImmutableSet;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Set;

@BinnieModule(moduleID = BotanyModuleUIDs.FARMING, moduleContainerID = Constants.BOTANY_MOD_ID, name = "Farming", unlocalizedDescription = "botany.module.farming")
public class ModuleFarming extends Module {
	public ItemInsulatedTube insulatedTube;

	@Override
	public Set<String> getDependencyUids() {
		return ImmutableSet.of(BotanyModuleUIDs.FLOWERS);
	}

	@Override
	public void preInit() {
		insulatedTube = new ItemInsulatedTube();
		Botany.proxy.registerItem(insulatedTube);
	}

	@Override
	public void init() {
		ItemStack yellow = new ItemStack(Blocks.YELLOW_FLOWER, 1);
		ItemStack red = new ItemStack(Blocks.RED_FLOWER, 1);
		ItemStack blue = new ItemStack(Blocks.RED_FLOWER, 1, 7);
		for (boolean manual : new boolean[]{true, false}) {
			for (boolean fertilised : new boolean[]{true, false}) {
				for (EnumMoisture moist : EnumMoisture.values()) {
					ItemStack icon;
					if (moist == EnumMoisture.DRY) {
						icon = yellow;
					} else if (moist == EnumMoisture.NORMAL) {
						icon = red;
					} else {
						icon = blue;
					}
					int insulate = 2 - moist.ordinal();
					if (fertilised) {
						insulate += 3;
					}
					new CircuitGarden(moist, null, manual, fertilised, new ItemStack(insulatedTube, 1, 128 * insulate), icon);
					new CircuitGarden(moist, EnumAcidity.ACID, manual, fertilised, new ItemStack(insulatedTube, 1, 1 + 128 * insulate), icon);
					new CircuitGarden(moist, EnumAcidity.NEUTRAL, manual, fertilised, new ItemStack(insulatedTube, 1, 2 + 128 * insulate), icon);
					new CircuitGarden(moist, EnumAcidity.ALKALINE, manual, fertilised, new ItemStack(insulatedTube, 1, 3 + 128 * insulate), icon);
				}
			}
		}
	}

	@Override
	public void postInit() {
		for (int mat = 0; mat < 4; ++mat) {
			for (int insulate = 0; insulate < 6; ++insulate) {
				ItemStack tubes = new ItemStack(insulatedTube, 2, mat + 128 * insulate);
				ItemStack insulateStack = ItemInsulatedTube.getInsulateStack(tubes);
				ItemStack forestryTube = new ItemStack(Mods.Forestry.item("thermionicTubes"), 1, mat);
				GameRegistry.addShapelessRecipe(tubes, forestryTube, forestryTube, insulateStack);
			}
		}
	}
}
