package choonster.testmod3.world.entity;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

/**
 * An arrow entity that behaves like the vanilla arrow but renders with a different texture.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2577561-custom-arrow
 *
 * @author Choonster
 */
public class ModArrow extends Arrow implements IEntityAdditionalSpawnData {
	public ModArrow(final EntityType<? extends ModArrow> entityType, final Level level) {
		super(entityType, level);
	}

	public ModArrow(final Level level, final LivingEntity shooter, final ItemStack pickupItemStack, @Nullable final ItemStack firedFromWeapon) {
		super(level, shooter, pickupItemStack, firedFromWeapon);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.MOD_ARROW.get();
	}

	@Override
	public void addEffect(final MobEffectInstance p_36871_) {
		// Mod arrows can't have potion effects
	}

	@Override
	protected void doPostHurtEffects(final LivingEntity p_36873_) {
		// Mod arrows can't have potion effects
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(ModItems.ARROW.get());
	}

	@Override
	public void writeSpawnData(final FriendlyByteBuf buffer) {
		final var shooter = getOwner();
		buffer.writeVarInt(shooter == null ? 0 : shooter.getId());
	}

	@Override
	public void readSpawnData(final FriendlyByteBuf additionalData) {
		final var shooter = level().getEntity(additionalData.readVarInt());
		if (shooter != null) {
			setOwner(shooter);
		}
	}
}
