package binnie.extrabees.items;

import binnie.extrabees.items.types.IEBEnumItem;
import forestry.api.core.IItemModelRegister;
import forestry.api.core.IModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemProduct extends Item implements IItemModelRegister {

	protected IEBEnumItem[] types;

	public ItemProduct(final IEBEnumItem[] types) {
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.types = types;
	}

	public IEBEnumItem get(final ItemStack stack) {
		final int i = stack.getItemDamage();
		if (i >= 0 && i < this.types.length) {
			return this.types[i];
		}
		return this.types[0];
	}

	@Override
	public String getItemStackDisplayName(final ItemStack itemstack) {
		return this.get(itemstack).getName(itemstack);
	}

	@Override
	public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
		for (final IEBEnumItem type : this.types) {
			if (type.isActive()) {
				subItems.add(new ItemStack(this, 1, type.ordinal()));
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("all")
	public void registerModel(Item item, IModelManager manager) {
		for (IEBEnumItem type : types) {
			ModelLoader.setCustomModelResourceLocation(item, type.ordinal(), new ModelResourceLocation(getRegistryName(), "inventory"));
		}
	}
}
