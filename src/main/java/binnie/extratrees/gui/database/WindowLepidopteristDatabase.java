package binnie.extratrees.gui.database;

import binnie.Binnie;
import binnie.core.AbstractMod;
import binnie.core.gui.database.*;
import binnie.core.gui.minecraft.Window;
import binnie.extratrees.ExtraTrees;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WindowLepidopteristDatabase extends WindowAbstractDatabase {
	public WindowLepidopteristDatabase(final EntityPlayer player, final Side side, final boolean nei) {
		super(player, side, nei, Binnie.GENETICS.mothBreedingSystem, 160);
	}

	public static Window create(final EntityPlayer player, final Side side, final boolean nei) {
		return new WindowLepidopteristDatabase(player, side, nei);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void addTabs() {
		new PageSpeciesOverview(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "butterfly.species.overview", 0));
		new PageSpeciesClassification(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "butterfly.species.classification", 0));
		new PageSpeciesImage(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "butterfly.species.specimen", 0));
		new PageSpeciesResultant(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "butterfly.species.resultant", 0));
		new PageSpeciesMutations(this.getInfoPages(Mode.SPECIES), new DatabaseTab(ExtraTrees.instance, "butterfly.species.further", 0));
		new PageBranchOverview(this.getInfoPages(Mode.BRANCHES), new DatabaseTab(ExtraTrees.instance, "butterfly.branches.overview", 0));
		new PageBranchSpecies(this.getInfoPages(Mode.BRANCHES), new DatabaseTab(ExtraTrees.instance, "butterfly.branches.species", 0));
		new PageBreeder(this.getInfoPages(Mode.BREEDER), this.getUsername(), new DatabaseTab(ExtraTrees.instance, "butterfly.breeder", 0));
	}

	@Override
	protected AbstractMod getMod() {
		return ExtraTrees.instance;
	}

	@Override
	protected String getBackgroundTextureName() {
		return "MothDatabase";
	}
}
