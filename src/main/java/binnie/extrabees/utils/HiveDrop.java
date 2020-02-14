package binnie.extrabees.utils;

import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IHiveDrop;
import forestry.api.genetics.IAllele;
import forestry.apiculture.genetics.IBeeDefinition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

public class HiveDrop implements IHiveDrop {

	private IAllele[] template;
	private List<ItemStack> extra;
	private int chance;

	public HiveDrop(IBeeDefinition species, final int chance) {
		this(species.getTemplate(), new ArrayList<>(), chance);
	}

	public HiveDrop(final IAlleleBeeSpecies species, final int chance) {
		this(Utils.getBeeRoot().getTemplate(species.getUID()), new ArrayList<>(), chance);
	}

	public HiveDrop(IAllele[] template, final List<ItemStack> extra, final int chance) {
		this.extra = extra;
		this.template = template;
		this.chance = chance;
	}

	@Override
	public List<ItemStack> getExtraItems(IBlockAccess world, BlockPos pos, int fortune) {
		final List<ItemStack> ret = new ArrayList<>();
		for (final ItemStack stack : this.extra) {
			ret.add(stack.copy());
		}
		return ret;
	}

	@Override
	public double getChance(IBlockAccess world, BlockPos pos, int fortune) {
		return chance;
	}

	@Override
	public double getIgnobleChance(IBlockAccess world, BlockPos pos, int fortune) {
		return 0.5; //TODO implement
	}

	@Override
	public IBee getBeeType(IBlockAccess world, BlockPos pos) {
		return Utils.getBeeRoot().getBee(Utils.getBeeRoot().templateAsGenome(this.template));
	}
}
