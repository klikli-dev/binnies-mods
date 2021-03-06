package binnie.botany;

import binnie.core.util.I18N;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabBotany extends CreativeTabs {
	public static CreativeTabs instance = new CreativeTabBotany();

	public CreativeTabBotany() {
		super("Botany");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel() {
		return getTabLabel();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTabLabel() {
		return I18N.localise("botany.tab.botany");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem() {
		return new ItemStack(Blocks.RED_FLOWER, 1, 5).getItem();
	}
}
