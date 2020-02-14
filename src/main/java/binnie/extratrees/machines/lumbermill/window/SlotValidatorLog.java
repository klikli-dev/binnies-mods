package binnie.extratrees.machines.lumbermill.window;

import binnie.core.machines.inventory.SlotValidator;
import binnie.core.util.I18N;
import binnie.extratrees.machines.lumbermill.recipes.LumbermillRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class SlotValidatorLog extends SlotValidator {
	private static final int logWoodId = OreDictionary.getOreID("logWood");

	public SlotValidatorLog() {
		super(SlotValidator.spriteBlock);
	}

	@Override
	public boolean isValid(final ItemStack itemStack) {
		ItemStack plank = LumbermillRecipeManager.getPlankProduct(itemStack);
		return plank != null;
	}

	@Override
	public String getTooltip() {
		return I18N.localise("extratrees.machine.machine.lumbermill.tooltips.logs");
	}
}
