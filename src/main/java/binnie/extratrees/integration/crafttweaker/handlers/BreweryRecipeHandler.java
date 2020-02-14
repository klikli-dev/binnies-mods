package binnie.extratrees.integration.crafttweaker.handlers;

import binnie.extratrees.api.recipes.ExtraTreesRecipeManager;
import binnie.extratrees.integration.crafttweaker.CraftTweakerUtil;
import binnie.extratrees.item.ExtraTreeItems;
import binnie.extratrees.machines.brewery.recipes.BrewedGrainRecipe;
import binnie.extratrees.machines.brewery.recipes.BreweryRecipe;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.api.liquid.ILiquidStack;
import minetweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.extratrees.Brewery")
public class BreweryRecipeHandler extends GenericRecipeHandler {

	@ZenMethod
	public static void addRecipe(ILiquidStack input, ILiquidStack output, @Optional IItemStack yeast){
		FluidStack inputStack = CraftTweakerUtil.getLiquidStack(input);
		FluidStack outputStack = CraftTweakerUtil.getLiquidStack(output);
		ItemStack yeastStack = ExtraTreeItems.Yeast.get(1);
		if(yeast != null) {
			yeastStack = CraftTweakerUtil.getItemStack(yeast);
		}
		addRecipe(new BreweryRecipe(inputStack, outputStack, yeastStack), ExtraTreesRecipeManager.breweryManager);
	}

	@ZenMethod
	public static void addGrainRecipe(IOreDictEntry grainOreName, ILiquidStack output, @Optional IOreDictEntry ingredientOreName, @Optional IItemStack yeast){
		FluidStack outputStack = CraftTweakerUtil.getLiquidStack(output);
		ItemStack yeastStack = ExtraTreeItems.Yeast.get(1);
		if(yeast != null) {
			yeastStack = CraftTweakerUtil.getItemStack(yeast);
		}
		addRecipe(new BrewedGrainRecipe(outputStack, grainOreName.getName(), ingredientOreName.getName(), yeastStack), ExtraTreesRecipeManager.breweryManager);
	}

	@ZenMethod
	public static void remove(IIngredient ingredient){
		if(ingredient instanceof IItemStack){
			removeRecipe(CraftTweakerUtil.getItemStack(ingredient), ExtraTreesRecipeManager.breweryManager);
		}else if(ingredient instanceof ILiquidStack){
			removeRecipe(CraftTweakerUtil.getLiquidStack(ingredient), ExtraTreesRecipeManager.breweryManager);
		}
	}

	@ZenMethod
	public static void removeInput(IIngredient ingredient){
		if(ingredient instanceof IItemStack){
			removeRecipeInput(CraftTweakerUtil.getItemStack(ingredient), ExtraTreesRecipeManager.breweryManager);
		}else if(ingredient instanceof ILiquidStack){
			removeRecipeInput(CraftTweakerUtil.getLiquidStack(ingredient), ExtraTreesRecipeManager.breweryManager);
		}
	}
}
