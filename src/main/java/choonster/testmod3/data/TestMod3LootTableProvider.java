package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.loot.TestMod3BlockLootTables;
import choonster.testmod3.data.loot.TestMod3EntityLootTables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Generates this mod's loot tables.
 *
 * @author Choonster
 */
public class TestMod3LootTableProvider implements IDataProvider {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();

	private final DataGenerator dataGenerator;

	private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> lootTableGenerators = ImmutableList.of(
			Pair.of(TestMod3BlockLootTables::new, LootParameterSets.BLOCK),
			Pair.of(TestMod3EntityLootTables::new, LootParameterSets.ENTITY)
	);

	public TestMod3LootTableProvider(final DataGenerator dataGenerator) {
		this.dataGenerator = dataGenerator;
	}

	/**
	 * Performs this provider's action.
	 */
	@Override
	public void act(final DirectoryCache cache) {
		final Path outputFolder = dataGenerator.getOutputFolder();

		final Map<ResourceLocation, LootTable> lootTables = Maps.newHashMap();
		lootTableGenerators.forEach(generatorPair -> {
			final Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> generator = generatorPair.getFirst().get();
			final LootParameterSet lootParameterSet = generatorPair.getSecond();

			generator.accept((lootTableID, lootTableBuilder) -> {
						if (lootTables.put(lootTableID, lootTableBuilder.setParameterSet(lootParameterSet).build()) != null) {
							throw new IllegalStateException("Duplicate loot table " + lootTableID);
						}
					}
			);
		});

		final ValidationResults validationresults = new ValidationResults();

		final Set<ResourceLocation> modLootTableIDs = LootTables.func_215796_a()
				.stream()
				.filter(lootTableID -> lootTableID.getNamespace().equals(TestMod3.MODID))
				.collect(Collectors.toSet());

		for (final ResourceLocation resourcelocation : Sets.difference(modLootTableIDs, lootTables.keySet())) {
			validationresults.addProblem("Missing built-in table: " + resourcelocation);
		}

		lootTables.forEach((lootTableID, lootTable) -> LootTableManager.func_215302_a(validationresults, lootTableID, lootTable, lootTables::get));

		final Multimap<String, String> problems = validationresults.getProblems();
		if (!problems.isEmpty()) {
			problems.forEach((path, message) -> {
				LOGGER.warn("Found validation problem in " + path + ": " + message);
			});
			throw new IllegalStateException("Failed to validate loot tables, see logs");
		} else {
			lootTables.forEach((lootTableID, lootTable) -> {
				final Path path = getPath(outputFolder, lootTableID);

				try {
					IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
				} catch (final IOException ioexception) {
					LOGGER.error("Couldn't save loot table {}", path, ioexception);
				}
			});
		}
	}

	private static Path getPath(final Path path, final ResourceLocation id) {
		return path.resolve("data/" + id.getNamespace() + "/loot_tables/" + id.getPath() + ".json");
	}

	/**
	 * Gets a name for this provider, to use in logging.
	 */
	@Override
	public String getName() {
		return "TestMod3LootTables";
	}
}
