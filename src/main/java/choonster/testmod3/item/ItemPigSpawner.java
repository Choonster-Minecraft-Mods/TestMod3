package choonster.testmod3.item;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * A pig spawner item.
 *
 * @author Choonster
 */
public class ItemPigSpawner extends Item {
	/**
	 * A factory to create the {@link IPigSpawner}
	 */
	private final Supplier<IPigSpawner> spawnerFactory;

	public ItemPigSpawner(final Supplier<IPigSpawner> spawnerFactory) {
		this.spawnerFactory = spawnerFactory;

		setHasSubtypes(true);
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
		return CapabilityPigSpawner.createProvider(spawnerFactory.get());
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public int getMaxDamage(final ItemStack stack) {
		final IPigSpawner pigSpawner = CapabilityPigSpawner.getPigSpawner(stack);

		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			return pigSpawnerFinite.getMaxNumPigs();
		}

		return super.getMaxDamage(stack);
	}

	@Override
	public boolean isDamaged(final ItemStack stack) {
		final IPigSpawner pigSpawner = CapabilityPigSpawner.getPigSpawner(stack);

		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			return pigSpawnerFinite.getNumPigs() < pigSpawnerFinite.getMaxNumPigs();
		}

		return super.isDamaged(stack);
	}

	@Override
	public double getDurabilityForDisplay(final ItemStack stack) {
		final IPigSpawner pigSpawner = CapabilityPigSpawner.getPigSpawner(stack);

		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			final int maxNumPigs = pigSpawnerFinite.getMaxNumPigs();
			return (double) (maxNumPigs - pigSpawnerFinite.getNumPigs()) / maxNumPigs;
		}

		return super.getDurabilityForDisplay(stack);
	}
}
