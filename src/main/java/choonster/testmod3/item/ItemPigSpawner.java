package choonster.testmod3.item;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
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
public class ItemPigSpawner extends ItemTestMod3 {
	/**
	 * A factory to create the {@link IPigSpawner}
	 */
	private Supplier<IPigSpawner> spawnerFactory;

	public ItemPigSpawner(String name) {
		super("pig_spawner_" + name);
		setHasSubtypes(true);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return CapabilityPigSpawner.createProvider(spawnerFactory.get());
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		final IPigSpawner pigSpawner = CapabilityPigSpawner.getPigSpawner(stack);

		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			return pigSpawnerFinite.getMaxNumPigs();
		}

		return super.getMaxDamage(stack);
	}

	@Override
	public boolean isDamaged(ItemStack stack) {
		final IPigSpawner pigSpawner = CapabilityPigSpawner.getPigSpawner(stack);

		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			return pigSpawnerFinite.getNumPigs() < pigSpawnerFinite.getMaxNumPigs();
		}

		return super.isDamaged(stack);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		final IPigSpawner pigSpawner = CapabilityPigSpawner.getPigSpawner(stack);

		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			final int maxNumPigs = pigSpawnerFinite.getMaxNumPigs();
			return (double) (maxNumPigs - pigSpawnerFinite.getNumPigs()) / maxNumPigs;
		}

		return super.getDurabilityForDisplay(stack);
	}

	public void setSpawnerFactory(Supplier<IPigSpawner> spawnerFactory) {
		if (this.spawnerFactory != null)
			throw new IllegalStateException("Attempt to replace the spawner factory of " + getRegistryName().toString());

		this.spawnerFactory = spawnerFactory;
	}
}
