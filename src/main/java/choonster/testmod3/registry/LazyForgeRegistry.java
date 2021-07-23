package choonster.testmod3.registry;

import net.minecraft.util.LazyValue;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Implementation of {@link IForgeRegistry} that delegates to an {@link IForgeRegistry} instance that's lazily loaded
 * from {@link GameRegistry}.
 *
 * @author Choonster
 * @deprecated Use {@link DeferredRegister#makeRegistry(String, Supplier)} instead.
 */
@Deprecated()
public class LazyForgeRegistry<V extends IForgeRegistryEntry<V>> implements IForgeRegistry<V> {
	private final LazyValue<IForgeRegistry<V>> registry;

	protected LazyForgeRegistry(final Class<V> registryType) {
		registry = new LazyValue<>(() ->
				Objects.requireNonNull(
						GameRegistry.findRegistry(registryType),
						() -> String.format("Registry of type %s not present", registryType.getName())
				)
		);
	}

	public static <V extends IForgeRegistryEntry<V>> LazyForgeRegistry<V> of(final Class<V> registryType) {
		return new LazyForgeRegistry<>(registryType);
	}

	private IForgeRegistry<V> getRegistry() {
		return registry.get();
	}

	@Override
	public ResourceLocation getRegistryName() {
		return getRegistry().getRegistryName();
	}

	@Override
	public Class<V> getRegistrySuperType() {
		return getRegistry().getRegistrySuperType();
	}

	@Override
	public void register(final V value) {
		getRegistry().register(value);
	}

	@SafeVarargs
	@Override
	public final void registerAll(final V... values) {
		getRegistry().registerAll(values);
	}

	@Override
	public boolean containsKey(final ResourceLocation key) {
		return getRegistry().containsKey(key);
	}

	@Override
	public boolean containsValue(final V value) {
		return getRegistry().containsValue(value);
	}

	@Override
	public boolean isEmpty() {
		return getRegistry().isEmpty();
	}

	@Override
	@Nullable
	public V getValue(final ResourceLocation key) {
		return getRegistry().getValue(key);
	}

	@Override
	@Nullable
	public ResourceLocation getKey(final V value) {
		return getRegistry().getKey(value);
	}

	@Override
	@Nullable
	public ResourceLocation getDefaultKey() {
		return getRegistry().getDefaultKey();
	}

	@Override
	@Nonnull
	public Set<ResourceLocation> getKeys() {
		return getRegistry().getKeys();
	}

	@Override
	@Nonnull
	public Collection<V> getValues() {
		return getRegistry().getValues();
	}

	@Override
	@Nonnull
	public Set<Map.Entry<RegistryKey<V>, V>> getEntries() {
		return getRegistry().getEntries();
	}

	@Override
	public <T> T getSlaveMap(final ResourceLocation slaveMapName, final Class<T> type) {
		return getRegistry().getSlaveMap(slaveMapName, type);
	}

	@Override
	public Iterator<V> iterator() {
		return getRegistry().iterator();
	}
}
