package choonster.testmod3.tileentity;

import choonster.testmod3.init.ModTileEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
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
public class PotionEffectTileEntity extends TileEntity implements ITickableTileEntity {
	private static final int RADIUS = 2;

	public PotionEffectTileEntity() {
		super(ModTileEntities.POTION_EFFECT);
	}

	@Override
	public void tick() {
		if (!getWorld().isRemote) {
			final BlockPos pos = getPos();
			final AxisAlignedBB areaToSearch = new AxisAlignedBB(pos.add(-RADIUS, -RADIUS, -RADIUS), pos.add(RADIUS, RADIUS, RADIUS));
			final List<LivingEntity> entities = getWorld().getEntitiesWithinAABB(LivingEntity.class, areaToSearch);

			entities.stream()
					.filter(entity -> !entity.isPotionActive(Effects.POISON))
					.forEach(entity -> entity.addPotionEffect(new EffectInstance(Effects.POISON, 200, 1)));
		}
	}
}
