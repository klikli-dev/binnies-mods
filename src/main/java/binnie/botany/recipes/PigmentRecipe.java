package binnie.botany.recipes;

import binnie.Binnie;
import binnie.botany.api.genetics.IFlower;
import binnie.botany.modules.ModuleCeramic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class PigmentRecipe implements IRecipe {
	@Override
	public boolean matches(InventoryCrafting crafting, World world) {
		return getCraftingResult(crafting) != null;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting) {
		int n = 0;
		ItemStack stack = null;
		for (int i = 0; i < crafting.getSizeInventory(); ++i) {
			ItemStack stackInSlot = crafting.getStackInSlot(i);
			if (stackInSlot != null) {
				if (++n > 1) {
					return null;
				}
				if (Binnie.GENETICS.getFlowerRoot().isMember(stackInSlot)) {
					IFlower flower = Binnie.GENETICS.getFlowerRoot().getMember(stackInSlot);
					if (flower != null && flower.getAge() >= 1) {
						stack = new ItemStack(ModuleCeramic.pigment, 1, flower.getGenome().getPrimaryColor().getID());
					}
				}
			}
		}
		return stack;
	}

	@Override
	public int getRecipeSize() {
		return 1;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
