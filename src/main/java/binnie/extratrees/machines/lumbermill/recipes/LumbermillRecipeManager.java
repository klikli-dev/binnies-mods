package binnie.extratrees.machines.lumbermill.recipes;

import binnie.core.util.FakeCraftingWorld;
import binnie.core.util.OreDictionaryUtil;
import binnie.extratrees.api.recipes.ILumbermillManager;
import binnie.extratrees.api.recipes.ILumbermillRecipe;
import binnie.extratrees.integration.jei.RecipeUids;
import binnie.extratrees.integration.jei.lumbermill.LumbermillRecipeWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class LumbermillRecipeManager implements ILumbermillManager {
	//Map<input log item, Pair<input log, output planks>>
	private static Multimap<Item, ILumbermillRecipe> recipes = ArrayListMultimap.create();

	public static ItemStack getPlankProduct(final ItemStack logStack) {
		if (recipes.isEmpty()) {
			calculateProducts();
		}

		Item logItem = logStack.getItem();
		for (ILumbermillRecipe recipe : recipes.get(logItem)) {
			if (recipe.getInput().isItemEqual(logStack)) {
				return recipe.getOutput().copy();
			}
		}
		return null;
	}

	public static void calculateProducts() {
		CraftingManager craftingManager = CraftingManager.getInstance();
		InventoryCrafting fakeCraftingInventory = new InventoryCrafting(new FakeCraftingHandler(), 3, 3){};

		List<ItemStack> logs = OreDictionary.getOres("logWood");
		for (ItemStack logStack : logs) {
			if (logStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
				for (int i = 0; i < 16; i++) {
					ItemStack logCopy = logStack.copy();
					logCopy.stackSize = 1;
					logCopy.setItemDamage(i);

					fakeCraftingInventory.clear();
					fakeCraftingInventory.setInventorySlotContents(0, logCopy);
					ItemStack recipeOutput = craftingManager.findMatchingRecipe(fakeCraftingInventory, FakeCraftingWorld.getInstance());
					if (recipeOutput != null) {
						if (OreDictionaryUtil.hasOreName(recipeOutput, "plankWood")) {
							addLogToPlankRecipe(logCopy.copy(), recipeOutput.copy());
						}
					}
				}
			} else {
				fakeCraftingInventory.clear();
				fakeCraftingInventory.setInventorySlotContents(0, logStack.copy());

				ItemStack recipeOutput = craftingManager.findMatchingRecipe(fakeCraftingInventory, FakeCraftingWorld.getInstance());
				if (recipeOutput != null) {
					if (OreDictionaryUtil.hasOreName(recipeOutput, "plankWood")) {
						addLogToPlankRecipe(logStack.copy(), recipeOutput.copy());
					}
				}
			}
		}
	}

	private static void addLogToPlankRecipe(ItemStack logStack, ItemStack plankStack){
		Item logItem = logStack.getItem();
		plankStack.stackSize = (int) Math.ceil(plankStack.stackSize * 1.5f); // turns stack of 4 up to 6
		recipes.put(logItem, new LumbermillRecipe(logStack, plankStack));
	}

	@Override
	public void addRecipe(ItemStack input, ItemStack output) {
		Item logItem = input.getItem();
		recipes.put(logItem, new LumbermillRecipe(input, output));
	}

	@Override
	public boolean addRecipe(ILumbermillRecipe recipe) {
		Item item = recipe.getInput().getItem();
		return recipes.put(item, recipe);
	}

	@Override
	public boolean removeRecipe(ILumbermillRecipe recipe) {
		Item item = recipe.getInput().getItem();
		return recipes.get(item).remove(recipe);
	}

	@Override
	public Collection<ILumbermillRecipe> recipes() {
		return recipes.values();
	}

	@Override
	public String getJEICategory() {
		return RecipeUids.LUMBERMILL;
	}

	@Nullable
	@Override
	public Object getJeiWrapper(ILumbermillRecipe recipe) {
		if(!Loader.isModLoaded("jei")){
			return null;
		}
		return getWrapper(recipe);
	}

	@Optional.Method(modid = "jei")
	private Object getWrapper(ILumbermillRecipe recipe){
		return new LumbermillRecipeWrapper(recipe.getInput(), recipe.getOutput());
	}

	private static class FakeCraftingHandler extends Container {
		@Override
		public void onCraftMatrixChanged(IInventory inventoryIn) {
		}

		@Override
		public boolean canInteractWith(EntityPlayer playerIn) {
			return false;
		}
	}
}
