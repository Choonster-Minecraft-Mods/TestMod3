package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.info.WorldgenRegistryDumpReport;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Copy of {@link WorldgenRegistryDumpReport} that only dumps this mod's registry entries.
 *
 * @author Choonster
 */
public class TestMod3WorldgenRegistryDumpReport implements DataProvider {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final DataGenerator generator;

	public TestMod3WorldgenRegistryDumpReport(final DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(final HashCache cache) {
		final var outputFolder = generator.getOutputFolder();
		final RegistryAccess registryAccess = RegistryAccess.BUILTIN.get();

		final var defaultDimensions = DimensionType.defaultDimensions(registryAccess, 0L, false);
		final ChunkGenerator chunkGenerator = WorldGenSettings.makeDefaultOverworld(registryAccess, 0L, false);

		final var registryWithOverworld = WorldGenSettings.withOverworld(
				registryAccess.ownedRegistryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
				defaultDimensions,
				chunkGenerator
		);

		final DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

		RegistryAccess.knownRegistries().forEach((registryData) -> {
			dumpRegistryCap(cache, outputFolder, registryAccess, ops, registryData);
		});

		dumpRegistry(outputFolder, cache, ops, Registry.LEVEL_STEM_REGISTRY, registryWithOverworld, LevelStem.CODEC);
	}

	private static <T> void dumpRegistryCap(final HashCache cache, final Path outputFolder, final RegistryAccess registryAccess, final DynamicOps<JsonElement> ops, final RegistryAccess.RegistryData<T> registryData) {
		dumpRegistry(outputFolder, cache, ops, registryData.key(), registryAccess.ownedRegistryOrThrow(registryData.key()), registryData.codec());
	}

	private static <E, T extends Registry<E>> void dumpRegistry(final Path outputFolder, final HashCache cache, final DynamicOps<JsonElement> ops, final ResourceKey<? extends T> registryKey, final T registry, final Encoder<E> encoder) {
		registry.entrySet()
				.stream()
				.filter(entry -> entry.getKey().location().getNamespace().equals(TestMod3.MODID))
				.forEach(entry -> {
					final var path = createPath(outputFolder, registryKey.location(), entry.getKey().location());
					dumpValue(path, cache, ops, encoder, entry.getValue());
				});
	}

	private static <E> void dumpValue(final Path outputFolder, final HashCache cache, final DynamicOps<JsonElement> ops, final Encoder<E> encoder, final E element) {
		try {
			final var result = encoder
					.encodeStart(ops, element)
					.resultOrPartial((p_206405_) -> LOGGER.error("Couldn't serialize element {}: {}", outputFolder, p_206405_));

			if (result.isPresent()) {
				DataProvider.save(GSON, cache, result.get(), outputFolder);
			}
		} catch (final IOException exception) {
			LOGGER.error("Couldn't save element {}", outputFolder, exception);
		}
	}

	private static Path createPath(final Path outputFolder, final ResourceLocation registryKey, final ResourceLocation elementName) {
		return resolveTopPath(outputFolder)
				.resolve(elementName.getNamespace())
				.resolve(registryKey.getPath())
				.resolve(elementName.getPath() + ".json");
	}

	private static Path resolveTopPath(final Path p_194690_) {
		return p_194690_.resolve("reports").resolve("worldgen");
	}

	@Override
	public String getName() {
		return "TestMod3Worldgen";
	}
}
