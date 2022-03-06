package choonster.testmod3.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.*;
import java.util.function.Supplier;

/**
 * Copy of {@link  DeferredRegister} for non-Forge registries.
 *
 * @author Choonster
 */
public class DeferredVanillaRegister<T> {
	public static <B> DeferredVanillaRegister<B> create(final Registry<B> reg, final String modid) {
		return new DeferredVanillaRegister<>(reg, modid);
	}

	private final Registry<T> registry;
	private final String modid;
	private final Map<VanillaRegistryObject<T>, Supplier<? extends T>> entries = new LinkedHashMap<>();
	private final Set<VanillaRegistryObject<T>> entriesView = Collections.unmodifiableSet(entries.keySet());

	private boolean seenRegisterEvent = false;

	private DeferredVanillaRegister(final Registry<T> registry, final String modid) {
		this.registry = registry;
		this.modid = modid;
	}

	/**
	 * Adds a new supplier to the list of entries to be registered, and returns a {@link  VanillaRegistryObject} that will be populated with the created entry automatically.
	 *
	 * @param name     The new entry's name, it will automatically have the modid prefixed.
	 * @param supplier A factory for the new entry, it should return a new instance every time it is called.
	 * @return A VanillaRegistryObject that will be updated with when the entries in the registry change.
	 */
	public VanillaRegistryObject<T> register(final String name, final Supplier<T> supplier) {
		if (seenRegisterEvent) {
			throw new IllegalStateException("Cannot register new entries to DeferredVanillaRegister after RegistryEvent.Register has been fired.");
		}

		Objects.requireNonNull(name);
		Objects.requireNonNull(supplier);

		final ResourceLocation key = new ResourceLocation(modid, name);

		final VanillaRegistryObject<T> ret = VanillaRegistryObject.of(key, registry);

		if (entries.putIfAbsent(ret, supplier) != null) {
			throw new IllegalArgumentException("Duplicate registration " + name);
		}

		return ret;
	}

	/**
	 * Adds our event handler to the specified event bus, this MUST be called in order for this class to function.
	 *
	 * @param bus The Mod Specific event bus.
	 */
	public void register(final IEventBus bus) {
		register(bus, Block.class);
	}

	/**
	 * Adds our event handler to the specified event bus, this MUST be called in order for this class to function.
	 *
	 * @param bus               The Mod Specific event bus.
	 * @param registrySuperType The RegistryEvent.Register type to perform the registration in. This can be used to control registration order.
	 */
	public <U extends IForgeRegistryEntry<U>> void register(final IEventBus bus, final Class<? super U> registrySuperType) {
		this.<U>register(bus, registrySuperType, EventPriority.NORMAL);
	}

	/**
	 * Adds our event handler to the specified event bus, this MUST be called in order for this class to function.
	 *
	 * @param bus               The Mod Specific event bus.
	 * @param registrySuperType The RegistryEvent.Register type to perform the registration in. This can be used to control registration order.
	 * @param priority          The priority to register the event handler at. This can be used to control registration order.
	 */
	public <U extends IForgeRegistryEntry<U>> void register(final IEventBus bus, final Class<? super U> registrySuperType, final EventPriority priority) {
		bus.addGenericListener(registrySuperType, priority, (RegistryEvent.Register<U> event) -> addEntries());
	}

	/**
	 * @return The unmodifiable view of registered entries. Useful for bulk operations on all values.
	 */
	public Collection<VanillaRegistryObject<T>> getEntries() {
		return entriesView;
	}

	private void addEntries() {
		seenRegisterEvent = true;

		for (final var entry : entries.entrySet()) {
			Registry.register(registry, entry.getKey().getId(), entry.getValue().get());
			entry.getKey().updateReference(registry);
		}
	}
}
