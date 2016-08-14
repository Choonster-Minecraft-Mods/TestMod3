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
import java.util.regex.Pattern;

/**
 * Remaps this mod's {@link Block}s and {@link Item}s after registry names have been changed.
 *
 * @author Choonster
 */
public final class Remapper {
	private static final Marker MARKER = MarkerManager.getMarker("Remapper", Logger.MOD_MARKER);

	/**
	 * A list of remapping functions that return {@code true} if they took an action for the {@link MissingMapping}.
	 */
	private static List<Predicate<MissingMapping>> remappingFunctions = ImmutableList.of(Remapper::remapCustomName, Remapper::remapCamelCaseToUnderscores);

	private Remapper() {
	}

	/**
	 * Remap this mod's missing mappings.
	 *
	 * @param missingMappings This mod's missing mappings
	 */
	public static void remap(List<MissingMapping> missingMappings) {
		int i = 1;
		for (MissingMapping missingMapping : missingMappings) { // For each missing mapping,
			Logger.info(MARKER, "Trying to remap %s", missingMapping.resourceLocation);

			for (Predicate<MissingMapping> remappingFunction : remappingFunctions) { // For each remapping function
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
	private static boolean tryRemap(MissingMapping missingMapping, ResourceLocation registryName) {
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

	private static final Pattern UPPER_CASE_LETTER = Pattern.compile("\\p{Lu}");

	/**
	 * Remap {@code camelCase} names to {@code lowercase_underscores} names.
	 *
	 * @param missingMapping The missing mapping
	 * @return True if the missing mapping was remapped
	 */
	private static boolean remapCamelCaseToUnderscores(MissingMapping missingMapping) {
		final String missingPath = missingMapping.resourceLocation.getResourcePath();

		// If the name is already lowercase, ignore it
		if (missingPath.toLowerCase().equals(missingPath)) return false;

		// Put an underscore in front of every uppercase letter, then convert it to lowercase.
		final String newPath = UPPER_CASE_LETTER.matcher(missingPath).replaceAll("_$0").toLowerCase();
		final ResourceLocation newRegistryName = new ResourceLocation(missingMapping.resourceLocation.getResourceDomain(), newPath);

		// Try to remap to the new registry name
		return tryRemap(missingMapping, newRegistryName);
	}

	/**
	 * Custom names to remap. Keys are the old names, values are the new names.
	 */
	private static final Map<String, String> customNames = ImmutableMap.<String, String>builder()
			.put("harvestSwordWood", "wooden_harvest_sword")
			.put("harvestSwordDiamond", "diamond_harvest_sword")
			.put("slowSwordWood", "wooden_slow_sword")
			.put("slowSwordDiamond", "diamond_slow_sword")
			.put("fluid.staticgas", "fluid.static_gas")
			.put("fluid.normalgas", "fluid.normal_gas")
			.put("stainedClaySlabLowDouble", "double_stained_clay_slab_low")
			.put("stainedClaySlabHighDouble", "double_stained_clay_slab_high")
			.put("headReplacement", "replacement_helmet")
			.put("chestReplacement", "replacement_chestplate")
			.put("legsReplacement", "replacement_leggings")
			.put("feetReplacement", "replacement_boots")
			.build();

	/**
	 * Remap names to those specified in {@link #customNames}.
	 *
	 * @param missingMapping The missing mapping
	 * @return True if the missing mapping was remapped
	 */
	private static boolean remapCustomName(MissingMapping missingMapping) {
		final String missingPath = missingMapping.resourceLocation.getResourcePath();

		if (!customNames.containsKey(missingPath)) return false;

		final String newPath = customNames.get(missingPath);
		final ResourceLocation newRegistryName = new ResourceLocation(missingMapping.resourceLocation.getResourceDomain(), newPath);

		return tryRemap(missingMapping, newRegistryName);
	}
}
