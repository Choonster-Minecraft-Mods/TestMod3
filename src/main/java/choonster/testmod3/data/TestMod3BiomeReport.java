package choonster.testmod3.data;

import choonster.testmod3.util.RegistryUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Generates "report" JSON files for this mod's biomes.
 *
 * @author Choonster
 */
// TODO: Remove in favour of WorldgenRegistryDumpReport?
public class TestMod3BiomeReport implements DataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;

	public TestMod3BiomeReport(final DataGenerator generator) {
		this.generator = generator;
	}

	/**
	 * Performs this provider's action.
	 */
	@Override
	public void run(final HashCache cache) {
		final Path basePath = generator.getOutputFolder();

		final RegistryAccess registryAccess = RegistryAccess.BUILTIN.get();
		final DynamicOps<JsonElement> dynamicOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

		for (final Biome biome : RegistryUtil.getModRegistryEntries(ForgeRegistries.BIOMES)) {
			final ResourceLocation registryName = Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome), "Biome registry name was null");
			final Path biomePath = getPath(basePath, registryName);

			final Function<Holder<Biome>, DataResult<JsonElement>> function = JsonOps.INSTANCE.withEncoder(Biome.CODEC);

			try {
				final Optional<JsonElement> optional = Biome.DIRECT_CODEC
						.encodeStart(dynamicOps, biome)
						.resultOrPartial((p_206405_) -> LOGGER.error("Couldn't serialize biome: {}", biomePath));

				if (optional.isPresent()) {
					DataProvider.save(GSON, cache, optional.get(), biomePath);
				}
			} catch (final IOException e) {
				LOGGER.error("Couldn't save biome {}", biomePath, e);
			}
		}
	}

	private static Path getPath(final Path path, final ResourceLocation biomeLocation) {
		return path.resolve("reports/biomes/" + biomeLocation.getNamespace() + "/" + biomeLocation.getPath() + ".json");
	}

	@Override
	public String getName() {
		return "TestMod3Biomes";
	}
}
