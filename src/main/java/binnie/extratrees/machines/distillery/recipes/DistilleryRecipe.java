package binnie.extratrees.machines.distillery.recipes;

import binnie.core.util.FluidStackUtil;
import binnie.extratrees.api.recipes.IDistilleryRecipe;
import com.google.common.base.Objects;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.Collections;

public class DistilleryRecipe implements IDistilleryRecipe {
	private final FluidStack input;
	private final FluidStack output;
	private final int level;

	public DistilleryRecipe(FluidStack input, FluidStack output, int level) {
		this.input = input;
		this.output = output;
		this.level = level;
	}

	@Override
	public FluidStack getOutput() {
		return output;
	}

	@Override
	public FluidStack getInput() {
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
	public int getLevel() {
		return level;
	}


	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("input", FluidStackUtil.toString(input))
			.add("output", FluidStackUtil.toString(output))
			.add("level", level)
			.toString();
	}
}
