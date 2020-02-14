package binnie.botany.genetics;

import binnie.botany.api.BotanyAPI;
import binnie.botany.api.genetics.*;
import binnie.botany.genetics.allele.AlleleFlowerSpecies;
import binnie.botany.models.FlowerSpriteManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IClassification;

public class FlowerFactory implements IFlowerFactory {
	@Override
	public IAlleleFlowerSpeciesBuilder createSpecies(String uid, String unlocalizedName, String authority, String unlocalizedDescription, boolean isDominant, IClassification branch, String binomial, IFlowerType flowerType) {
		FlowerSpriteManager.initSprites(flowerType);
		return new AlleleFlowerSpecies(uid, unlocalizedName, authority, unlocalizedDescription, isDominant, branch, binomial, flowerType);
	}

	@Override
	public IFlowerMutationBuilder createMutation(IAlleleFlowerSpecies allele0, IAlleleFlowerSpecies allele1, IAllele[] result, int chance) {
		FlowerMutation mutation = new FlowerMutation(allele0, allele1, result, chance);
		BotanyAPI.flowerRoot.registerMutation(mutation);
		return mutation;
	}
}
