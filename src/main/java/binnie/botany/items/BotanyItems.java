package binnie.botany.items;

import binnie.botany.modules.ModuleGardening;
import binnie.core.item.IItemMiscProvider;
import binnie.core.util.I18N;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Locale;

public enum BotanyItems implements IItemMiscProvider {
	POWDER_ASH,
	POWDER_PULP,
	POWDER_MULCH,
	POWDER_SULPHUR,
	POWDER_FERTILISER,
	POWDER_COMPOST,
	MORTAR,
	WEEDKILLER;

	String name;

	BotanyItems() {
		name = name().toLowerCase(Locale.ENGLISH);
	}

	@Override
	public void addInformation(List<String> tooltip) {
		if(this == MORTAR){
			tooltip.add(I18N.localise("item.botany.mortar.tooltip"));
		}
	}

	@Override
	public String getDisplayName(ItemStack stack) {
		return I18N.localise("item.botany." + name + ".name");
	}

	@Override
	public String getModelPath() {
		return name;
	}

	@Override
	public ItemStack get(int size) {
		return new ItemStack(ModuleGardening.misc, size, ordinal());
	}

	@Override
	public boolean isActive() {
		return true;
	}
}
