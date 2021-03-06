package binnie.core.gui.controls.core;

import binnie.core.gui.*;
import binnie.core.gui.geometry.Area;
import binnie.core.gui.geometry.Point;
import binnie.core.gui.minecraft.Window;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Control extends Widget implements ITooltipHelp, ITooltip {
	public int extraLevel;
	List<String> helpStrings;
	List<String> tooltipStrings;

	public Control(IWidget parent, int xPos, int yPos, int width, int height) {
		super(parent);
		this.helpStrings = new ArrayList<>();
		this.tooltipStrings = new ArrayList<>();
		this.extraLevel = 0;
		this.setPosition(new Point(xPos, yPos));
		this.setSize(new Point(width, height));
		this.initialise();
	}

	public Control(IWidget parent, Area area) {
		this(parent, area.xPos(), area.yPos(), area.width(), area.height());
	}

	protected void initialise() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onUpdateClient() {
	}

	@Nonnull
	@Override
	public IWidget getParent() {
		//noinspection ConstantConditions
		return super.getParent();
	}

	public void addHelp(final String string) {
		this.helpStrings.add(string);
	}

	public void addHelp(final String[] strings) {
		for (final String string : strings) {
			this.addHelp(string);
		}
	}

	public void addTooltip(final String string) {
		this.addAttribute(Attribute.MOUSE_OVER);
		this.tooltipStrings.add(string);
	}

	public void addTooltip(final String[] strings) {
		for (final String string : strings) {
			this.addTooltip(string);
		}
	}

	@Override
	public int getLevel() {
		return this.extraLevel + super.getLevel();
	}

	@Override
	public void getHelpTooltip(final Tooltip tooltip) {
		tooltip.add(this.helpStrings);
	}

	@Override
	public void getTooltip(final Tooltip tooltip) {
		tooltip.add(this.tooltipStrings);
	}

	public Window getWindow() {
		return (Window) this.getTopParent();
	}
}
