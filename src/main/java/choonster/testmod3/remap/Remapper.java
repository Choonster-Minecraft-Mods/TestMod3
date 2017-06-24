package choonster.testmod3.remap;

import choonster.testmod3.Logger;
import choonster.testmod3.TestMod3;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.RegistryEvent.MissingMappings.Mapping;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Remaps this mod's {@link Block}s and {@link Item}s after registry names have been changed.
 *
 * @author Choonster
 */
final class Remapper<T extends IForgeRegistryEntry<T>> {
	private static final Marker MARKER = MarkerManager.getMarker("Remapper").addParents(Logger.MOD_MARKER);

	/**
	 * A list of remapping functions that return {@code true} if they took an action for the {@link Mapping<T>}.
	 */
	private final List<Predicate<Mapping<T>>> remappingFunctions = ImmutableList.of(this::remapCustomName);

	private Remapper() {
	}

	/**
	 * Remap this mod's missing mappings.
	 *
	 * @param missingMappings This mod's missing mappings
	 */
	private void remapAll(final List<Mapping<T>> missingMappings) {
		for (final Mapping<T> missingMapping : missingMappings) { // For each missing mapping,
			Logger.info(MARKER, "Trying to remap %s", missingMapping.key);

			// Try to apply all remapping functions until one performs an action.
			final boolean remapped = remappingFunctions.stream().anyMatch(mappingPredicate -> mappingPredicate.test(missingMapping));

			if (!remapped) {
				Logger.info(MARKER, "Couldn't remap %s", missingMapping.key);
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
		final IForgeRegistry<T> registry = missingMapping.registry;
		final T value = registry.getValue(registryName);
		if (registry.containsKey(registryName) && value != null) {
			Logger.info(MARKER, "Remapped %s %s to %s", registry.getRegistrySuperType().getSimpleName(), missingMapping.key, registryName);
			missingMapping.remap(value);
			return true;
		}

		return false;
	}

	/**
	 * Custom names to remap. Keys are the old names, values are the new names.
	 */
	private static final Map<String, String> customNames = ImmutableMap.<String, String>builder()
			.put("harvestswordwood", "wooden_harvest_sword")
			.put("harvestsworddiamond", "diamond_harvest_sword")
			.put("slowswordwood", "wooden_slow_sword")
			.put("slowsworddiamond", "diamond_slow_sword")
			.put("fluid.staticgas", "fluid.static_gas")
			.put("fluid.normalgas", "fluid.normal_gas")
			.put("stainedclayslablowdouble", "double_stained_clay_slab_low")
			.put("stainedclayslabhighdouble", "double_stained_clay_slab_high")
			.put("headreplacement", "replacement_helmet")
			.put("chestreplacement", "replacement_chestplate")
			.put("legsreplacement", "replacement_leggings")
			.put("feetreplacement", "replacement_boots")
			.put("modeltest", "model_test")
			.put("pig_spawner.finite", "pig_spawner_finite")
			.put("pig_spawner.infinite", "pig_spawner_infinite")
			.put("watergrass", "water_grass")
			.build();

	/**
	 * Remap names to those specified in {@link #customNames}.
	 *
	 * @param missingMapping The missing mapping
	 * @return True if the missing mapping was remapped
	 */
	private boolean remapCustomName(final Mapping<T> missingMapping) {
		final String missingPath = missingMapping.key.getResourcePath();

		if (!customNames.containsKey(missingPath)) return false;

		final String newPath = customNames.get(missingPath);
		final ResourceLocation newRegistryName = new ResourceLocation(missingMapping.key.getResourceDomain(), newPath);

		return tryRemap(missingMapping, newRegistryName);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	@SuppressWarnings("unused")
	private static class EventHandler {
		private static final Remapper<Block> blockRemapper = new Remapper<>();
		private static final Remapper<Item> itemRemapper = new Remapper<>();

		@SubscribeEvent
		public static void missingBlockMappings(final RegistryEvent.MissingMappings<Block> event) {
			blockRemapper.remapAll(event.getMappings());
		}

		@SubscribeEvent
		public static void missingItemMappings(final RegistryEvent.MissingMappings<Item> event) {
			itemRemapper.remapAll(event.getMappings());
		}
	}
}
