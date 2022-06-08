package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.info.WorldgenRegistryDumpReport;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
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

	private final DataGenerator generator;

	public TestMod3WorldgenRegistryDumpReport(final DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(final CachedOutput cache) {
		final var outputFolder = generator.getOutputFolder();
		final RegistryAccess registryAccess = RegistryAccess.BUILTIN.get();
		final DynamicOps<JsonElement> ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

		RegistryAccess.knownRegistries().forEach((registryData) -> {
			dumpRegistryCap(cache, outputFolder, registryAccess, ops, registryData);
		});
	}

	private <T> void dumpRegistryCap(final CachedOutput cache, final Path outputFolder, final RegistryAccess registryAccess, final DynamicOps<JsonElement> ops, final RegistryAccess.RegistryData<T> registryData) {
		final ResourceKey<? extends Registry<T>> key = registryData.key();
		final DataGenerator.PathProvider pathProvider = generator.createPathProvider(DataGenerator.Target.DATA_PACK, key.location().getPath());

		registryAccess.ownedRegistryOrThrow(registryData.key())
				.entrySet()
				.stream()
				.filter(entry -> entry.getKey().location().getNamespace().equals(TestMod3.MODID))
				.forEach(entry -> dumpValue(pathProvider.json(entry.getKey().location()), cache, ops, registryData.codec(), entry.getValue()));
	}

	private static <E> void dumpValue(final Path outputFolder, final CachedOutput cache, final DynamicOps<JsonElement> ops, final Encoder<E> encoder, final E element) {
		try {
			final var result = encoder
					.encodeStart(ops, element)
					.resultOrPartial((p_206405_) -> LOGGER.error("Couldn't serialize element {}: {}", outputFolder, p_206405_));

			if (result.isPresent()) {
				DataProvider.saveStable(cache, result.get(), outputFolder);
			}
		} catch (final IOException exception) {
			LOGGER.error("Couldn't save element {}", outputFolder, exception);
		}
	}

	@Override
	public String getName() {
		return "TestMod3Worldgen";
	}
}
