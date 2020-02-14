package binnie.core.machines.inventory;

import binnie.core.util.I18N;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class InventorySlot extends BaseSlot<ItemStack> {
	private ItemStack itemStack;
	private Type type;

	public InventorySlot(final int index, final String unlocName) {
		super(index, unlocName);
		this.itemStack = null;
		this.type = Type.Standard;
	}

	@Override
	@Nullable
	public ItemStack getContent() {
		return this.itemStack == null ? null : this.itemStack;
	}

	@Override
	public void setContent(final ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemStack getItemStack() {
		return this.itemStack;
	}

	public void setItemStack(final ItemStack duplicate) {
		this.setContent(duplicate);
	}

	@Override
	public boolean isEmpty() {
		return this.itemStack == null;
	}

	public ItemStack decrStackSize(final int amount) {
		if (this.itemStack == null) {
			return null;
		}
		if (this.itemStack.stackSize <= amount) {
			final ItemStack returnStack = this.itemStack.copy();
			this.itemStack = null;
			return returnStack;
		}
		final ItemStack returnStack = this.itemStack.copy();
		final ItemStack itemStack = this.itemStack;
		itemStack.stackSize -= amount;
		returnStack.stackSize = amount;
		return returnStack;
	}

	@Override
	public void readFromNBT(final NBTTagCompound slotNBT) {
		if (slotNBT.hasKey("item")) {
			final NBTTagCompound itemNBT = slotNBT.getCompoundTag("item");
			this.itemStack = ItemStack.loadItemStackFromNBT(itemNBT);
		} else {
			this.itemStack = null;
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound slotNBT) {
		final NBTTagCompound itemNBT = new NBTTagCompound();
		if (this.itemStack != null) {
			this.itemStack.writeToNBT(itemNBT);
		}
		slotNBT.setTag("item", itemNBT);
		return slotNBT;
	}

	@Override
	public SlotValidator getValidator() {
		return (SlotValidator) this.validator;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(final Type type) {
		this.type = type;
		if (type == Type.Recipe) {
			//this.setReadOnly();
			this.forbidInteraction();
		}
	}

	public boolean isRecipe() {
		return this.type == Type.Recipe;
	}

	@Override
	public String getName() {
		return I18N.localise("binniecore.gui.slot." + this.unlocName);
	}

	public enum Type {
		Standard,
		Recipe
	}
}