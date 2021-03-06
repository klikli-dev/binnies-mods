package binnie.genetics.gui.analyst.flower;

import binnie.botany.api.gardening.EnumAcidity;
import binnie.botany.api.gardening.EnumMoisture;
import binnie.botany.api.genetics.IFlower;
import binnie.botany.blocks.BlockSoil;
import binnie.botany.modules.ModuleGardening;
import binnie.core.genetics.Tolerance;
import binnie.core.gui.IWidget;
import binnie.core.gui.controls.ControlText;
import binnie.core.gui.controls.ControlTextCentered;
import binnie.core.gui.geometry.Area;
import binnie.core.gui.geometry.TextJustification;
import binnie.core.gui.minecraft.control.ControlItemDisplay;
import binnie.core.util.I18N;
import binnie.genetics.gui.analyst.AnalystConstants;
import binnie.genetics.gui.analyst.ControlAnalystPage;
import binnie.genetics.gui.analyst.ControlToleranceBar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class AnalystPageSoil extends ControlAnalystPage {
	public AnalystPageSoil(IWidget parent, Area area, IFlower flower) {
		super(parent, area);
		setColor(6697728);
		EnumMoisture moisture = flower.getGenome().getPrimary().getMoisture();
		forestry.api.genetics.EnumTolerance moistureTol = flower.getGenome().getToleranceMoisture();
		EnumAcidity pH = flower.getGenome().getPrimary().getPH();
		forestry.api.genetics.EnumTolerance pHTol = flower.getGenome().getTolerancePH();
		int y = 4;
		new ControlTextCentered(this, y, TextFormatting.UNDERLINE + getTitle()).setColor(getColor());
		y += 16;
		new ControlText(this, new Area(4, y, getWidth() - 8, 14), I18N.localise(AnalystConstants.SOIL_KEY + ".tolerance.moisture"), TextJustification.MIDDLE_CENTER).setColor(getColor());
		y += 12;
		createMoisture(this, (getWidth() - 100) / 2, y, 100, 10, moisture, moistureTol);
		y += 16;
		new ControlText(this, new Area(4, y, getWidth() - 8, 14), I18N.localise(AnalystConstants.SOIL_KEY + ".tolerance.ph"), TextJustification.MIDDLE_CENTER).setColor(getColor());
		y += 12;
		createAcidity(this, (getWidth() - 100) / 2, y, 100, 10, pH, pHTol);
		y += 16;
		new ControlText(this, new Area(4, y, getWidth() - 8, 14), I18N.localise(AnalystConstants.SOIL_KEY + ".recommended"), TextJustification.MIDDLE_CENTER).setColor(getColor());
		y += 12;
		EnumMoisture recomMoisture = EnumMoisture.NORMAL;
		boolean canTolNormal = Tolerance.canTolerate(moisture, EnumMoisture.NORMAL, moistureTol);
		boolean canTolDamp = Tolerance.canTolerate(moisture, EnumMoisture.DAMP, moistureTol);
		boolean canTolDry = Tolerance.canTolerate(moisture, EnumMoisture.DRY, moistureTol);
		if (canTolNormal) {
			if (canTolDamp && !canTolDry) {
				recomMoisture = EnumMoisture.DAMP;
			} else if (canTolDry && !canTolDamp) {
				recomMoisture = EnumMoisture.DRY;
			}
		} else {
			if (canTolDamp) {
				recomMoisture = EnumMoisture.DAMP;
			}
			if (canTolDry) {
				recomMoisture = EnumMoisture.DRY;
			}
		}
		EnumAcidity recomPH = EnumAcidity.NEUTRAL;
		boolean canTolNeutral = Tolerance.canTolerate(pH, EnumAcidity.NEUTRAL, pHTol);
		boolean canTolAcid = Tolerance.canTolerate(pH, EnumAcidity.ACID, pHTol);
		boolean canTolAlkaline = Tolerance.canTolerate(pH, EnumAcidity.ALKALINE, pHTol);
		if (canTolNeutral) {
			if (canTolAcid && !canTolAlkaline) {
				recomPH = EnumAcidity.ACID;
			} else if (canTolAlkaline && !canTolAcid) {
				recomPH = EnumAcidity.ALKALINE;
			}
		} else {
			if (canTolAcid) {
				recomPH = EnumAcidity.ACID;
			}
			if (canTolAlkaline) {
				recomPH = EnumAcidity.ALKALINE;
			}
		}
		ItemStack stack = new ItemStack(ModuleGardening.soil, 1, BlockSoil.getMeta(recomPH, recomMoisture));
		ControlItemDisplay recomSoil = new ControlItemDisplay(this, (getWidth() - 24) / 2, y, 24);
		recomSoil.setItemStack(stack);
		recomSoil.setTooltip();
		y += 32;
		new ControlText(this, new Area(4, y, getWidth() - 8, 14), I18N.localise(AnalystConstants.SOIL_KEY + ".other"), TextJustification.MIDDLE_CENTER).setColor(getColor());
		y += 12;
		List<ItemStack> stacks = new ArrayList<>();
		for (EnumAcidity a : EnumSet.range(EnumAcidity.ACID, EnumAcidity.ALKALINE)) {
			for (EnumMoisture b : EnumSet.range(EnumMoisture.DRY, EnumMoisture.DAMP)) {
				if (Tolerance.canTolerate(pH, a, pHTol) && Tolerance.canTolerate(moisture, b, moistureTol) && (a != recomPH || b != recomMoisture)) {
					stacks.add(new ItemStack(ModuleGardening.soil, 1, BlockSoil.getMeta(a, b)));
				}
			}
		}
		int soilListWidth = 17 * stacks.size() - 1;
		int soilListX = (getWidth() - soilListWidth) / 2;
		int t = 0;
		for (ItemStack soilStack : stacks) {
			ControlItemDisplay display = new ControlItemDisplay(this, soilListX + 17 * t++, y);
			display.setItemStack(soilStack);
			display.setTooltip();
		}
	}

	protected void createMoisture(IWidget parent, int x, int y, int w, int h, EnumMoisture value, forestry.api.genetics.EnumTolerance tol) {
		new ControlToleranceBar<EnumMoisture>(parent, x, y, w, h, EnumMoisture.class) {
			@Override
			protected String getName(EnumMoisture value) {
				return value.getLocalisedName(false);
			}

			@Override
			protected int getColour(EnumMoisture value) {
				return (new int[]{13434828, 6737151, 3368703})[value.ordinal()];
			}
		}.setValues(value, tol);
	}

	protected void createAcidity(IWidget parent, int x, int y, int w, int h, EnumAcidity value, forestry.api.genetics.EnumTolerance tol) {
		new ControlToleranceBar<EnumAcidity>(parent, x, y, w, h, EnumAcidity.class) {
			@Override
			protected String getName(EnumAcidity value) {
				return value.getLocalisedName(false);
			}

			@Override
			protected int getColour(EnumAcidity value) {
				return (new int[]{16711782, 65280, 26367})[value.ordinal()];
			}
		}.setValues(value, tol);
	}

	@Override
	public String getTitle() {
		return I18N.localise(AnalystConstants.SOIL_KEY + ".title");
	}
}
