package binnie.core.gui.minecraft.control;

import binnie.core.gui.*;
import binnie.core.gui.controls.core.Control;
import binnie.core.gui.resource.minecraft.CraftGUITexture;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class ControlUser extends Control implements ITooltip {
	String team;
	private String username;

	public ControlUser(final IWidget parent, final int x, final int y, final String username) {
		super(parent, x, y, 16, 16);
		this.username = "";
		this.team = "";
		this.addAttribute(Attribute.MOUSE_OVER);
		this.username = username;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onRenderBackground(int guiWidth, int guiHeight) {
		CraftGUI.RENDER.texture(CraftGUITexture.USER_BUTTON, this.getArea());
	}

	@Override
	public void getTooltip(final Tooltip tooltip) {
		tooltip.setType(Tooltip.Type.USER);
		tooltip.add("Owner");
		if (!Objects.equals(this.username, "")) {
			tooltip.add(this.username);
		}
		tooltip.setMaxWidth(200);
	}
}
