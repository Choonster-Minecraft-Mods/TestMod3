package com.choonster.testmod3.tileentity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
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
 * http://www.minecraftforge.net/forum/index.php/topic,35818.0.html
 *
 * @author Choonster
 */
public class TileEntityPotionEffect extends TileEntity implements ITickable {
	private static final int RADIUS = 2;

	@Override
	public void update() {
		if (!getWorld().isRemote) {

			BlockPos pos = getPos();
			AxisAlignedBB areaToSearch = new AxisAlignedBB(pos.add(-RADIUS, -RADIUS, -RADIUS), pos.add(RADIUS, RADIUS, RADIUS));
			List<EntityLivingBase> entities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, areaToSearch);

			for (EntityLivingBase entity : entities) {
				if (!entity.isPotionActive(MobEffects.poison)) {
					entity.addPotionEffect(new PotionEffect(MobEffects.poison, 200, 1));
				}
			}
		}
	}
}
