package choonster.testmod3.remap;

import choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.registries.MissingMappingsEvent.Mapping;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Remaps this mod's {@link Block}s and {@link Item}s after registry names have been changed.
 *
 * @author Choonster
 */
final class Remapper<T> {
	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * A list of remapping functions that return {@code true} if they took an action for the {@link Mapping <T>}.
	 */
	private final List<Predicate<Mapping<T>>> remappingFunctions = ImmutableList.of(this::remapCustomName, this::ignoreName);

	private Remapper() {
	}

	/**
	 * Remap this mod's missing mappings.
	 *
	 * @param missingMappings This mod's missing mappings
	 */
	private void remapAll(final List<Mapping<T>> missingMappings) {
		for (final Mapping<T> missingMapping : missingMappings) { // For each missing mapping,
			LOGGER.info("Trying to remap {}", missingMapping.getKey());

			// Try to apply all remapping functions until one performs an action.
			final boolean remapped = remappingFunctions.stream().anyMatch(mappingPredicate -> mappingPredicate.test(missingMapping));

			if (!remapped) {
				LOGGER.info("Couldn't remap {}", missingMapping.getKey());
			}
		}
	}

	/**
	 * Try to remap {@code missingMapping} to the value of {@code registryName}.
	 *
	 * @param missingMapping The missing mapping
	 * @param registryName   The registry name to remap to
	 * @return True if the remapping was successful
	 */
	private boolean tryRemap(final Mapping<T> missingMapping, final ResourceLocation registryName) {
		final IForgeRegistry<T> registry = missingMapping.getRegistry();
		final T value = registry.getValue(registryName);
		if (registry.containsKey(registryName) && value != null) {
			LOGGER.info("Remapped {} to {}", ResourceKey.create(registry.getRegistryKey(), missingMapping.getKey()), registryName);
			missingMapping.remap(value);
			return true;
		}

		return false;
	}

	/**
	 * Custom names to remap. Keys are the old names, values are the new names.
	 */
	private static final Map<String, String> customNames = ImmutableMap.<String, String>builder()
			.put("watergrass", "water_grass")
			.build();

	/**
	 * Remap names to those specified in {@link #customNames}.
	 *
	 * @param missingMapping The missing mapping
	 * @return True if the missing mapping was remapped
	 */
	private boolean remapCustomName(final Mapping<T> missingMapping) {
		final String missingPath = missingMapping.getKey().getPath();

		if (!customNames.containsKey(missingPath)) {
			return false;
		}

		final String newPath = customNames.get(missingPath);
		final ResourceLocation newRegistryName = new ResourceLocation(missingMapping.getKey().getNamespace(), newPath);

		return tryRemap(missingMapping, newRegistryName);
	}

	/**
	 * Names to ignore and leave in the save.
	 */
	private static final List<String> namesToIgnore = ImmutableList.<String>builder()
			.build();

	private boolean ignoreName(final Mapping<T> missingMapping) {
		final String missingPath = missingMapping.getKey().getPath();

		if (!namesToIgnore.contains(missingPath)) {
			return false;
		}

		missingMapping.ignore();

		LOGGER.info("Ignored missing entry {}", ResourceKey.create(missingMapping.getRegistry().getRegistryKey(), missingMapping.getKey()));

		return true;
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	@SuppressWarnings("unused")
	private static class EventHandler {
		private static final Remapper<Block> blockRemapper = new Remapper<>();
		private static final Remapper<Item> itemRemapper = new Remapper<>();

		@SubscribeEvent
		public static void missingMappings(final MissingMappingsEvent event) {
			blockRemapper.remapAll(event.getMappings(ForgeRegistries.Keys.BLOCKS, TestMod3.MODID));
			itemRemapper.remapAll(event.getMappings(ForgeRegistries.Keys.ITEMS, TestMod3.MODID));
		}
	}
}
