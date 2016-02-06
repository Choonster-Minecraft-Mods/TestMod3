package com.choonster.testmod3.item;

import com.choonster.testmod3.api.pigspawner.IPigSpawner;
import com.choonster.testmod3.api.pigspawner.IPigSpawnerFinite;
import com.choonster.testmod3.pigspawner.CapabilityPigSpawner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

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
	private final Supplier<IPigSpawner> spawnerFactory;

	public ItemPigSpawner(String name, Supplier<IPigSpawner> spawnerFactory) {
		super("pigSpawner." + name);
		this.spawnerFactory = spawnerFactory;
		setHasSubtypes(true);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new CapabilityPigSpawner.Provider(spawnerFactory.get());
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
}
