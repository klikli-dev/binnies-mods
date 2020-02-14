package binnie.genetics.item;

import binnie.genetics.Genetics;
import binnie.genetics.core.GeneticsGUI;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemPunnettSquare extends Item {

	public ItemPunnettSquare() {
		this.setCreativeTab(CreativeTabs.TOOLS);
		this.setMaxStackSize(1);
		setRegistryName("punnett_square");
	}

	@Override
	public String getItemStackDisplayName(final ItemStack itemstack) {
		return "Punnett Square";
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (handIn == EnumHand.MAIN_HAND) {
			Genetics.proxy.openGui(GeneticsGUI.PUNNETT_SQUARE, playerIn, playerIn.getPosition());
		}
		return new ActionResult<>(EnumActionResult.PASS, itemStack);
	}
}
