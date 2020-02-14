package binnie.extratrees.machines.brewery.recipes;

import binnie.extratrees.api.recipes.IBreweryCrafting;
import forestry.api.core.INbtWritable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class BreweryCrafting implements INbtWritable, IBreweryCrafting {
	@Nullable
	public FluidStack inputFluid;
	@Nullable
	public ItemStack[] inputGrains;
	public ItemStack ingredient;
	public ItemStack yeast;

	public BreweryCrafting( @Nullable final FluidStack inputFluid, final ItemStack ingredient, @Nullable final ItemStack[] inputGrains, final ItemStack yeast) {
		this.inputFluid = inputFluid;
		this.inputGrains = ((inputGrains == null) ? new ItemStack[3] : inputGrains);
		this.ingredient = ingredient;
		this.yeast = yeast;
	}

	public static BreweryCrafting create(final NBTTagCompound nbt) {
		FluidStack inputFluid = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid"));
		ItemStack ingredient = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("ingr"));
		ItemStack[] inputGrains = new ItemStack[]{
				ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("in1")),
				ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("in2")),
				ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("in3"))
		};
		ItemStack yeast = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("yeast"));
		return new BreweryCrafting(inputFluid, ingredient, inputGrains, yeast);
	}
	
	public boolean hasInputGrainsEmpty(){
		return inputGrains == null || (isEmptyStack(inputGrains[0]) && isEmptyStack(inputGrains[1]) && isEmptyStack(inputGrains[2]));
	}

	private boolean isEmptyStack(ItemStack stack){
		return stack == null;
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
		if (this.inputFluid != null) {
			final NBTTagCompound fluidTag = new NBTTagCompound();
			this.inputFluid.writeToNBT(fluidTag);
			nbt.setTag("fluid", fluidTag);
		}
		nbt.setTag("ingr", this.getNBT(this.ingredient));
		nbt.setTag("in1", this.getNBT(this.inputGrains[0]));
		nbt.setTag("in2", this.getNBT(this.inputGrains[1]));
		nbt.setTag("in3", this.getNBT(this.inputGrains[2]));
		nbt.setTag("yeast", this.getNBT(this.yeast));
		return nbt;
	}

	private NBTTagCompound getNBT(final ItemStack ingr) {
		if (ingr == null) {
			return new NBTTagCompound();
		}
		final NBTTagCompound nbt = new NBTTagCompound();
		ingr.writeToNBT(nbt);
		return nbt;
	}

	@Nullable
	public FluidStack getInputFluid() {
		return inputFluid;
	}

	@Nullable
	public ItemStack[] getInputGrains() {
		return inputGrains;
	}

	public ItemStack getIngredient() {
		return ingredient;
	}

	public ItemStack getYeast() {
		return yeast;
	}
}
