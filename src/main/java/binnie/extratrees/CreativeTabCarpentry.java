package binnie.extratrees;

import binnie.core.Mods;
import binnie.core.util.I18N;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabCarpentry extends CreativeTabs {
	public static CreativeTabs instance = new CreativeTabCarpentry();

	public CreativeTabCarpentry() {
		super("Carpentry");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() {
		return getTabLabel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTabLabel() {
		return I18N.localise("extratrees.tab.carpentry");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return new ItemStack(Mods.Forestry.block("carpenter")).getItem();
	}
}
