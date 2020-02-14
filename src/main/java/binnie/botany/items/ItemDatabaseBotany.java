package binnie.botany.items;

import binnie.botany.Botany;
import binnie.botany.gui.BotanyGUI;
import binnie.core.util.I18N;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemDatabaseBotany extends ItemBotany implements IItemModelRegister {
	public ItemDatabaseBotany() {
		super("database");
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		super.getSubItems(itemIn, tab, subItems);
		subItems.add(new ItemStack(itemIn, 1, 1));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item, IModelManager manager) {
		manager.registerItemModel(item, 0, "botanist_database");
		manager.registerItemModel(item, 1, "botanist_database_master");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (itemStack.getMetadata() == 0) {
			Botany.proxy.openGui(BotanyGUI.DATABASE, playerIn, playerIn.getPosition());
		} else {
			Botany.proxy.openGui(BotanyGUI.DATABASE_MASTER, playerIn, playerIn.getPosition());
		}
		return super.onItemRightClick(itemStack, worldIn, playerIn, hand);
	}

	@Override
	public String getItemStackDisplayName(ItemStack i) {
		return I18N.localise("item.botany.database." + ((i.getItemDamage() == 0) ? "name" : "master.name"));
	}
}
