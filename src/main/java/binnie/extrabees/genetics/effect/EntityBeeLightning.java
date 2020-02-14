package binnie.extrabees.genetics.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityBeeLightning extends EntityLightningBolt {

	private int lightningState;
	private int boltLivingTime;

	public EntityBeeLightning(final World world) {
		super(world, 0, 0, 0, true);
	}

	public EntityBeeLightning(final World world, final double x, final double y, final double z) {
		super(world, x, y, z, false);
		this.lightningState = 2;
		this.boltLivingTime = this.rand.nextInt(3) + 1;
	}

	@Override
	public void onUpdate() {
		this.onEntityUpdate();
		--this.lightningState;
		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				this.setDead();
			} else if (this.lightningState < -this.rand.nextInt(10)) {
				--this.boltLivingTime;
				this.lightningState = 1;
				this.boltVertex = this.rand.nextLong();
				final int i = MathHelper.floor_double(this.posX);
				final int j = MathHelper.floor_double(this.posY);
				final int k = MathHelper.floor_double(this.posZ);
				final BlockPos pos = new BlockPos(i, j, k);
				if (!this.worldObj.isRemote && this.worldObj.isAreaLoaded(pos, 10)) {
					if (this.worldObj.isAirBlock(pos) && Blocks.FIRE.canPlaceBlockAt(this.worldObj, pos)) {
						this.worldObj.setBlockState(pos, Blocks.FIRE.getDefaultState());
					}
				}
			}
		}
		if (this.lightningState >= 0) {
			if (this.worldObj.isRemote) {
				this.worldObj.setLastLightningBolt(2);
			} else {
				final double d0 = 3.0;
				final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - d0, this.posY - d0, this.posZ - d0, this.posX + d0, this.posY + 6.0 + d0, this.posZ + d0));
				for (Entity entity : list) {
					entity.onStruckByLightning(this);
				}
			}
		}
	}
}
