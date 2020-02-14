package binnie.core.gui.minecraft.control;

import binnie.core.gui.*;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.resource.minecraft.CraftGUITexture;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ControlHelp extends Control implements ITooltip {
	public ControlHelp(final IWidget parent, final int x, final int y) {
		super(parent, x, y, 16, 16);
		this.addAttribute(Attribute.MOUSE_OVER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderBackground(int guiWidth, int guiHeight) {
		CraftGUI.RENDER.texture(CraftGUITexture.HELP_BUTTON, this.getArea());
	}

	@Override
	public void getTooltip(final Tooltip tooltip) {
		tooltip.setType(Tooltip.Type.HELP);
		tooltip.add("Help");
		tooltip.add("To activate help tooltips,");
		tooltip.add("hold down the tab key and");
		tooltip.add("mouse over controls.");
	}

	@Override
	public void getHelpTooltip(final Tooltip tooltip) {
		this.getTooltip(tooltip);
	}
}
