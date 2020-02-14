package binnie.extratrees.block;

import binnie.Constants;
import binnie.extratrees.block.property.PropertyETType;
import binnie.extratrees.genetics.ETTreeDefinition;
import forestry.api.arboriculture.IFruitProvider;
import forestry.api.arboriculture.ITreeGenome;
import forestry.api.core.IModelManager;
import forestry.arboriculture.blocks.BlockForestryLeaves;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Genetic leaves with no tile entity, used for worldgen trees.
 * Similar to decorative leaves, but these will drop saplings and can be used for pollination.
 */
public abstract class BlockETDefaultLeaves extends BlockForestryLeaves {
	private static final int VARIANTS_PER_BLOCK = 4;
	
	public static List<BlockETDefaultLeaves> create() {
		List<BlockETDefaultLeaves> blocks = new ArrayList<>();
		final int blockCount = PropertyETType.getBlockCount(VARIANTS_PER_BLOCK);
		for (int blockNumber = 0; blockNumber < blockCount; blockNumber++) {
			final PropertyETType variant = PropertyETType.create("variant", blockNumber, VARIANTS_PER_BLOCK);
			BlockETDefaultLeaves block = new BlockETDefaultLeaves(blockNumber) {
				@Override
				public PropertyETType getVariant() {
					return variant;
				}
			};
			blocks.add(block);
		}
		return blocks;
	}
	
	private final int blockNumber;
	
	public BlockETDefaultLeaves(int blockNumber) {
		this.blockNumber = blockNumber;
		PropertyETType variant = getVariant();
		setDefaultState(this.blockState.getBaseState()
				.withProperty(variant, variant.getFirstType())
				.withProperty(CHECK_DECAY, false)
				.withProperty(DECAYABLE, true));
		String name = "leaves.default." + blockNumber;
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Constants.EXTRA_TREES_MOD_ID, name));
	}
	
	public int getBlockNumber() {
		return blockNumber;
	}
	
	protected abstract PropertyETType getVariant();
	
	@Nullable
	public ETTreeDefinition getTreeDefinition(IBlockState blockState) {
		if (blockState.getBlock() == this) {
			return blockState.getValue(getVariant());
		} else {
			return null;
		}
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		ETTreeDefinition treeDefinition = getTreeDefinition(state);
		if (treeDefinition == null) {
			return 0;
		}
		return treeDefinition.getMetadata() - blockNumber * VARIANTS_PER_BLOCK;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
				.withProperty(getVariant(), getTreeType(meta))
				.withProperty(DECAYABLE, (meta & 4) == 0)
				.withProperty(CHECK_DECAY, (meta & 8) > 0);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = damageDropped(state);
		
		if (!state.getValue(DECAYABLE)) {
			i |= 4;
		}
		
		if (state.getValue(CHECK_DECAY)) {
			i |= 8;
		}
		
		return i;
	}
	
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, getVariant(), CHECK_DECAY, DECAYABLE);
	}
	
	public ETTreeDefinition getTreeType(int meta) {
		int variantCount = getVariant().getAllowedValues().size();
		int variantMeta = (meta % variantCount) + blockNumber * VARIANTS_PER_BLOCK;
		return ETTreeDefinition.byMetadata(variantMeta);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, ItemStack stack) {
		ETTreeDefinition type = getTreeType(meta);
		return getDefaultState().withProperty(getVariant(), type);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel(Item item, IModelManager manager) {
		for (IBlockState state : blockState.getValidStates()) {
			int meta = getMetaFromState(state);
			ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation("forestry:leaves.default." + blockNumber, "inventory"));
		}
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
		ETTreeDefinition treeDefinition = getTreeDefinition(state);
		if (treeDefinition == null) {
			treeDefinition = ETTreeDefinition.OrchardApple;
		}
		ITreeGenome genome = treeDefinition.getGenome();
		
		if (tintIndex == 0) {
			return genome.getPrimary().getLeafSpriteProvider().getColor(false);
		} else {
			IFruitProvider fruitProvider = genome.getFruitProvider();
			return fruitProvider.getDecorativeColor();
		}
	}
}