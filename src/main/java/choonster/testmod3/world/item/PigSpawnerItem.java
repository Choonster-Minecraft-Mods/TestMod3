package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * A pig spawner item.
 *
 * @author Choonster
 */
public class PigSpawnerItem<T extends IPigSpawner> extends Item {
	/**
	 * A factory to create the {@link IPigSpawner}
	 */
	private final Supplier<T> spawnerFactory;

	/**
	 * The codec for the {@link IPigSpawner} type
	 */
	private final Codec<T> spawnerCodec;

	public PigSpawnerItem(final Supplier<T> spawnerFactory, final Codec<T> spawnerCodec, final Item.Properties properties) {
		super(properties);
		this.spawnerFactory = spawnerFactory;
		this.spawnerCodec = spawnerCodec;
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
		return PigSpawnerCapability.createProvider(spawnerFactory.get(), spawnerCodec);
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
	public int getBarWidth(final ItemStack stack) {
		return PigSpawnerCapability.getPigSpawner(stack)
				.filter(pigSpawner -> pigSpawner instanceof IPigSpawnerFinite)
				.map(pigSpawner -> {
					final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
					final int maxNumPigs = pigSpawnerFinite.getMaxNumPigs();
					return Math.round(13.0f - ((float) (maxNumPigs - pigSpawnerFinite.getNumPigs()) / maxNumPigs) * 13.0f);
				})
				.orElse(super.getBarWidth(stack));
	}
}
