package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility methods for Forge registries.
 *
 * @author Choonster
 */
public class RegistryUtil {
	/**
	 * Get all of this mod's registry entries from the provided registry.
	 *
	 * @param registry The registry
	 * @param <T>      The registry type
	 * @return A Stream containing the registry entries
	 */
	public static <T> Stream<T> getModRegistryEntriesStream(final IForgeRegistry<T> registry) {
		return stream(registry)
				.filter(entry ->
						Optional.ofNullable(registry.getKey(entry))
								.filter(key -> key.getNamespace().equals(TestMod3.MODID))
								.isPresent()
				);
	}

	/**
	 * Get all of this mod's registry entries from the provided registry.
	 *
	 * @param registry The registry
	 * @param <T>      The registry type
	 * @return A Set containing the registry entries
	 */
	public static <T> Set<T> getModRegistryEntries(final IForgeRegistry<T> registry) {
		return getModRegistryEntriesStream(registry).collect(Collectors.toSet());
	}

	/**
	 * Gets a {@link Stream} of the registry's entries.
	 *
	 * @param registry The registry
	 * @param <T>      The registry type
	 * @return A Stream of the registry's entries
	 */
	public static <T> Stream<T> stream(final IForgeRegistry<T> registry) {
		return StreamSupport.stream(registry.spliterator(), false);
	}

	/**
	 * Gets the key of the registry entry, throwing an exception if it's not set.
	 *
	 * @param entry The registry entry
	 * @return The key
	 * @throws NullPointerException If the key is null
	 */
	public static <T> ResourceLocation getKey(final IForgeRegistry<T> registry, final T entry) {
		return Preconditions.checkNotNull(registry.getKey(entry), "%s has no registry key", entry);
	}

	/**
	 * @see #getKey(IForgeRegistry, Object)
	 */
	public static ResourceLocation getKey(final Block block) {
		return getKey(ForgeRegistries.BLOCKS, block);
	}

	/**
	 * @see #getKey(IForgeRegistry, Object)
	 */
	public static ResourceLocation getKey(final Item item) {
		return getKey(ForgeRegistries.ITEMS, item);
	}

	/**
	 * @see #getKey(IForgeRegistry, Object)
	 */
	public static ResourceLocation getKey(final EntityType<?> entityType) {
		return getKey(ForgeRegistries.ENTITY_TYPES, entityType);
	}

	/**
	 * @see #getKey(IForgeRegistry, Object)
	 */
	public static ResourceLocation getKey(final Fluid fluid) {
		return getKey(ForgeRegistries.FLUIDS, fluid);
	}

	/**
	 * @see #getKey(IForgeRegistry, Object)
	 */
	public static ResourceLocation getKey(final Potion potion) {
		return getKey(ForgeRegistries.POTIONS, potion);
	}
}
