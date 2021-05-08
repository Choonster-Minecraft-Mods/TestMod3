package choonster.testmod3.entity;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

/**
 * An arrow entity that behaves like the vanilla arrow but renders with a different texture.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2577561-custom-arrow
 *
 * @author Choonster
 */
public class ModArrowEntity extends ArrowEntity implements IEntityAdditionalSpawnData {
	public ModArrowEntity(final EntityType<? extends ModArrowEntity> entityType, final World world) {
		super(entityType, world);
	}

	public ModArrowEntity(final World world, final LivingEntity shooter) {
		super(world, shooter);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.MOD_ARROW.get();
	}

	@Override
	public void setPotionEffect(final ItemStack stack) {
		super.setPotionEffect(new ItemStack(Items.ARROW)); // Mod arrows can't have potion effects
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ModItems.ARROW.get());
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void writeSpawnData(final PacketBuffer buffer) {
		final Entity shooter = getShooter();
		buffer.writeVarInt(shooter == null ? 0 : shooter.getEntityId());
	}

	@Override
	public void readSpawnData(final PacketBuffer additionalData) {
		final Entity shooter = world.getEntityByID(additionalData.readVarInt());
		if (shooter != null) {
			setShooter(shooter);
		}
	}
}
