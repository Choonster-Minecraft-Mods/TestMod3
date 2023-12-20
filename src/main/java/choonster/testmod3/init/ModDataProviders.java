package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.*;
import choonster.testmod3.data.worldgen.ModBiomeModifiers;
import choonster.testmod3.data.worldgen.ModBiomes;
import choonster.testmod3.data.worldgen.ModConfiguredFeatures;
import choonster.testmod3.data.worldgen.ModPlacedFeatures;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DataPackRegistriesHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

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
		final var patchedRegistries = createLookup(event.getLookupProvider());
		final var lookupProvider = patchedRegistries.thenApply(RegistrySetBuilder.PatchedRegistries::full);

		dataGenerator.addProvider(true, TestMod3PackMetadataGenerator.create(output));

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
		dataGenerator.addProvider(event.includeServer(), new TestMod3ItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeTagsProvider(output, lookupProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3FluidTagsProvider(output, lookupProvider, existingFileHelper));

		dataGenerator.addProvider(event.includeServer(), new RegistriesDatapackGenerator(output, lookupProvider, Set.of(TestMod3.MODID)));
	}

	@SuppressWarnings("UnstableApiUsage")
	private static CompletableFuture<RegistrySetBuilder.PatchedRegistries> createLookup(final CompletableFuture<HolderLookup.Provider> future) {
		return future.thenApply(vanillaLookupProvider -> {
			final var builder = new RegistrySetBuilder()
					.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
					.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
					.add(Registries.BIOME, ModBiomes::bootstrap)
					.add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

			final var registryAccess = RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
			final var clonerFactory = new LenientTagsCloner.Factory();

			DataPackRegistriesHooks.getDataPackRegistries().forEach(registryData ->
					registryData.runWithArguments(clonerFactory::addCodec)
			);

			final var dataPackRegistryKeys = DataPackRegistriesHooks.getDataPackRegistries()
					.stream()
					.map(RegistryDataLoader.RegistryData::key);

			final var fullLookupProvider = HolderLookup.Provider.create(
					Stream.concat(vanillaLookupProvider.listRegistries(), dataPackRegistryKeys)
							.distinct()
							.map(key -> Pair.of(key, vanillaLookupProvider.lookup(key)))
							.map(pair -> pair.getSecond().orElseGet(() -> new EmptyRegistryLookup<>(pair.getFirst())))
			);

			final var patchedRegistries = builder.buildPatch(registryAccess, fullLookupProvider, clonerFactory);
			final var patchedLookupProvider = patchedRegistries.full();

			final var biomeLookup = patchedLookupProvider.lookup(Registries.BIOME);
			final var placedFeatureLookup = patchedLookupProvider.lookup(Registries.PLACED_FEATURE);

			if (biomeLookup.isPresent() || placedFeatureLookup.isPresent()) {
				VanillaRegistries.validateThatAllBiomeFeaturesHaveBiomeFilter(
						placedFeatureLookup.orElseGet(() -> vanillaLookupProvider.lookupOrThrow(Registries.PLACED_FEATURE)),
						biomeLookup.orElseGet(() -> vanillaLookupProvider.lookupOrThrow(Registries.BIOME))
				);
			}

			return patchedRegistries;
		});
	}

	private record EmptyRegistryLookup<T>(
			ResourceKey<? extends Registry<? extends T>> key
	) implements HolderLookup.RegistryLookup<T> {
		@Override
		public Optional<Holder.Reference<T>> get(final ResourceKey<T> resourceKey) {
			return Optional.empty();
		}

		@Override
		public Optional<HolderSet.Named<T>> get(final TagKey<T> tagKey) {
			return Optional.empty();
		}

		@Override
		public Stream<Holder.Reference<T>> listElements() {
			return Stream.empty();
		}

		@Override
		public Stream<HolderSet.Named<T>> listTags() {
			return Stream.empty();
		}

		@Override
		public Lifecycle registryLifecycle() {
			return Lifecycle.experimental();
		}
	}

	/**
	 * An extension of {@link Cloner} that allows all Tags ({@link HolderSet.Named}) to be created when
	 * decoding the cloned value.
	 *
	 * @param <T> The registry element type
	 */
	private static class LenientTagsCloner<T> extends Cloner<T> {
		protected LenientTagsCloner(final Codec<T> directCodec) {
			super(directCodec);
		}

		@Override
		public T clone(final T object, final HolderLookup.Provider source, final HolderLookup.Provider destination) {
			final var destinationWrapper = HolderLookup.Provider.create(
					destination.listRegistries()
							.map(destination::lookupOrThrow)
							.map(Lookup::new)
			);

			return super.clone(object, source, destinationWrapper);
		}

		private static class Lookup<T> extends HolderLookup.RegistryLookup.Delegate<T> {
			private final RegistryLookup<T> lookup;

			private Lookup(final RegistryLookup<T> lookup) {
				this.lookup = lookup;
			}

			@Override
			protected RegistryLookup<T> parent() {
				return lookup;
			}

			@SuppressWarnings("deprecation")
			@Override
			public Optional<HolderSet.Named<T>> get(final TagKey<T> p_256245_) {
				return super.get(p_256245_).or(() -> Optional.of(HolderSet.emptyNamed(lookup, p_256245_)));
			}
		}

		public static class Factory extends Cloner.Factory {
			private final Map<ResourceKey<? extends Registry<?>>, Cloner<?>> codecs = new HashMap<>();

			@Override
			public <T> Cloner.Factory addCodec(final ResourceKey<? extends Registry<? extends T>> p_310427_, final Codec<T> p_312943_) {
				codecs.put(p_310427_, new LenientTagsCloner<>(p_312943_));
				return this;
			}

			@SuppressWarnings("unchecked")
			@Nullable
			@Override
			public <T> Cloner<T> cloner(final ResourceKey<? extends Registry<? extends T>> p_311946_) {
				return (Cloner<T>) codecs.get(p_311946_);
			}
		}
	}
}
