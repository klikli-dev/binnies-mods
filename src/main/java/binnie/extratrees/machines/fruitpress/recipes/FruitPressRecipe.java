package binnie.extratrees.machines.fruitpress.recipes;

import binnie.core.util.FluidStackUtil;
import binnie.extratrees.api.recipes.IFruitPressRecipe;
import com.google.common.base.Objects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.Collections;

public class FruitPressRecipe implements IFruitPressRecipe{
	private final ItemStack input;
	private final FluidStack output;

	public FruitPressRecipe(ItemStack input, FluidStack output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public FluidStack getOutput() {
		return output;
	}

	@Override
	public ItemStack getInput() {
		return input;
	}

	@Override
	public Collection<Object> getInputs() {
		return Collections.singleton(input);
	}

	@Override
	public Collection<Object> getOutputs() {
		return Collections.singleton(output);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("input", input)
			.add("output", FluidStackUtil.toString(output))
			.toString();
	}
}
