package binnie.extratrees.machines.designer.window;

import binnie.core.machines.inventory.SlotValidator;
import binnie.extratrees.machines.ModuleMachine;
import binnie.extratrees.machines.designer.DesignerType;
import net.minecraft.item.ItemStack;

public class SlotValidatorBeeswax extends SlotValidator {
	DesignerType type;

	public SlotValidatorBeeswax(final DesignerType type) {
		super(ModuleMachine.spritePolish);
		this.type = type;
	}

	@Override
	public boolean isValid(final ItemStack itemStack) {
		return this.type.getSystem().getAdhesive().isItemEqual(itemStack);
	}

	@Override
	public String getTooltip() {
		return this.type.getSystem().getAdhesive().getDisplayName();
	}
}
