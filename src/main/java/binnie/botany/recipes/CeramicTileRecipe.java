package binnie.botany.recipes;

import binnie.botany.ceramic.brick.CeramicBrickType;
import binnie.botany.modules.ModuleCeramic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

public class CeramicTileRecipe implements IRecipe {
	private ItemStack cached = null;

	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		cached = getCraftingResult(inv);
		return cached != null;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		Item ceramicBlock = Item.getItemFromBlock(ModuleCeramic.ceramic);
		Item ceramicTile = Item.getItemFromBlock(ModuleCeramic.ceramicTile);
		Item ceramicBrick = Item.getItemFromBlock(ModuleCeramic.ceramicBrick);
		Item mortar = ModuleCeramic.misc;
		List<ItemStack> stacks = new ArrayList<>();
		int ix = -1;
		int iy = -1;
		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack stack = inv.getStackInSlot(i);
			if (stack != null) {
				int x = i / 3;
				int y = i % 3;
				if (ix == -1) {
					ix = x;
					iy = y;
				}
				if (x - ix >= 2 || y - iy >= 2 || y < iy || x < ix) {
					return null;
				}
				if (stack.getItem() != ceramicBlock && stack.getItem() != ceramicTile && stack.getItem() != ceramicBrick && stack.getItem() != mortar) {
					return null;
				}
				stacks.add(stack);
			}
		}
		for (CeramicBrickType type : CeramicBrickType.VALUES) {
			ItemStack result = type.getRecipe(stacks);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return cached;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv) {
		return ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
