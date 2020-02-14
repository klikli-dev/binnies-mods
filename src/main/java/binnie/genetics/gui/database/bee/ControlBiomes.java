package binnie.genetics.gui.database.bee;

import binnie.Binnie;
import binnie.core.gui.*;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.geometry.Area;
import binnie.core.gui.resource.minecraft.CraftGUITexture;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ControlBiomes extends Control implements ITooltip {
	List<Integer> tolerated;

	public ControlBiomes(IWidget parent, int x, int y, int width, int height) {
		super(parent, x, y, width * 16, height * 16);
		tolerated = new ArrayList<>();
		addAttribute(Attribute.MOUSE_OVER);
	}

	@Override
	public void getTooltip(Tooltip tooltip) {
		if (tolerated.isEmpty()) {
			return;
		}

		int x = (int) (getRelativeMousePosition().xPos() / 16.0f);
		int y = (int) (getRelativeMousePosition().yPos() / 16.0f);
		int i = x + y * 8;
		if (i >= tolerated.size()) {
			return;
		}

		Biome biome = Biome.getBiome(tolerated.get(i));
		if (biome != null) {
			tooltip.add(biome.getBiomeName());
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderForeground(int guiWidth, int guiHeight) {
		for (int i = 0; i < tolerated.size(); ++i) {
			int x = i % 8 * 16;
			int y = i / 8 * 16;
			if (Biome.getBiome(i) != null) {
				//TODO FIND COLOR
				//CraftGUI.Render.colour(Biome.getBiome(i).color);
			}
			CraftGUI.RENDER.texture(CraftGUITexture.BUTTON, new Area(x, y, 16, 16));
		}
	}

	public void setSpecies(IAlleleBeeSpecies species) {
		tolerated.clear();
		IBeeGenome genome = Binnie.GENETICS.getBeeRoot().templateAsGenome(Binnie.GENETICS.getBeeRoot().getTemplate(species.getUID()));
		IBee bee = Binnie.GENETICS.getBeeRoot().getBee(genome);
	}
}
