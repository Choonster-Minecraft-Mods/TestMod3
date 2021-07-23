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
		super(ModTileEntities.POTION_EFFECT.get());
	}

	@Override
	public void tick() {
		if (!getLevel().isClientSide) {
			final BlockPos pos = getBlockPos();
			final AxisAlignedBB areaToSearch = new AxisAlignedBB(pos.offset(-RADIUS, -RADIUS, -RADIUS), pos.offset(RADIUS, RADIUS, RADIUS));
			final List<LivingEntity> entities = getLevel().getEntitiesOfClass(LivingEntity.class, areaToSearch);

			entities.stream()
					.filter(entity -> !entity.hasEffect(Effects.POISON))
					.forEach(entity -> entity.addEffect(new EffectInstance(Effects.POISON, 200, 1)));
		}
	}
}
