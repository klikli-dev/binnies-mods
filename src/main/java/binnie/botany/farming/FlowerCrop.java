package binnie.botany.farming;

import forestry.api.farming.ICrop;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlowerCrop implements ICrop {
	List<ItemStack> drops = new ArrayList<>();
	BlockPos position;

	public FlowerCrop(BlockPos pos, ItemStack... drops) {
		Collections.addAll(this.drops, drops);
		position = pos;
	}

	@Nullable
	@Override
	public List<ItemStack> harvest() {
		return drops;
	}

	@Override
	public BlockPos getPosition() {
		return position;
	}
}
