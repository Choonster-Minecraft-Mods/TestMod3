package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A pig spawner item.
 *
 * @author Choonster
 */
public class PigSpawnerItem extends Item {
	/**
	 * A factory to create the {@link IPigSpawner}
	 */
	private final Supplier<IPigSpawner> spawnerFactory;

	public PigSpawnerItem(final Supplier<IPigSpawner> spawnerFactory, final Item.Properties properties) {
		super(properties);
		this.spawnerFactory = spawnerFactory;
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
		//noinspection ConstantConditions
		if (PigSpawnerCapability.PIG_SPAWNER_CAPABILITY == null) return null;

		return PigSpawnerCapability.createProvider(spawnerFactory.get());
	}

	@Override
	public boolean canBeDepleted() {
		return true;
	}

	@Override
	public int getMaxDamage(final ItemStack stack) {
		return PigSpawnerCapability.getPigSpawner(stack)
				.filter(pigSpawner -> pigSpawner instanceof IPigSpawnerFinite)
				.map(pigSpawner -> {
					final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
					return pigSpawnerFinite.getMaxNumPigs();
				})
				.orElse(super.getMaxDamage(stack));
	}

	@Override
	public boolean isDamaged(final ItemStack stack) {
		return PigSpawnerCapability.getPigSpawner(stack)
				.filter(pigSpawner -> pigSpawner instanceof IPigSpawnerFinite)
				.map(pigSpawner -> {
					final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
					return pigSpawnerFinite.getNumPigs() < pigSpawnerFinite.getMaxNumPigs();
				})
				.orElse(super.isDamaged(stack));
	}

	@Override
	public double getDurabilityForDisplay(final ItemStack stack) {
		return PigSpawnerCapability.getPigSpawner(stack)
				.filter(pigSpawner -> pigSpawner instanceof IPigSpawnerFinite)
				.map(pigSpawner -> {
					final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
					final int maxNumPigs = pigSpawnerFinite.getMaxNumPigs();
					return (double) (maxNumPigs - pigSpawnerFinite.getNumPigs()) / maxNumPigs;
				})
				.orElse(super.getDurabilityForDisplay(stack));
	}
}
