package choonster.testmod3.data;

import choonster.testmod3.util.RegistryUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.BiomeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Generates "report" JSON files for this mod's biomes.
 * <p>
 * Adapted from {@link BiomeProvider}.
 *
 * @author Choonster
 */
public class TestMod3BiomeProvider implements IDataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;

	public TestMod3BiomeProvider(final DataGenerator generator) {
		this.generator = generator;
	}

	/**
	 * Performs this provider's action.
	 */
	@Override
	public void act(final DirectoryCache cache) {
		final Path basePath = generator.getOutputFolder();

		for (final Biome biome : RegistryUtil.getModRegistryEntries(ForgeRegistries.BIOMES)) {
			final ResourceLocation registryName = Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome), "Biome registry name was null");
			final Path biomePath = getPath(basePath, registryName);

			final Function<Supplier<Biome>, DataResult<JsonElement>> function = JsonOps.INSTANCE.withEncoder(Biome.BIOME_CODEC);

			try {
				final Optional<JsonElement> optional = function.apply(() -> biome).result();
				if (optional.isPresent()) {
					IDataProvider.save(GSON, cache, optional.get(), biomePath);
				} else {
					LOGGER.error("Couldn't serialise biome {}", biomePath);
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
