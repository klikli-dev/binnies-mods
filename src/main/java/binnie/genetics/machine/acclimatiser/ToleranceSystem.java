package binnie.genetics.machine.acclimatiser;

import binnie.core.genetics.Gene;
import binnie.core.genetics.Tolerance;
import binnie.genetics.machine.inoculator.Inoculator;
import forestry.api.genetics.*;
import net.minecraft.item.ItemStack;

import java.util.Random;

class ToleranceSystem {
	String uid;
	IChromosomeType chromosomeOrdinal;
	ToleranceType type;

	ToleranceSystem(final String uid, final IChromosomeType chromosomeOrdinal, final ToleranceType type) {
		this.uid = uid;
		this.chromosomeOrdinal = chromosomeOrdinal;
		this.type = type;
	}

	public boolean canAlter(final ItemStack stack, final ItemStack acclim) {
		final IIndividual member = AlleleManager.alleleRegistry.getIndividual(stack);
		final IGenome genome = member.getGenome();
		final IAlleleTolerance tolAllele = (IAlleleTolerance) genome.getActiveAllele(this.chromosomeOrdinal);
		final Tolerance tol = Tolerance.get(tolAllele.getValue());
		final float effect = this.type.getEffect(acclim);
		return (effect > 0.0f && tol.getBounds()[1] < 5) || (effect < 0.0f && tol.getBounds()[0] > -5);
	}

	public ItemStack alter(final ItemStack stack, final ItemStack acc) {
		final Random rand = new Random();
		final float effect = this.type.getEffect(acc);
		if (rand.nextFloat() > Math.abs(effect)) {
			return stack;
		}
		final IIndividual member = AlleleManager.alleleRegistry.getIndividual(stack);
		final IGenome genome = member.getGenome();
		final IAlleleTolerance tolAllele = (IAlleleTolerance) genome.getActiveAllele(this.chromosomeOrdinal);
		final Tolerance tol = Tolerance.get(tolAllele.getValue());
		final Tolerance newTol = Acclimatiser.alterTolerance(tol, effect);
		if (rand.nextFloat() > 1.0f / (-newTol.getBounds()[0] + newTol.getBounds()[1])) {
			return stack;
		}
		final ISpeciesRoot root = AlleleManager.alleleRegistry.getSpeciesRoot(stack);
		Inoculator.setGene(new Gene(newTol.getAllele(), this.chromosomeOrdinal, root), stack, rand.nextInt(2));
		return stack;
	}
}
