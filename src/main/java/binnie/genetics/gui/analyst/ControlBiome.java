package binnie.genetics.gui.analyst;

import binnie.core.gui.ITooltip;
import binnie.core.gui.IWidget;
import binnie.core.gui.Tooltip;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.geometry.Point;
import binnie.core.gui.renderer.RenderUtil;
import forestry.api.core.ForestryAPI;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ControlBiome extends Control implements ITooltip {
	private final Biome biome;
	private String iconCategory;

	public ControlBiome(IWidget parent, int x, int y, int w, int h, Biome biome) {
		super(parent, x, y, w, h);
		iconCategory = "plains";
		this.biome = biome;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderBackground(int guiWidth, int guiHeight) {
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MOUNTAIN) || BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.HILLS)) {
			iconCategory = "hills";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SANDY)) {
			iconCategory = "desert";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SNOWY)) {
			iconCategory = "snow";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FOREST)) {
			iconCategory = "forest";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP)) {
			iconCategory = "swamp";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE)) {
			iconCategory = "jungle";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.COLD) && BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.FOREST)) {
			iconCategory = "taiga";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.MUSHROOM)) {
			iconCategory = "mushroom";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.OCEAN)) {
			iconCategory = "ocean";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.NETHER)) {
			iconCategory = "nether";
		}
		if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.END)) {
			iconCategory = "end";
		}
		TextureAtlasSprite sprite = ForestryAPI.textureManager.getDefault("habitats/" + iconCategory);
		RenderUtil.drawGuiSprite(Point.ZERO, sprite);
	}

	@Override
	public void getTooltip(Tooltip tooltip) {
		tooltip.add(biome.getBiomeName().replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2"));
	}
}
