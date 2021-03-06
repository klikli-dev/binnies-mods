package binnie.extratrees.block;

import binnie.core.block.TileEntityMetadata;
import binnie.extratrees.api.CarpentryManager;
import binnie.extratrees.api.IDesignMaterial;
import binnie.extratrees.block.decor.FenceDescription;
import binnie.extratrees.block.decor.FenceType;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.WoodBlockKind;
import forestry.arboriculture.IWoodTyped;
import forestry.core.utils.Translator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class WoodManager {
	private static List<IPlankType> PLANK_TYPES;
	private static Map<IPlankType, ItemStack> PLANKS_STACKS;

	public static String getDisplayName(IWoodTyped wood, IWoodType woodType) {
		WoodBlockKind blockKind = wood.getBlockKind();

		String displayName;

		if (woodType instanceof EnumETLog || woodType instanceof EnumShrubLog) {
			String customUnlocalizedName = "tile.et." + blockKind + "." + woodType + ".name";
			if (Translator.canTranslateToLocal(customUnlocalizedName)) {
				displayName = Translator.translateToLocal(customUnlocalizedName);
			} else {
				String woodGrammar = Translator.translateToLocal("for." + blockKind + ".grammar");
				String woodTypeName = Translator.translateToLocal("et.trees.woodType." + woodType);

				displayName = woodGrammar.replaceAll("%TYPE", woodTypeName);
			}
		} else {
			throw new IllegalArgumentException("Unknown wood type: " + woodType);
		}

		if (wood.isFireproof()) {
			displayName = Translator.translateToLocalFormatted("tile.for.fireproof", displayName);
		}

		return displayName;
	}

	public static IPlankType getPlankType(final int index) {
		final IDesignMaterial wood = CarpentryManager.carpentryInterface.getWoodMaterial(index);
		if (wood instanceof IPlankType) {
			return (IPlankType) wood;
		}
		return PlankType.ExtraTreePlanks.Fir;
	}

	public static int getPlankTypeIndex(final IPlankType type) {
		final int index = CarpentryManager.carpentryInterface.getCarpentryWoodIndex(type);
		return (index < 0) ? 0 : index;
	}

	@Nullable
	public static FenceType getFenceType(final ItemStack stack) {
		final FenceDescription desc = getFenceDescription(stack);
		return (desc == null) ? null : desc.getFenceType();
	}

	@Nullable
	public static FenceDescription getFenceDescription(final ItemStack stack) {
		if (stack.getItem() == Item.getItemFromBlock(ModuleBlocks.blockMultiFence)) {
			final int damage = TileEntityMetadata.getItemDamage(stack);
			return getFenceDescription(damage);
		}
		for (final IPlankType type : getAllPlankTypes()) {
			if (type instanceof IFenceProvider) {
				final ItemStack f = ((IFenceProvider) type).getFence();
				if (ItemStack.areItemStacksEqual(stack, f)) {
					return new FenceDescription(new FenceType(0), type, type);
				}
			}
		}
		return null;
	}

	public static FenceDescription getFenceDescription(final int meta) {
		return new FenceDescription(meta);
	}

	public static FenceType getFenceType(final int meta) {
		return getFenceDescription(meta).getFenceType();
	}

	public static ItemStack getGate(final IPlankType plank) {
		if (plank == PlankType.VanillaPlanks.OAK) {
			return new ItemStack(Blocks.OAK_FENCE_GATE);
		}
		return TreeManager.woodAccess.getStack(plank.getWoodType(), WoodBlockKind.FENCE_GATE, false);
	}
	
	public static ItemStack getDoor(final IPlankType plank) {
		if (plank == PlankType.VanillaPlanks.OAK) {
			return new ItemStack(Items.OAK_DOOR);
		}else if (plank == PlankType.VanillaPlanks.BIRCH) {
			return new ItemStack(Items.BIRCH_DOOR);
		}else if (plank == PlankType.VanillaPlanks.SPRUCE) {
			return new ItemStack(Items.SPRUCE_DOOR);
		} else if (plank == PlankType.VanillaPlanks.BIG_OAK) {
			return new ItemStack(Items.DARK_OAK_DOOR);
		} else if (plank == PlankType.VanillaPlanks.JUNGLE) {
			return new ItemStack(Items.JUNGLE_DOOR);
		} else if (plank == PlankType.VanillaPlanks.ACACIA) {
			return new ItemStack(Items.ACACIA_DOOR);
		}
		return TreeManager.woodAccess.getStack(plank.getWoodType(), WoodBlockKind.DOOR, false);
	}

	public static ItemStack getFence(final IPlankType plank, final FenceType type, final int amount) {
		return getFence(plank, plank, type, amount);
	}

	public static ItemStack getFence(final IPlankType plank, final IPlankType plank2, final FenceType type, final int amount) {
		if (plank instanceof IFenceProvider && plank == plank2 && type.isPlain()) {
			final ItemStack original = ((IFenceProvider) plank).getFence();
			if (original != null) {
				original.stackSize = amount;
				return original;
			}
		}
		final int ord = type.ordinal();
		final int i = getPlankTypeIndex(plank) + 256 * ord;
		final ItemStack stack = TileEntityMetadata.getItemStack(ModuleBlocks.blockMultiFence, i + 65536 * getPlankTypeIndex(plank2));
		stack.stackSize = amount;
		return stack;
	}

	public static List<IPlankType> getAllPlankTypes() {
		if(PLANK_TYPES == null){
			PLANK_TYPES = new ArrayList<>();
			Collections.addAll(PLANK_TYPES, PlankType.ExtraTreePlanks.VALUES);
			Collections.addAll(PLANK_TYPES, PlankType.ForestryPlanks.values());
			Collections.addAll(PLANK_TYPES, PlankType.VanillaPlanks.values());
			//TODO: extrabiomes 1.10.2
			/*for (final IPlankType type : PlankType.ExtraBiomesPlank.values()) {
				if (type.getStack() != null) {
					list.add(type);
				}
			}*/
		}
		return PLANK_TYPES;
	}

	public static Map<IPlankType, ItemStack> getAllPlankStacks() {
		if (PLANKS_STACKS == null) {
			PLANKS_STACKS = new HashMap<>();
			for (IPlankType type : getAllPlankTypes()) {
				PLANKS_STACKS.put(type, type.getStack(false));
			}
		}
		return PLANKS_STACKS;
	}

	public static Collection<ItemStack> getAllPlankStacks(IPlankType type) {
		Map<IPlankType, ItemStack> planks = new HashMap<>(getAllPlankStacks());
		planks.remove(type);
		return planks.values();
	}

	@Nullable
	public static IPlankType getPlankType(ItemStack itemStack) {
		for (IPlankType type : getAllPlankTypes()) {
			if (type.getStack(false).isItemEqual(itemStack)) {
				return type;
			}
		}
		return null;
	}
}
