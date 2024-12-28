package choonster.testmod3.data.models.model;

import choonster.testmod3.util.RegistryUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * {@link ModelTemplate} implementation that uses {@link net.minecraftforge.client.model.DynamicFluidContainerModel}.
 *
 * @author Choonster
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DynamicFluidContainerModelTemplate extends ModelTemplate {
	private static final ResourceLocation LOADER = ResourceLocation.fromNamespaceAndPath(
			"forge",
			"fluid_container"
	);

	private final Optional<ResourceLocation> model;
	private final Set<TextureSlot> requiredSlots;
	private final Fluid fluid;
	private final boolean flipGas;

	public DynamicFluidContainerModelTemplate(
			final Optional<ResourceLocation> model,
			final Optional<String> suffix,
			final Fluid fluid,
			final boolean flipGas,
			final TextureSlot... requiredSlots
	) {
		super(model, suffix, requiredSlots);
		this.model = model;
		this.fluid = fluid;
		this.flipGas = flipGas;
		this.requiredSlots = ImmutableSet.copyOf(requiredSlots);
	}

	@Override
	public ResourceLocation create(
			final ResourceLocation modelLocation,
			final TextureMapping textureMapping,
			final BiConsumer<ResourceLocation, ModelInstance> modelOutput
	) {
		final var textureMap = createMap(textureMapping);

		modelOutput.accept(modelLocation, () -> {
			final var output = new JsonObject();

			output.addProperty("loader", LOADER.toString());
			output.addProperty("fluid", RegistryUtil.getKey(fluid).toString());
			output.addProperty("flip_gas", flipGas);

			model.ifPresent(model -> output.addProperty("parent", model.toString()));

			if (!textureMap.isEmpty()) {
				final var textures = new JsonObject();

				textureMap.forEach(
						(slot, texture) ->
								textures.addProperty(slot.getId(), texture.toString())
				);

				output.add("textures", textures);
			}

			return output;
		});

		return modelLocation;
	}

	private Map<TextureSlot, ResourceLocation> createMap(final TextureMapping p_378668_) {
		return Streams.concat(requiredSlots.stream(), p_378668_.getForced()).collect(ImmutableMap.toImmutableMap(Function.identity(), p_378668_::get));
	}
}
