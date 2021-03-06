package binnie.botany.gui.database;

import binnie.botany.api.genetics.IColorMix;
import binnie.core.gui.*;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.database.DatabaseConstants;
import binnie.core.gui.geometry.Point;
import binnie.core.gui.resource.Texture;
import binnie.core.gui.resource.minecraft.CraftGUITextureSheet;
import binnie.core.gui.resource.minecraft.StandardTexture;
import binnie.core.util.I18N;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ControlColorMixSymbol extends Control implements ITooltip {
	static Texture MutationPlus = new StandardTexture(2, 94, 16, 16, CraftGUITextureSheet.CONTROLS_2);
	static Texture MutationArrow = new StandardTexture(20, 94, 32, 16, CraftGUITextureSheet.CONTROLS_2);
	IColorMix value;
	int type;

	protected ControlColorMixSymbol(IWidget parent, int x, int y, int type, IColorMix value) {
		super(parent, x, y, 16 + type * 16, 16);
		this.value = value;
		this.type = type;
		addAttribute(Attribute.MOUSE_OVER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderBackground(int guiWidth, int guiHeight) {
		super.onRenderBackground(guiWidth, guiHeight);
		if (type == 0) {
			CraftGUI.RENDER.texture(ControlColorMixSymbol.MutationPlus, Point.ZERO);
		} else {
			CraftGUI.RENDER.texture(ControlColorMixSymbol.MutationArrow, Point.ZERO);
		}
	}

	public void setValue(IColorMix value) {
		this.value = value;
		setColor(0xffffff);
	}

	@Override
	public void getTooltip(Tooltip tooltip) {
		if (type == 1) {
			float chance = value.getChance();
			tooltip.add(I18N.localise(DatabaseConstants.BOTANY_CONTROL_KEY + ".color_mix_symbol.chance", chance));
		}
	}
}
