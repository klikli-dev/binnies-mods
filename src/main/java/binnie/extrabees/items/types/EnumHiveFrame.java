package binnie.extrabees.items.types;

import binnie.core.util.I18N;
import binnie.extrabees.items.ItemHiveFrame;
import binnie.extrabees.utils.BeeModifierLogic;
import binnie.extrabees.utils.EnumBeeBooleanModifier;
import binnie.extrabees.utils.EnumBeeModifier;
import forestry.api.apiculture.*;
import forestry.apiculture.PluginApiculture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public enum EnumHiveFrame implements IHiveFrame, IBeeModifier {

	COCOA{
		@Override
		protected void init(ItemStack impregnatedFrame) {
			logic.setModifier(EnumBeeModifier.LIFESPAN, 0.75f, 0.25f);
			logic.setModifier(EnumBeeModifier.PRODUCTION, 1.5f, 5.0f);
			GameRegistry.addRecipe(new ItemStack(EnumHiveFrame.COCOA.item),
				" c ",
				"cFc",
				" c ",
				'F', impregnatedFrame,
				'c', new ItemStack(Items.DYE, 1, 3));
		}
	},
	CAGE{
		@Override
		protected void init(ItemStack impregnatedFrame) {
			logic.setModifier(EnumBeeModifier.TERRITORY, 0.5f, 0.1f);
			logic.setModifier(EnumBeeModifier.LIFESPAN, 0.75f, 0.5f);
			logic.setModifier(EnumBeeModifier.PRODUCTION, 0.75f, 0.5f);
			GameRegistry.addShapelessRecipe(new ItemStack(EnumHiveFrame.CAGE.item), impregnatedFrame, Blocks.IRON_BARS);
		}
	},
	SOUL(80){
		@Override
		protected void init(ItemStack impregnatedFrame) {
			logic.setModifier(EnumBeeModifier.MUTATION, 1.5f, 5.0f);
			logic.setModifier(EnumBeeModifier.LIFESPAN, 0.75f, 0.5f);
			logic.setModifier(EnumBeeModifier.PRODUCTION, 0.25f, 0.1f);
			GameRegistry.addShapelessRecipe(new ItemStack(EnumHiveFrame.SOUL.item), impregnatedFrame, Blocks.SOUL_SAND);
		}
	},
	CLAY{
		@Override
		protected void init(ItemStack impregnatedFrame) {
			logic.setModifier(EnumBeeModifier.LIFESPAN, 1.5f, 5.0f);
			logic.setModifier(EnumBeeModifier.MUTATION, 0.5f, 0.2f);
			logic.setModifier(EnumBeeModifier.PRODUCTION, 0.75f, 0.2f);
			GameRegistry.addRecipe(new ItemStack(EnumHiveFrame.CLAY.item),
				" c ",
				"cFc",
				" c ",
				'F', impregnatedFrame,
				'c', Items.CLAY_BALL);
		}
	},
	DEBUG{
		@Override
		protected void init(ItemStack impregnatedFrame) {
			logic.setModifier(EnumBeeModifier.LIFESPAN, 1.0E-4f, 1.0E-4f);
		}
	};

	private final Item item;
	private final int maxDamage;
	protected final BeeModifierLogic logic;

	EnumHiveFrame() {
		this(240);
	}

	EnumHiveFrame(int maxDamage) {
		this.maxDamage = maxDamage;
		this.logic = new BeeModifierLogic();
		this.item = new ItemHiveFrame(this).setRegistryName("hive_frame." + name().toLowerCase());
	}

	protected void init(ItemStack impregnatedFrame){

	}

	public static void init() {
		ItemStack impregnatedFrame = PluginApiculture.items.frameImpregnated.getItemStack();
		for(EnumHiveFrame frame : values()){
			frame.init(impregnatedFrame);
		}
	}

	public int getIconIndex() {
		return 55 + this.ordinal();
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	@Override
	public ItemStack frameUsed(final IBeeHousing house, final ItemStack frame, final IBee queen, final int wear) {
		frame.setItemDamage(frame.getItemDamage() + wear);
		if (frame.getItemDamage() >= frame.getMaxDamage()) {
			return null;
		}
		return frame;
	}

	@Override
	public float getTerritoryModifier(final IBeeGenome genome, final float currentModifier) {
		return this.logic.getModifier(EnumBeeModifier.TERRITORY, currentModifier);
	}

	@Override
	public float getMutationModifier(final IBeeGenome genome, final IBeeGenome mate, final float currentModifier) {
		return this.logic.getModifier(EnumBeeModifier.MUTATION, currentModifier);
	}

	@Override
	public float getLifespanModifier(final IBeeGenome genome, @Nullable final IBeeGenome mate, final float currentModifier) {
		return this.logic.getModifier(EnumBeeModifier.LIFESPAN, currentModifier);
	}

	@Override
	public float getProductionModifier(final IBeeGenome genome, final float currentModifier) {
		return this.logic.getModifier(EnumBeeModifier.PRODUCTION, currentModifier);
	}

	@Override
	public float getFloweringModifier(final IBeeGenome genome, final float currentModifier) {
		return this.logic.getModifier(EnumBeeModifier.FLOWERING, currentModifier);
	}

	@Override
	public float getGeneticDecay(final IBeeGenome genome, final float currentModifier) {
		return this.logic.getModifier(EnumBeeModifier.GENETIC_DECAY, currentModifier);
	}

	@Override
	public boolean isSealed() {
		return this.logic.getModifier(EnumBeeBooleanModifier.Sealed);
	}

	@Override
	public boolean isSelfLighted() {
		return this.logic.getModifier(EnumBeeBooleanModifier.SelfLighted);
	}

	@Override
	public boolean isSunlightSimulated() {
		return this.logic.getModifier(EnumBeeBooleanModifier.SunlightStimulated);
	}

	@Override
	public boolean isHellish() {
		return this.logic.getModifier(EnumBeeBooleanModifier.Hellish);
	}

	public String getName() {
		return I18N.localise("extrabees.item.frame." + this.toString().toLowerCase());
	}

	@Override
	public IBeeModifier getBeeModifier() {
		return this;
	}

	public Item getItem() {
		return item;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		this.logic.addInformation(stack, playerIn, tooltip, advanced);
	}

}
