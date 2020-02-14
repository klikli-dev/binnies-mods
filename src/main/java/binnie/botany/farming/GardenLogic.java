package binnie.botany.farming;

import binnie.Binnie;
import binnie.botany.api.gardening.*;
import binnie.botany.core.BotanyCore;
import binnie.botany.modules.ModuleGardening;
import binnie.botany.tile.TileEntityFlower;
import binnie.core.liquid.ManagerLiquid;
import forestry.api.farming.FarmDirection;
import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmHousing;
import forestry.api.farming.IFarmable;
import forestry.core.owner.IOwnedTile;
import forestry.core.utils.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class GardenLogic extends FarmLogic {
	List<IFarmable> farmables;
	private EnumMoisture moisture;
	@Nullable
	private EnumAcidity acidity;
	private boolean fertilised;
	private String name;
	private List<ItemStack> produce;
	private ItemStack icon;

	public GardenLogic(EnumMoisture moisture, @Nullable EnumAcidity acidity, boolean isManual, boolean isFertilised, ItemStack icon, String name) {
		this.isManual = isManual;
		this.moisture = moisture;
		this.acidity = acidity;
		this.fertilised = isFertilised;
		this.icon = icon;
		this.name = name;

		produce = new ArrayList<>();
		farmables = new ArrayList<>();
		farmables.add(new FarmableFlower());
		farmables.add(new FarmableVanillaFlower());
	}

	@Override
	public int getFertilizerConsumption() {
		return fertilised ? 8 : 2;
	}

	@Override
	public int getWaterConsumption(float hydrationModifier) {
		return (int) (moisture.ordinal() * 40 * hydrationModifier);
	}

	@Override
	public boolean isAcceptedResource(ItemStack itemstack) {
		IGardeningManager gardening = BotanyCore.getGardening();
		return gardening.isSoil(itemstack.getItem()) ||
				itemstack.getItem() == Item.getItemFromBlock(Blocks.SAND) ||
				itemstack.getItem() == Item.getItemFromBlock(Blocks.DIRT) ||
				gardening.isFertiliser(EnumFertiliserType.ACID, itemstack) ||
				gardening.isFertiliser(EnumFertiliserType.ALKALINE, itemstack);
	}

	@Override
	public List<ItemStack> collect(World world, IFarmHousing farmHousing) {
		List<ItemStack> products = produce;
		produce = new ArrayList<>();
		return products;
	}

	@Override
	public boolean cultivate(World world, IFarmHousing farmHousing, BlockPos pos, FarmDirection direction, int extent) {
		return maintainSoil(world, pos, direction, extent, farmHousing) ||
				(!isManual && maintainWater(world, pos, direction, extent, farmHousing)) ||
				maintainCrops(world, pos.up(), direction, extent, farmHousing);
	}

	private boolean isWaste(ItemStack stack) {
		return stack.getItem() == Item.getItemFromBlock(Blocks.DIRT);
	}

	private boolean maintainSoil(World world, BlockPos pos, FarmDirection direction, int extent, IFarmHousing housing) {
		IGardeningManager gardening = BotanyCore.getGardening();
		for (int i = 0; i < extent; ++i) {
			BlockPos position = pos.offset(direction.getFacing(), i);
			if (fertilised && gardening.isSoil(getBlock(world, position))) {
				IBlockSoil soil = (IBlockSoil) getBlock(world, position);
				if (soil.fertilise(world, position, EnumSoilType.FLOWERBED)) {
					continue;
				}
			}

			if (getBlock(world, position.up()) == ModuleGardening.plant) {
				world.setBlockToAir(position.up());
			} else {
				if (acidity != null && gardening.isSoil(getBlock(world, position))) {
					IBlockSoil soil = (IBlockSoil) getBlock(world, position);
					EnumAcidity pH = soil.getPH(world, position);
					if (pH.ordinal() < acidity.ordinal()) {
						ItemStack stack = getAvailableFertiliser(housing, EnumFertiliserType.ALKALINE);
						if (stack != null && soil.setPH(world, position, EnumAcidity.values()[pH.ordinal() + 1])) {
							List<ItemStack> resources = new ArrayList<>();
							resources.add(stack);
							housing.getFarmInventory().removeResources((ItemStack[])resources.toArray());
							continue;
						}
					}
					if (pH.ordinal() > acidity.ordinal()) {
						ItemStack stack = getAvailableFertiliser(housing, EnumFertiliserType.ACID);
						if (stack != null && soil.setPH(world, position, EnumAcidity.values()[pH.ordinal() - 1])) {
							List<ItemStack> resources = new ArrayList<>();
							resources.add(stack);
							housing.getFarmInventory().removeResources((ItemStack[])resources.toArray());
							continue;
						}
					}
				}

				if (!isAirBlock(world, position) && !BlockUtil.isReplaceableBlock(getBlockState(world, position), world, position)) {
					ItemStack block = getAsItemStack(world, position);
					ItemStack loam = getAvailableLoam(housing);
					if (isWaste(block) && loam != null) {
						produce.addAll(Blocks.DIRT.getDrops(world, position, Block.getBlockFromItem(block.getItem()).getStateFromMeta(block.getItemDamage()), 0));
						setBlock(world, position, Blocks.AIR, 0);
						return trySetSoil(world, position, loam, housing);
					}
				} else if (!isManual) {
					if (!isWaterBlock(world, position)) {
						if (i % 2 == 0) {
							return trySetSoil(world, position, housing);
						}
						FarmDirection cclock = FarmDirection.EAST;
						if (direction == FarmDirection.EAST) {
							cclock = FarmDirection.SOUTH;
						} else if (direction == FarmDirection.SOUTH) {
							cclock = FarmDirection.EAST;
						} else if (direction == FarmDirection.WEST) {
							cclock = FarmDirection.SOUTH;
						}
						BlockPos previous = position.offset(cclock.getFacing());
						ItemStack soil2 = getAsItemStack(world, previous);
						if (!gardening.isSoil(soil2.getItem())) {
							trySetSoil(world, position, housing);
						}
					}
				}
			}
		}
		return false;
	}

	private boolean maintainWater(World world, BlockPos pos, FarmDirection direction, int extent, IFarmHousing housing) {
		for (int i = 0; i < extent; ++i) {
			BlockPos position = pos.offset(direction.getFacing(), i);
			if (!isAirBlock(world, position) && !BlockUtil.isReplaceableBlock(getBlockState(world, position), world, position)) {
				continue;
			}
			if (isWaterBlock(world, position)) {
				continue;
			}

			boolean isEnclosed = true;
			if (world.isAirBlock(position.east())) {
				isEnclosed = false;
			} else if (world.isAirBlock(position.west())) {
				isEnclosed = false;
			} else if (world.isAirBlock(position.south())) {
				isEnclosed = false;
			} else if (world.isAirBlock(position.north())) {
				isEnclosed = false;
			}

			isEnclosed = (isEnclosed || moisture != EnumMoisture.DAMP);
			if (isEnclosed) {
				return trySetWater(world, position, housing);
			}
		}
		return false;
	}

	private ItemStack getAvailableLoam(IFarmHousing housing) {
		EnumMoisture[] moistures;
		if (moisture == EnumMoisture.DAMP) {
			moistures = new EnumMoisture[]{
					EnumMoisture.DAMP,
					EnumMoisture.NORMAL,
					EnumMoisture.DRY
			};
		} else if (moisture == EnumMoisture.DRY) {
			moistures = new EnumMoisture[]{
					EnumMoisture.DRY,
					EnumMoisture.DAMP,
					EnumMoisture.DRY
			};
		} else {
			moistures = new EnumMoisture[]{
					EnumMoisture.DRY,
					EnumMoisture.NORMAL,
					EnumMoisture.DAMP
			};
		}

		EnumAcidity[] acidities = {
				EnumAcidity.NEUTRAL,
				EnumAcidity.ACID,
				EnumAcidity.ALKALINE
		};

		for (EnumMoisture moist : moistures) {
			for (EnumAcidity acid : acidities) {
				for (Block type : new Block[]{ModuleGardening.flowerbed, ModuleGardening.loam, ModuleGardening.soil}) {
					int meta = acid.ordinal() * 3 + moist.ordinal();
					List<ItemStack> resources = new ArrayList<>();
					ItemStack resourceStack = new ItemStack(type, 1, meta);
					resources.add(resourceStack);
					if (housing.getFarmInventory().hasResources((ItemStack[])resources.toArray())) {
						return resourceStack;
					}
				}
			}
		}

		List<ItemStack> resources = new ArrayList<>();
		ItemStack resourceStack = new ItemStack(Blocks.DIRT);
		resources.add(resourceStack);
		if (housing.getFarmInventory().hasResources((ItemStack[])resources.toArray())) {
			return new ItemStack(Blocks.DIRT);
		}
		return null;
	}

	private boolean trySetSoil(World world, BlockPos position, IFarmHousing housing) {
		return trySetSoil(world, position, getAvailableLoam(housing), housing);
	}

	private boolean trySetSoil(World world, BlockPos position, ItemStack loam, IFarmHousing housing) {
		ItemStack copy = loam;
		if (loam == null) {
			return false;
		}

		if (loam.getItem() == Item.getItemFromBlock(Blocks.DIRT)) {
			loam = new ItemStack(ModuleGardening.soil, 1, 4);
		}

		setBlock(world, position, ((ItemBlock) loam.getItem()).getBlock(), loam.getItemDamage());
		List<ItemStack> resources = new ArrayList<>();
		resources.add(copy);
		housing.getFarmInventory().removeResources((ItemStack[])resources.toArray());
		return true;
	}

	private boolean trySetWater(World world, BlockPos position, IFarmHousing housing) {
		FluidStack water = Binnie.LIQUID.getFluidStack(ManagerLiquid.WATER, 1000);
		if (moisture == EnumMoisture.DAMP) {
			if (water == null || !housing.hasLiquid(water)) {
				return false;
			}
			setBlock(world, position, Blocks.WATER, 0);
			housing.removeLiquid(water);
			return true;
		}

		if (moisture != EnumMoisture.DRY) {
			return trySetSoil(world, position, housing);
		}
		ItemStack sand = new ItemStack(Blocks.SAND, 1);
		List<ItemStack> resources = new ArrayList<>();
		resources.add(sand);
		if (!housing.getFarmInventory().hasResources((ItemStack[])resources.toArray())) {
			return false;
		}
		setBlock(world, position, Blocks.SAND, 0);
		housing.getFarmInventory().removeResources((ItemStack[])resources.toArray());
		return true;
	}

	public void setIcon(ItemStack icon) {
		this.icon = icon;
	}

	@Override
	public boolean isAcceptedGermling(ItemStack itemstack) {
		for (IFarmable farmable : farmables) {
			if (farmable.isGermling(itemstack)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<ICrop> harvest(World world, BlockPos pos, FarmDirection direction, int extent) {
		if (isManual) {
			return Collections.emptyList();
		}
		return farmables.stream().map(farmable -> farmable.getCropAt(world, pos.up(), world.getBlockState(pos.up())))
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public ResourceLocation getTextureMap() {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ItemStack getIconItemStack() {
		return icon;
	}

	protected boolean maintainCrops(World world, BlockPos pos, FarmDirection direction, int extent, IFarmHousing housing) {
		IGardeningManager gardening = BotanyCore.getGardening();
		for (int i = 0; i < extent; ++i) {
			BlockPos position = pos.offset(direction.getFacing(), i);
			if (isAirBlock(world, position) || BlockUtil.isReplaceableBlock(getBlockState(world, position), world, position)) {
				ItemStack below = getAsItemStack(world, position.down());
				if (gardening.isSoil(below.getItem())) {
					return trySetCrop(world, position, housing);
				}
			}
		}
		return false;
	}

	private boolean trySetCrop(World world, BlockPos position, IFarmHousing housing) {
		for (IFarmable farmable : farmables) {
			if (!housing.plantGermling(farmable, world, position)) {
				continue;
			}

			if (housing instanceof IOwnedTile) {
				TileEntity tile = world.getTileEntity(position);
				if (tile instanceof TileEntityFlower) {
					TileEntityFlower flower = (TileEntityFlower) tile;
					IOwnedTile owned = (IOwnedTile) housing;

					flower.setOwner(owned.getOwnerHandler().getOwner());
				}
			}
			return true;
		}
		return false;
	}

	public ItemStack getAvailableFertiliser(IFarmHousing housing, EnumFertiliserType type) {
		for (ItemStack stack : BotanyCore.getGardening().getFertilisers(type)) {
			if (stack != null) {
				List<ItemStack> resources = new ArrayList<>();
				resources.add(stack);
				if (housing.getFarmInventory().hasResources((ItemStack[])resources.toArray())) {
					return stack;
				}
			}
		}
		return null;
	}
}
