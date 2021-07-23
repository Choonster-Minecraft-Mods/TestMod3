package choonster.testmod3.world.entity;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

/**
 * An arrow entity that behaves like the vanilla arrow but renders with a different texture.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2577561-custom-arrow
 *
 * @author Choonster
 */
public class ModArrow extends Arrow implements IEntityAdditionalSpawnData {
	public ModArrow(final EntityType<? extends ModArrow> entityType, final Level world) {
		super(entityType, world);
	}

	public ModArrow(final Level world, final LivingEntity shooter) {
		super(world, shooter);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.MOD_ARROW.get();
	}

	@Override
	public void setEffectsFromItem(final ItemStack stack) {
		super.setEffectsFromItem(new ItemStack(Items.ARROW)); // Mod arrows can't have potion effects
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(ModItems.ARROW.get());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(final FriendlyByteBuf buffer) {
		final Entity shooter = getOwner();
		buffer.writeVarInt(shooter == null ? 0 : shooter.getId());
	}

	@Override
	public void readSpawnData(final FriendlyByteBuf additionalData) {
		final Entity shooter = level.getEntity(additionalData.readVarInt());
		if (shooter != null) {
			setOwner(shooter);
		}
	}
}
