package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.*;
import choonster.testmod3.data.worldgen.ModBiomes;
import choonster.testmod3.data.worldgen.ModConfiguredFeatures;
import choonster.testmod3.data.worldgen.ModPlacedFeatures;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Registers this mod's {@link DataProvider}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDataProviders {
	private static final Field BUILDER = ObfuscationReflectionHelper.findField(VanillaRegistries.class, /* BUILDER */ "f_254635_");
	private static final Method WRAP_CONTEXT_LOOKUP = ObfuscationReflectionHelper.findMethod(RegistrySetBuilder.class, /* wrapContextLookup */ "m_254882_", HolderLookup.RegistryLookup.class);

	@SubscribeEvent
	public static void registerDataProviders(final GatherDataEvent event) {
		final var dataGenerator = event.getGenerator();
		final var output = dataGenerator.getPackOutput();
		final var existingFileHelper = event.getExistingFileHelper();
		final var lookupProvider = event.getLookupProvider().thenApply(ModDataProviders::createLookup);

		dataGenerator.addProvider(event.includeClient(), new TestMod3LanguageProvider(output));

		final var itemModelProvider = new TestMod3ItemModelProvider(output, existingFileHelper);
		dataGenerator.addProvider(event.includeClient(), itemModelProvider);

		// Let blockstate provider see generated item models by passing its existing file helper
		dataGenerator.addProvider(event.includeClient(), new TestMod3BlockStateProvider(output, itemModelProvider.existingFileHelper));

		dataGenerator.addProvider(event.includeServer(), new TestMod3RecipeProvider(output));
		dataGenerator.addProvider(event.includeServer(), TestMod3LootTableProvider.create(output));
		dataGenerator.addProvider(event.includeServer(), new TestMod3LootModifierProvider(output));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeModifierProvider(output, existingFileHelper, lookupProvider));

		final var blockTagsProvider = new TestMod3BlockTagsProvider(output, lookupProvider, existingFileHelper);
		dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
		dataGenerator.addProvider(event.includeServer(), new TestMod3ItemTagsProvider(output, lookupProvider, blockTagsProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeTagsProvider(output, lookupProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3FluidTagsProvider(output, lookupProvider, existingFileHelper));

		dataGenerator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider::join));
	}

	private static HolderLookup.Provider createLookup(final HolderLookup.Provider vanillaLookupProvider) {
		try {
			final var registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);

			final var builder = new RegistrySetBuilder()
					.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
					.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
					.add(Registries.BIOME, ModBiomes::bootstrap);

			final var vanillaKeys = Set.copyOf(((RegistrySetBuilder) BUILDER.get(null)).getEntryKeys());
			final var modKeys = Set.copyOf(builder.getEntryKeys());

			final var missingKeys = Sets.difference(vanillaKeys, modKeys);

			missingKeys.forEach(key -> builder.add(
					ResourceKey.create(ResourceKey.createRegistryKey(key.registry()), key.location()),
					context -> {
					}
			));

			return builder.buildPatch(registryAccess, vanillaLookupProvider);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to create holder lookup", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> HolderGetter<T> wrapContextLookup(final HolderLookup.RegistryLookup<T> lookup) {
		try {
			return (HolderGetter<T>) WRAP_CONTEXT_LOOKUP.invoke(null, lookup);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to wrap context lookup", e);
		}
	}
}
