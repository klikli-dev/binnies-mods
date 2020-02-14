package binnie.botany.tile;

import binnie.Binnie;
import binnie.botany.api.gardening.EnumSoilType;
import binnie.botany.api.gardening.IGardeningManager;
import binnie.botany.api.genetics.*;
import binnie.botany.blocks.PlantType;
import binnie.botany.core.BotanyCore;
import binnie.botany.genetics.EnumFlowerColor;
import binnie.botany.genetics.Flower;
import binnie.botany.modules.ModuleFlowers;
import binnie.botany.modules.ModuleGardening;
import binnie.core.BinnieCore;
import com.mojang.authlib.GameProfile;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.core.IErrorState;
import forestry.api.genetics.IIndividual;
import forestry.api.genetics.IPollinatable;
import forestry.api.lepidopterology.IButterfly;
import forestry.api.lepidopterology.IButterflyNursery;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.EnumPlantType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TileEntityFlower extends TileEntity implements IPollinatable, IButterflyNursery {
	@Nullable
	IFlower flower;
	@Nullable
	GameProfile owner;
	int section;
	@Nullable
	RenderInfo renderInfo;
	@Nullable
	IButterfly caterpillar;
	int matureTime;

	public TileEntityFlower() {
		flower = null;
		section = 0;
		renderInfo = null;
		matureTime = 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtCompound) {
		if (nbtCompound.hasKey("Flower")) {
			flower = new Flower(nbtCompound.getCompoundTag("Flower"));
			if (flower.getAge() == 0) {
				flower.setFlowered(false);
			}
		}

		if (nbtCompound.hasKey("section")) {
			section = nbtCompound.getByte("section");
		}

		if (nbtCompound.hasKey("owner")) {
			owner = NBTUtil.readGameProfileFromNBT(nbtCompound.getCompoundTag("owner"));
		}

		if (nbtCompound.hasKey("CATER") && BinnieCore.isLepidopteryActive()) {
			matureTime = nbtCompound.getInteger("caterTime");
			caterpillar = Binnie.GENETICS.getButterflyRoot().getMember(nbtCompound.getCompoundTag("cater"));
		}

		readRenderInfo(nbtCompound);
		super.readFromNBT(nbtCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtCompound) {
		if (flower != null) {
			nbtCompound.setTag("Flower", flower.writeToNBT(new NBTTagCompound()));
		}

		if (owner != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			NBTUtil.writeGameProfile(nbt, owner);
			nbtCompound.setTag("owner", nbt);
		}

		if (caterpillar != null) {
			nbtCompound.setInteger("caterTime", matureTime);
			NBTTagCompound subcompound = new NBTTagCompound();
			caterpillar.writeToNBT(subcompound);
			nbtCompound.setTag("cater", subcompound);
		}

		nbtCompound.setByte("section", (byte) getSection());
		return super.writeToNBT(nbtCompound);
	}

	public void create(ItemStack stack, @Nullable GameProfile owner) {
		IFlower flower = BotanyCore.getFlowerRoot().getMember(stack);
		create(flower, owner);
	}

	public void create(IFlower flower, @Nullable GameProfile owner) {
		this.flower = flower;
		if (flower.getAge() == 0) {
			flower.setFlowered(false);
		}

		updateRender(true);
		this.owner = owner;
	}

	@Override
	public EnumPlantType getPlantType() {
		return EnumPlantType.Plains;
	}

	@Override
	@Nullable
	public IIndividual getPollen() {
		return getFlower();
	}

	@Override
	public boolean canMateWith(IIndividual individual) {
		return isBreeding()
				&& individual instanceof IFlower
				&& getFlower() != null
				&& getFlower().getMate() == null
				&& getFlower().hasFlowered()
				&& !getFlower().isGeneticEqual(individual);
	}

	@Override
	public void mateWith(IIndividual individual) {
		if (getFlower() == null || !(individual instanceof IFlower)) {
			return;
		}

		IAlleleFlowerSpecies primary = (IAlleleFlowerSpecies) individual.getGenome().getPrimary();
		IAlleleFlowerSpecies primary2 = getFlower().getGenome().getPrimary();
		if (primary == primary2 || worldObj.rand.nextInt(4) == 0) {
			getFlower().mate((IFlower) individual);
			worldObj.markBlockRangeForRenderUpdate(pos, pos);
		}
	}

	@Nullable
	public IFlower getFlower() {
		if (getSection() <= 0) {
			return flower;
		}

		TileEntity tile = worldObj.getTileEntity(pos.down(getSection()));
		if (tile instanceof TileEntityFlower) {
			return ((TileEntityFlower) tile).getFlower();
		}
		return null;
	}

	public boolean isBreeding() {
		Block roots = worldObj.getBlockState(getPos().down()).getBlock();
		return BotanyCore.getGardening().isSoil(roots);
	}

	public void randomUpdate(Random rand) {
		if (worldObj.getBlockState(pos).getBlock() != ModuleFlowers.flower) {
			invalidate();
			return;
		}
		if (getSection() > 0) {
			return;
		}
		if (flower == null) {
			return;
		}

		if (!isBreeding()) {
			return;
		}

		if(updateState(rand)){
			return;
		}

		IGardeningManager gardening = BotanyCore.getGardening();
		EnumSoilType soil = gardening.getSoilType(worldObj, pos.down());
		float chanceDispersal = 0.8f;
		chanceDispersal += 0.2f * flower.getGenome().getFertility();
		chanceDispersal *= 1.0f + soil.ordinal() * 0.5f;
		float chancePollinate = 1.0f;
		chancePollinate += 0.25f * flower.getGenome().getFertility();
		chancePollinate *= 1.0f + soil.ordinal() * 0.5f;
		float chanceSelfPollinate = 0.2f * chancePollinate;
		plantOffspring(rand, chanceDispersal);

		mateFlower(rand, chancePollinate, chanceSelfPollinate);
		spawnButterflies();
		matureCaterpillar();
		checkIfDead(false);
		updateRender(true);
	}

	private void mateFlower(Random rand, float chancePollinate, float chanceSelfPollinate){
		if (worldObj.rand.nextFloat() < chancePollinate && flower.hasFlowered() && !flower.isWilted()) {
			for (int a2 = 0; a2 < 4; ++a2) {
				int dx3;
				int dz2;
				for (dx3 = 0, dz2 = 0; dx3 == 0 && dz2 == 0; dx3 = rand.nextInt(5) - 2, dz2 = rand.nextInt(5) - 2) {
				}
				TileEntity tile = worldObj.getTileEntity(pos.add(dx3, 0, dz2));
				if (tile instanceof IPollinatable && ((IPollinatable) tile).canMateWith(getFlower())) {
					((IPollinatable) tile).mateWith(getFlower());
				}
			}
		}

		if (worldObj.rand.nextFloat() < chanceSelfPollinate && flower.hasFlowered() && flower.getMate() == null) {
			mateWith(getFlower());
		}
	}

	private void plantOffspring(Random rand, float chanceDispersal){
		if (worldObj.rand.nextFloat() < chanceDispersal && flower.hasFlowered() && !flower.isWilted()) {
			IFlowerGenome mate = flower.getMate();
			if (mate != null) {
				boolean dispersed = false;
				for (int a = 0; a < 5 && !dispersed; ++a) {
					int dx2;
					int dz;
					for (dx2 = 0, dz = 0; dx2 == 0 && dz == 0; dx2 = rand.nextInt(3) - 1, dz = rand.nextInt(3) - 1) {
					}

					Block b2 = worldObj.getBlockState(pos.add(dx2, -1, dz)).getBlock();
					if (worldObj.isAirBlock(pos.add(dx2, 0, dz)) && BotanyCore.getGardening().isSoil(b2)) {
						IFlower offspring = flower.getOffspring(worldObj, pos);
						if (offspring != null) {
							BotanyCore.getFlowerRoot().plant(worldObj, pos.add(dx2, 0, dz), offspring, getOwner());
							flower.removeMate();
							dispersed = true;
						}
					}
				}
			}
		}
	}

	private boolean updateState(Random rand){
		float light = worldObj.getLight(pos);
		if (light < 6.0f) {
			for (int offsetX = -2; offsetX <= 2; ++offsetX) {
				for (int offsetY = -2; offsetY <= 2; ++offsetY) {
					light -= (worldObj.canBlockSeeSky(pos.add(offsetX, 0, offsetY)) ? 0.0f : 0.5f);
				}
			}
		}

		IGardeningManager gardening = BotanyCore.getGardening();
		boolean canTolerate = gardening.canTolerate(getFlower(), worldObj, pos);
		if (rand.nextFloat() < getFlower().getGenome().getAgeChance()) {
			if (flower.getAge() < 1) {
				if (canTolerate && light > 6.0f) {
					doFlowerAge();
				}
			} else {
				doFlowerAge();
			}
		}

		if (canTolerate && flower.getAge() > 1 && !flower.isWilted() && light > 6.0f) {
			flower.setFlowered(true);
		}

		if (!canTolerate && flower.isWilted() && rand.nextInt(2 + Math.max(flower.getAge(), 2)) == 0) {
			kill();
			return true;
		}

		if (light < 2.0f && flower.isWilted()) {
			kill();
			return true;
		}

		if (!canTolerate || light < 1.0f) {
			flower.setWilted(true);
		} else {
			flower.setWilted(false);
		}

		return false;
	}

	private void doFlowerAge() {
		getFlower().age();
		if (getFlower().getAge() == 1) {
			IFlowerRoot flowerRoot = BotanyCore.getFlowerRoot();
			flowerRoot.onGrowFromSeed(worldObj, pos);
			if (getOwner() != null && getFlower() != null) {
				flowerRoot.getBreedingTracker(worldObj, getOwner()).registerBirth(getFlower());
			}
		}
	}

	private NBTTagCompound writeRenderInfo(NBTTagCompound tag) {
		if (renderInfo != null) {
			NBTTagCompound nbtRenderInfo = new NBTTagCompound();
			nbtRenderInfo.setByte("primary", (byte) renderInfo.primary.getID());
			nbtRenderInfo.setByte("secondary", (byte) renderInfo.secondary.getID());
			nbtRenderInfo.setByte("stem", (byte) renderInfo.stem.getID());
			nbtRenderInfo.setByte("type", (byte) renderInfo.type.ordinal());
			nbtRenderInfo.setByte("age", renderInfo.age);
			nbtRenderInfo.setByte("section", renderInfo.section);
			nbtRenderInfo.setBoolean("wilted", renderInfo.wilted);
			nbtRenderInfo.setBoolean("flowered", renderInfo.flowered);
			tag.setTag("renderinfo", nbtRenderInfo);
		}
		return tag;
	}

	private void readRenderInfo(NBTTagCompound tag) {
		if (tag.hasKey("renderinfo")) {
			NBTTagCompound infotag = tag.getCompoundTag("renderinfo");
			RenderInfo info = new RenderInfo();
			info.primary = EnumFlowerColor.values()[infotag.getByte("primary")].getFlowerColorAllele();
			info.secondary = EnumFlowerColor.values()[infotag.getByte("secondary")].getFlowerColorAllele();
			info.stem = EnumFlowerColor.values()[infotag.getByte("stem")].getFlowerColorAllele();
			info.type = binnie.botany.genetics.EnumFlowerType.values()[infotag.getByte("type")];
			info.age = infotag.getByte("age");
			info.section = infotag.getByte("section");
			info.wilted = infotag.getBoolean("wilted");
			info.flowered = infotag.getBoolean("flowered");
			setRender(info);
		}
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		// TODO client only call?
		readRenderInfo(pkt.getNbtCompound());
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		updateRender(true);
		return writeRenderInfo(super.getUpdateTag());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		if (renderInfo == null && getFlower() != null) {
			renderInfo = new RenderInfo(getFlower(), this);
		}
		return (renderInfo != null) ? new SPacketUpdateTileEntity(pos, 0, getUpdateTag()) : null;
	}

	public void updateRender(boolean update) {
		if (update && getFlower() != null && getFlower().getGenome() != null) {
			RenderInfo newInfo = new RenderInfo(getFlower(), this);
			if (renderInfo == null || !newInfo.equals(renderInfo)) {
				setRender(newInfo);
			}
		}

		TileEntity above = worldObj.getTileEntity(pos.up());
		if (above instanceof TileEntityFlower) {
			((TileEntityFlower) above).updateRender(true);
		}
	}

	public int getSection() {
		return section;
	}

	public void setSection(int i) {
		section = i;
		if (BinnieCore.getBinnieProxy().isSimulating(worldObj)) {
			updateRender(true);
		}
	}

	public ItemStack getItemStack() {
		IFlower flower = getFlower();
		if (flower == null) {
			return null;
		}
		return Binnie.GENETICS.getFlowerRoot().getMemberStack(flower, EnumFlowerStage.getStage(flower));
	}

	@Nullable
	private TileEntityFlower getRoot() {
		if (getSection() == 0) {
			return null;
		}
		TileEntity tile = worldObj.getTileEntity(pos.down(getSection()));
		return (tile instanceof TileEntityFlower) ? ((TileEntityFlower) tile) : null;
	}

	public void onShear() {
		if (getRoot() != null) {
			getRoot().onShear();
		}

		if (getFlower() == null || getFlower().getAge() <= 1) {
			return;
		}

		Random rand = new Random();
		IFlower cutting = (IFlower) getFlower().copy();
		cutting.setAge(0);
		ItemStack cuttingStack = BotanyCore.getFlowerRoot().getMemberStack(cutting, EnumFlowerStage.SEED);
		float f = 0.7f;
		double xPos = rand.nextFloat() * f + (1.0f - f) * 0.5;
		double yPos = rand.nextFloat() * f + (1.0f - f) * 0.5;
		double zPos = rand.nextFloat() * f + (1.0f - f) * 0.5;
		EntityItem entityItem = new EntityItem(worldObj, pos.getX() + xPos, pos.getY() + yPos, pos.getZ() + zPos, cuttingStack);
		entityItem.setPickupDelay(10);
		worldObj.spawnEntityInWorld(entityItem);
		for (int maxAge = getFlower().getMaxAge(), i = 0; i < maxAge; ++i) {
			if (rand.nextBoolean()) {
				getFlower().age();
				if (checkIfDead(true)) {
					return;
				}
			}
		}
	}

	public boolean checkIfDead(boolean wasCut) {
		if (getSection() != 0) {
			return getRoot().checkIfDead(wasCut);
		}

		EnumSoilType soil = BotanyCore.getGardening().getSoilType(worldObj, pos);
		int maxAge = (int) (flower.getMaxAge() * (1.0f + soil.ordinal() * 0.25f));
		if (flower.getAge() > maxAge) {
			if (!wasCut && flower.getMate() != null) {
				worldObj.setBlockToAir(pos);
				IFlower offspring = flower.getOffspring(worldObj, pos.down());
				TileEntity above = worldObj.getTileEntity(pos.up());
				if (above instanceof TileEntityFlower) {
					worldObj.setBlockToAir(pos.up());
				}
				BotanyCore.getFlowerRoot().plant(worldObj, pos, offspring, getOwner());
			} else {
				kill();
			}
			return true;
		}
		return false;
	}

	public void kill() {
		if (flower.getAge() > 0) {
			worldObj.setBlockState(pos, ModuleGardening.plant.getStateFromMeta(PlantType.DEAD_FLOWER.ordinal()), 2);
		} else {
			worldObj.setBlockToAir(pos);
		}

		for (int i = 1; worldObj.getTileEntity(pos.up(i)) instanceof TileEntityFlower; ++i) {
			worldObj.setBlockToAir(pos.up(i));
		}
	}

	public boolean onBonemeal() {
		if (getFlower() == null) {
			return false;
		}
		if (!isBreeding()) {
			return false;
		}
		if (getFlower().isWilted()) {
			return false;
		}

		//this.doFlowerAge();
		if (getFlower().getAge() > 1 && !getFlower().hasFlowered()) {
			getFlower().setFlowered(true);
		}
		checkIfDead(false);
		updateRender(true);
		return true;
	}

	@Nullable
	public GameProfile getOwner() {
		return owner;
	}

	public void setOwner(GameProfile ownerName) {
		owner = ownerName;
	}

	public void spawnButterflies() {
		/*if (!BinnieCore.isLepidopteryActive()) {
			return;
		}
		final int x = this.xCoord;
		final int y = this.yCoord;
		final int z = this.zCoord;
		final worldObj worldObj = this.worldObjObj;
		if (this.getCaterpillar() != null) {
			return;
		}
		if (worldObj.rand.nextFloat() >= this.getFlower().getGenome().getSappiness()) {
			return;
		}
		if (this.worldObjObj.rand.nextFloat() >= 0.2f) {
			return;
		}
		final IButterfly spawn = Binnie.Genetics.getButterflyRoot().getIndividualTemplates().get(this.worldObjObj.rand.nextInt(Binnie.Genetics.getButterflyRoot().getIndividualTemplates().size()));
		if (this.worldObjObj.rand.nextFloat() >= spawn.getGenome().getPrimary().getRarity() * 0.5f) {
			return;
		}
		if (this.worldObjObj.countEntities(EntityButterfly.class) > 100) {
			return;
		}
		if (!spawn.canSpawn(this.worldObjObj, x, y, z)) {
			return;
		}
		if (worldObj.isAirBlock(x, y + 1, z)) {
			this.attemptButterflySpawn(worldObj, spawn, x, y + 1, z);
		}
		else if (worldObj.isAirBlock(x - 1, y, z)) {
			this.attemptButterflySpawn(worldObj, spawn, x - 1, y, z);
		}
		else if (worldObj.isAirBlock(x + 1, y, z)) {
			this.attemptButterflySpawn(worldObj, spawn, x + 1, y, z);
		}
		else if (worldObj.isAirBlock(x, y, z - 1)) {
			this.attemptButterflySpawn(worldObj, spawn, x, y, z - 1);
		}
		else if (worldObj.isAirBlock(x, y, z + 1)) {
			this.attemptButterflySpawn(worldObj, spawn, x, y, z + 1);
		}*/
	}

	private void attemptButterflySpawn(World worldObj, IButterfly butterfly, double x, double y, double z) {
		if (BinnieCore.isLepidopteryActive()) {
			Binnie.GENETICS.getButterflyRoot().spawnButterflyInWorld(worldObj, butterfly.copy(), x, y + 0.10000000149011612, z);
		}
	}

	public GameProfile getOwnerName() {
		return getOwner();
	}

	@Override
	public EnumTemperature getTemperature() {
		return EnumTemperature.getFromValue(worldObj.getBiome(getPos()).getTemperature());
	}

	@Override
	public EnumHumidity getHumidity() {
		return EnumHumidity.getFromValue(worldObj.getBiome(getPos()).getRainfall());
	}

	public void setErrorState(int state) {
	}

	public int getErrorOrdinal() {
		return 0;
	}

	public boolean addProduct(ItemStack product, boolean all) {
		return false;
	}

	@Override
	@Nullable
	public IButterfly getCaterpillar() {
		return caterpillar;
	}

	@Override
	public void setCaterpillar(@Nullable IButterfly butterfly) {
		caterpillar = butterfly;
		matureTime = 0;
	}

	@Override
	public IIndividual getNanny() {
		return getFlower();
	}

	@Override
	public boolean canNurse(IButterfly butterfly) {
		return getFlower() != null && !getFlower().isWilted() && getFlower().getAge() > 1;
	}

	private void matureCaterpillar() {
		//TODO Spawn the caterpillar in a cocon on the next Tree or other thing
		/*if (this.getCaterpillar() == null) {
			return;
		}
        ++this.matureTime;
		if (this.matureTime >= this.caterpillar.getGenome().getLifespan() / (this.caterpillar.getGenome().getFertility() * 2) && this.caterpillar.canTakeFlight(this.worldObjObj, this.xCoord, this.yCoord, this.zCoord)) {
			if (this.worldObjObj.isAirBlock(this.xCoord, this.yCoord + 1, this.zCoord)) {
				this.attemptButterflySpawn(this.worldObjObj, this.caterpillar, this.xCoord, this.yCoord + 1, this.zCoord);
			}
			else if (this.worldObjObj.isAirBlock(this.xCoord - 1, this.yCoord, this.zCoord)) {
				this.attemptButterflySpawn(this.worldObjObj, this.caterpillar, this.xCoord - 1, this.yCoord, this.zCoord);
			}
			else if (this.worldObjObj.isAirBlock(this.xCoord + 1, this.yCoord, this.zCoord)) {
				this.attemptButterflySpawn(this.worldObjObj, this.caterpillar, this.xCoord + 1, this.yCoord, this.zCoord);
			}
			else if (this.worldObjObj.isAirBlock(this.xCoord, this.yCoord, this.zCoord - 1)) {
				this.attemptButterflySpawn(this.worldObjObj, this.caterpillar, this.xCoord, this.yCoord, this.zCoord - 1);
			}
			else if (this.worldObjObj.isAirBlock(this.xCoord, this.yCoord, this.zCoord + 1)) {
				this.attemptButterflySpawn(this.worldObjObj, this.caterpillar, this.xCoord, this.yCoord, this.zCoord + 1);
			}
			this.setCaterpillar(null);
		}*/
	}

	public void setRender(RenderInfo render) {
		renderInfo = render;
		section = renderInfo.section;
		if (!worldObj.isRemote) {
			IBlockState blockState = worldObj.getBlockState(pos);
			worldObj.notifyBlockUpdate(pos, blockState, blockState, 0);
		} else {
			worldObj.markBlockRangeForRenderUpdate(pos, pos);
		}
	}

	public int getAge() {
		return (renderInfo == null) ? 1 : renderInfo.age;
	}

	public int getRenderSection() {
		return (renderInfo == null) ? 1 : renderInfo.section;
	}

	public boolean isWilted() {
		return renderInfo != null && renderInfo.wilted;
	}

	public boolean isFlowered() {
		return renderInfo == null || renderInfo.flowered;
	}

	public int getPrimaryColour() {
		return (renderInfo == null)
				? EnumFlowerColor.Red.getFlowerColorAllele().getColor(false)
				: renderInfo.primary.getColor(isWilted());
	}

	public int getSecondaryColour() {
		return (renderInfo == null)
				? EnumFlowerColor.Red.getFlowerColorAllele().getColor(false)
				: renderInfo.secondary.getColor(isWilted());
	}

	public int getStemColour() {
		return (renderInfo == null)
				? EnumFlowerColor.Green.getFlowerColorAllele().getColor(false)
				: renderInfo.stem.getColor(isWilted());
	}

	public IFlowerType getType() {
		return (renderInfo == null)
				? binnie.botany.genetics.EnumFlowerType.POPPY
				: renderInfo.type;
	}

	@Override
	public Biome getBiome() {
		return worldObj.getBiome(getPos());
	}

	@Nullable
	public IErrorState getErrorState() {
		return null;
	}

	public void setErrorState(IErrorState state) {
	}

	public boolean setErrorCondition(boolean condition, IErrorState errorState) {
		return false;
	}

	public Set<IErrorState> getErrorStates() {
		return new HashSet<>();
	}

	@Override
	public BlockPos getCoordinates() {
		return getPos();
	}

	@Override
	public boolean isPollinated() {
		return isBreeding();
	}

	@Override
	public World getWorld() {
		return worldObj;
	}

	public static class RenderInfo {
		public IFlowerColor primary;
		public IFlowerColor secondary;
		public IFlowerColor stem;
		public IFlowerType type;
		public byte age;
		public boolean wilted;
		public boolean flowered;
		public byte section;

		public RenderInfo() {
		}

		public RenderInfo(IFlower flower, TileEntityFlower tile) {
			section = (byte) tile.getSection();
			primary = flower.getGenome().getPrimaryColor();
			secondary = flower.getGenome().getSecondaryColor();
			stem = flower.getGenome().getStemColor();
			age = (byte) flower.getAge();
			wilted = flower.isWilted();
			flowered = flower.hasFlowered();
			type = flower.getGenome().getType();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof RenderInfo) {
				RenderInfo o = (RenderInfo) obj;
				return o.age == age && o.wilted == wilted && o.flowered == flowered && o.primary == primary && o.secondary == secondary && o.stem == stem && o.type == type;
			}
			return super.equals(obj);
		}
	}
}
