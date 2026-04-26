package choonster.testmod3.data;

import choonster.testmod3.fluid.BasicFluidType;
import choonster.testmod3.init.ModFluids;
import net.minecraft.client.data.AtlasProvider;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Generates this mod's atlas definitions.
 *
 * @author Choonster
 */
public class TestMod3AtlasProvider extends AtlasProvider {
	public TestMod3AtlasProvider(final PackOutput output) {
		super(output);
	}

	@Override
	public CompletableFuture<?> run(final CachedOutput cache) {
		final var fluidSpriteSources = ModFluids.orderedFluidType()
				.stream()
				.filter(fluidType -> fluidType.get() instanceof BasicFluidType)
				.map(fluidType -> (BasicFluidType) fluidType.get())
				.map(basicFluidType -> fromBlock(basicFluidType.getStillTexture()))
				.collect(Collectors.toList());

		return CompletableFuture.allOf(
				storeAtlas(cache, AtlasIds.ITEMS, fluidSpriteSources)
		);
	}

	private static SpriteSource fromBlock(final Identifier resource) {
		var id = resource;
		if (resource.getPath().startsWith("block/")) {
			id = resource.withPath(path -> "item/" + path.substring(6));
		}
		return new SingleFile(resource, Optional.of(id));
	}
}
