package binnie.core.gui.fieldkit;

import binnie.Binnie;
import binnie.core.gui.Attribute;
import binnie.core.gui.CraftGUI;
import binnie.core.gui.ITooltip;
import binnie.core.gui.Tooltip;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.events.EventValueChanged;
import binnie.core.gui.events.EventWidget;
import binnie.core.gui.geometry.Point;
import binnie.core.gui.resource.Texture;
import binnie.core.gui.resource.minecraft.StandardTexture;
import binnie.core.texture.BinnieCoreTexture;
import forestry.api.genetics.IChromosomeType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ControlChromoPicker extends Control implements ITooltip {
	Texture Selected;
	Texture Texture;
	IChromosomeType type;
	ControlChromosome parent;

	public ControlChromoPicker(ControlChromosome parent, int x, int y, IChromosomeType chromo) {
		super(parent, x, y, 16, 16);
		Selected = new StandardTexture(160, 18, 16, 16, BinnieCoreTexture.GUI_PUNNETT);
		Texture = new StandardTexture(160, 34, 16, 16, BinnieCoreTexture.GUI_PUNNETT);
		type = chromo;
		addAttribute(Attribute.MOUSE_OVER);
		this.parent = parent;
		addSelfEventHandler(new EventWidget.StartMouseOver.Handler() {
			@Override
			public void onEvent(EventWidget.StartMouseOver event) {
				callEvent(new EventValueChanged<Object>(getWidget(), type));
			}
		});
		addSelfEventHandler(new EventWidget.EndMouseOver.Handler() {
			@Override
			public void onEvent(EventWidget.EndMouseOver event) {
				callEvent(new EventValueChanged<>(getWidget(), null));
			}
		});
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderBackground(int guiWidth, int guiHeight) {
		super.onRenderBackground(guiWidth, guiHeight);
		boolean selected = isMouseOver();
		Texture text = selected ? Selected : Texture;
		CraftGUI.RENDER.texture(text, Point.ZERO);
	}

	@Override
	public void getTooltip(Tooltip tooltip) {
		tooltip.add(Binnie.GENETICS.getSystem(parent.getRoot()).getChromosomeName(type));
	}
}
