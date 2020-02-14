package binnie.genetics.machine.analyser;

import binnie.Binnie;
import binnie.genetics.api.IItemAnalysable;
import com.mojang.authlib.GameProfile;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.ISpeciesRoot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Analyser {
	public static final int[] SLOT_RESERVE = new int[]{0, 1, 2, 3, 4, 5};
	public static final int SLOT_TARGET = 6;
	public static final int[] SLOT_FINISHED = new int[]{7, 8, 9, 10, 11, 12};
	public static final int SLOT_DYE = 13;

	public static boolean isAnalysable(final ItemStack stack) {
		final IIndividual ind = AlleleManager.alleleRegistry.getIndividual(stack);
		return ind != null || stack.getItem() instanceof IItemAnalysable || Binnie.GENETICS.getConversion(stack) != null;
	}

	public static boolean isAnalysed(final ItemStack stack) {
		if (stack == null) {
			return false;
		}
		final IIndividual ind = AlleleManager.alleleRegistry.getIndividual(stack);
		if (ind != null) {
			return ind.isAnalyzed();
		}
		return stack.getItem() instanceof IItemAnalysable && ((IItemAnalysable) stack.getItem()).isAnalysed(stack);
	}

	public static ItemStack analyse(ItemStack stack, World world, GameProfile username) {
		if (stack != null) {
			ItemStack conv = Binnie.GENETICS.getConversionStack(stack).copy();
			if (conv != null) {
				conv.stackSize = stack.stackSize;
				stack = conv;

			}
			ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(stack);
			if (root != null) {
				final IIndividual ind = root.getMember(stack);
				ind.analyze();
				IBreedingTracker breedingTracker = ind.getGenome().getSpeciesRoot().getBreedingTracker(world, username);
				breedingTracker.registerBirth(ind);
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				ind.writeToNBT(nbttagcompound);
				stack.setTagCompound(nbttagcompound);
				return stack;
			}
			if (stack.getItem() instanceof IItemAnalysable) {
				return ((IItemAnalysable) stack.getItem()).analyse(stack);
			}
		}
		return stack;
	}
}
