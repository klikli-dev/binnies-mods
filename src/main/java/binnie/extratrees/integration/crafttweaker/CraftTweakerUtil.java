package binnie.extratrees.integration.crafttweaker;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CraftTweakerUtil {

	public static ItemStack getItemStack(IIngredient item) {
		if(item == null)
			return null;

		Object internal = item.getInternal();
		if(internal == null || !(internal instanceof ItemStack)) {
			MineTweakerAPI.logError("Not a valid item stack: " + item);
		}
		return (ItemStack) internal;
	}

	public static FluidStack getLiquidStack(IIngredient stack) {
		if(stack == null || !(stack.getInternal() instanceof FluidStack))
			return null;

		return (FluidStack) stack.getInternal();
	}
}
