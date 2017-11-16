package choonster.testmod3.tileentity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * A TileEntity that applies a potion effect to all entities within a certain distance of it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/35606-189-applying-a-potion-effect-via-a-te/
 *
 * @author Choonster
 */
public class TileEntityPotionEffect extends TileEntity implements ITickable {
	private static final int RADIUS = 2;

	@Override
	public void update() {
		if (!getWorld().isRemote) {
			final BlockPos pos = getPos();
			final AxisAlignedBB areaToSearch = new AxisAlignedBB(pos.add(-RADIUS, -RADIUS, -RADIUS), pos.add(RADIUS, RADIUS, RADIUS));
			final List<EntityLivingBase> entities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, areaToSearch);

			entities.stream().filter(entity -> !entity.isPotionActive(MobEffects.POISON)).forEach(entity -> entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 1)));
		}
	}
}
