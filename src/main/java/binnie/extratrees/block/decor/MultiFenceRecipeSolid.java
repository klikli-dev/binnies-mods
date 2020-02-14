package binnie.extratrees.block.decor;

import binnie.extratrees.block.WoodManager;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class MultiFenceRecipeSolid implements IRecipe {
	private ItemStack cached = null;

	@Override
	public boolean matches(final InventoryCrafting inv, final World world) {
		for (int row = 0; row < 3; ++row) {
			ItemStack a = inv.getStackInSlot(row * 3);
			ItemStack b = inv.getStackInSlot(row * 3 + 1);
			ItemStack c = inv.getStackInSlot(row * 3 + 2);
			if (a != null && b!=null && c != null) {
				FenceType type = WoodManager.getFenceType(a);
				FenceType type2 = WoodManager.getFenceType(b);
				FenceType type3 = WoodManager.getFenceType(c);
				if (type != null && type2 != null && type3 != null && type.equals(type2) && type.equals(type3)) {
					FenceDescription fenceDescription = WoodManager.getFenceDescription(a);
					if (fenceDescription != null) {
						FenceType fenceType = new FenceType(type.size, true, type.solid);
						this.cached = WoodManager.getFence(fenceDescription.getPlankType(), fenceDescription.getSecondaryPlankType(), fenceType, 2);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		return this.getRecipeOutput();
	}

	@Override
	public int getRecipeSize() {
		return 3;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.cached;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
