package binnie.extrabees.blocks.type;

import forestry.api.apiculture.IHiveDrop;
import net.minecraft.util.IStringSerializable;

import java.util.ArrayList;
import java.util.List;

public enum EnumHiveType implements IStringSerializable {

	Water,
	Rock,
	Nether,
	Marble;

	public List<IHiveDrop> drops;

	EnumHiveType() {
		this.drops = new ArrayList<>();
	}

	@Override
	public String getName() {
		return this.name().toLowerCase();
	}

}
