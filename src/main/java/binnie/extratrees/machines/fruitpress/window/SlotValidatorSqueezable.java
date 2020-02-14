package binnie.extratrees.machines.fruitpress.window;

import binnie.core.machines.inventory.SlotValidator;
import binnie.core.util.I18N;
import binnie.extratrees.machines.fruitpress.recipes.FruitPressRecipeManager;
import net.minecraft.item.ItemStack;

public class SlotValidatorSqueezable extends SlotValidator {
	public SlotValidatorSqueezable() {
		super(SlotValidator.spriteBlock);
	}

	@Override
	public boolean isValid(final ItemStack itemStack) {
		return FruitPressRecipeManager.isInput(itemStack);
	}

	@Override
	public String getTooltip() {
		return I18N.localise("extratrees.machine.machine.press.tooltips.fruit");
	}
}
