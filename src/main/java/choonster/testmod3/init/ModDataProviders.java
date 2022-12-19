package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.*;
import choonster.testmod3.data.worldgen.ModBiomeModifiers;
import choonster.testmod3.data.worldgen.ModBiomes;
import choonster.testmod3.data.worldgen.ModConfiguredFeatures;
import choonster.testmod3.data.worldgen.ModPlacedFeatures;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DataPackRegistriesHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Registers this mod's {@link DataProvider}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDataProviders {
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

		final var blockTagsProvider = new TestMod3BlockTagsProvider(output, lookupProvider, existingFileHelper);
		dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
		dataGenerator.addProvider(event.includeServer(), new TestMod3ItemTagsProvider(output, lookupProvider, blockTagsProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeTagsProvider(output, lookupProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3FluidTagsProvider(output, lookupProvider, existingFileHelper));

		dataGenerator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider, Set.of(TestMod3.MODID)));
	}

	private static HolderLookup.Provider createLookup(final HolderLookup.Provider vanillaLookupProvider) {
		final var registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);

		final var builder = new RegistrySetBuilder()
				.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
				.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
				.add(Registries.BIOME, ModBiomes::bootstrap)
				.add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

		@SuppressWarnings("UnstableApiUsage")
		final var allKeys = DataPackRegistriesHooks.getDataPackRegistries()
				.stream()
				.map(RegistryDataLoader.RegistryData::key)
				.collect(Collectors.toSet());

		final var modKeys = Set.copyOf(builder.getEntryKeys());

		final var missingKeys = Sets.difference(allKeys, modKeys);

		missingKeys.forEach(key -> builder.add(
				ResourceKey.create(ResourceKey.createRegistryKey(key.registry()), key.location()),
				context -> {
				}
		));

		return builder.buildPatch(registryAccess, vanillaLookupProvider);
	}
}
