package binnie.extrabees.worldgen;

import binnie.extrabees.ExtraBees;
import binnie.extrabees.blocks.BlockExtraBeeHive;
import binnie.extrabees.blocks.type.EnumHiveType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenHiveRock extends WorldGenHive {

	public WorldGenHiveRock(int rate) {
		super(rate);
	}

	@Override
	public boolean generate(final World world, final Random random, final BlockPos pos) {
		final IBlockState block = world.getBlockState(pos);
		if (block.getBlock().isAir(block, world, pos)) {
			return false;
		}
		if (block.getBlock().isReplaceableOreGen(block, world, pos, BlockStateMatcher.forBlock(Blocks.STONE))) {
			world.setBlockState(pos, ExtraBees.hive.getDefaultState().withProperty(BlockExtraBeeHive.hiveType, EnumHiveType.Rock));
			return true;
		}
		return false;
	}
}
