package choonster.testmod3.remap;

import choonster.testmod3.Logger;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
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
public final class Remapper {
	private static final Marker MARKER = MarkerManager.getMarker("Remapper").addParents(Logger.MOD_MARKER);

	/**
	 * A list of remapping functions that return {@code true} if they took an action for the {@link MissingMapping}.
	 */
	private static final List<Predicate<MissingMapping>> remappingFunctions = ImmutableList.of(Remapper::remapCustomName);

	private Remapper() {
	}

	/**
	 * Remap this mod's missing mappings.
	 *
	 * @param missingMappings This mod's missing mappings
	 */
	public static void remap(final List<MissingMapping> missingMappings) {
		for (final MissingMapping missingMapping : missingMappings) { // For each missing mapping,
			Logger.info(MARKER, "Trying to remap %s", missingMapping.resourceLocation);

			for (final Predicate<MissingMapping> remappingFunction : remappingFunctions) { // For each remapping function
				if (remappingFunction.test(missingMapping)) { // If the function took an action,
					break; // Break from the inner loop
				}
			}

			if (missingMapping.getAction() == FMLMissingMappingsEvent.Action.DEFAULT) {
				Logger.info(MARKER, "Couldn't remap %s", missingMapping.resourceLocation);
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
	private static boolean tryRemap(final MissingMapping missingMapping, final ResourceLocation registryName) {
		switch (missingMapping.type) {
			case BLOCK:
				final IForgeRegistry<Block> blockRegistry = ForgeRegistries.BLOCKS;

				if (blockRegistry.containsKey(registryName)) {
					Logger.info(MARKER, "Remapped block %s to %s", missingMapping.resourceLocation, registryName);
					missingMapping.remap(blockRegistry.getValue(registryName));
					return true;
				}

				break;
			case ITEM:
				final IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;

				if (itemRegistry.containsKey(registryName)) {
					Logger.info(MARKER, "Remapped item %s to %s", missingMapping.resourceLocation, registryName);
					missingMapping.remap(itemRegistry.getValue(registryName));
					return true;
				}

				break;
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
			.build();

	/**
	 * Remap names to those specified in {@link #customNames}.
	 *
	 * @param missingMapping The missing mapping
	 * @return True if the missing mapping was remapped
	 */
	private static boolean remapCustomName(final MissingMapping missingMapping) {
		final String missingPath = missingMapping.resourceLocation.getResourcePath();

		if (!customNames.containsKey(missingPath)) return false;

		final String newPath = customNames.get(missingPath);
		final ResourceLocation newRegistryName = new ResourceLocation(missingMapping.resourceLocation.getResourceDomain(), newPath);

		return tryRemap(missingMapping, newRegistryName);
	}
}
