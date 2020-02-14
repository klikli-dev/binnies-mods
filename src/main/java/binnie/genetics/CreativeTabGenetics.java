package binnie.genetics;

import binnie.core.util.I18N;
import binnie.genetics.item.GeneticsItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabGenetics extends CreativeTabs {
	public static CreativeTabs instance = new CreativeTabGenetics();

	public CreativeTabGenetics() {
		super("Genetics");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() {
		return getTabLabel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTabLabel() {
		return I18N.localise("genetics.tab.genetics");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return GeneticsItems.EMPTY_SERUM.get(1).getItem();
	}
}
