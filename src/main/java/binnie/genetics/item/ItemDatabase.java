package binnie.genetics.item;

import binnie.core.item.ItemCore;
import binnie.core.util.I18N;
import binnie.genetics.CreativeTabGenetics;
import binnie.genetics.Genetics;
import binnie.genetics.core.GeneticsGUI;
import forestry.api.core.IModelManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemDatabase extends ItemCore {

	public ItemDatabase() {
		super("geneticdatabase");
		this.setCreativeTab(CreativeTabGenetics.instance);
		this.setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel(Item item, IModelManager manager) {
		manager.registerItemModel(item, 0);
		manager.registerItemModel(item, 1, "geneticdatabase_master");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		super.getSubItems(itemIn, tab, subItems);
		subItems.add(new ItemStack(itemIn, 1, 1));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
		final GeneticsGUI id;
		if (isMaster(itemStack)) {
			id = GeneticsGUI.DATABASE_NEI;
		} else {
			id = GeneticsGUI.DATABASE;
		}

		Genetics.proxy.openGui(id, player, player.getPosition());

		return new ActionResult(EnumActionResult.PASS, itemStack);
	}

	@Override
	public String getItemStackDisplayName(ItemStack itemStack) {
		return I18N.localise("genetics." + (isMaster(itemStack) ? "item.database.master.name" : "item.database.name"));
	}

	protected boolean isMaster(ItemStack itemStack) {
		return itemStack.getItemDamage() > 0;
	}
}
