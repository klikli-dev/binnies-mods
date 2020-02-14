package binnie.core.item;

import binnie.core.BinnieCore;
import binnie.core.gui.BinnieCoreGUI;
import forestry.api.core.Tabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemGenesis extends ItemCore {

	public ItemGenesis() {
		super("genesis");
		this.setCreativeTab(Tabs.tabApiculture);
		this.setUnlocalizedName("genesis");
		this.setMaxStackSize(1);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		BinnieCore.getBinnieProxy().openGui(BinnieCoreGUI.GENESIS, playerIn, playerIn.getPosition());
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack i) {
		return "Genesis";
	}
}
