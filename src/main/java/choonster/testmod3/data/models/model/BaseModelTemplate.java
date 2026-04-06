package choonster.testmod3.data.models.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Extensible base class for {@link ModelTemplate} implementations.
 *
 * @author Choonster
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class BaseModelTemplate extends ModelTemplate {
	protected final Optional<Identifier> model;
	protected final Optional<String> suffix;
	protected final Set<TextureSlot> requiredSlots;

	public BaseModelTemplate(final Optional<Identifier> model, final Optional<String> suffix, final TextureSlot... requiredSlots) {
		super(model, suffix, requiredSlots);
		this.model = model;
		this.suffix = suffix;
		this.requiredSlots = ImmutableSet.copyOf(requiredSlots);
	}

	@Override
	public Identifier create(
			final Identifier modelLocation,
			final TextureMapping textureMapping,
			final BiConsumer<Identifier, ModelInstance> modelOutput
	) {
		final var map = createMap(textureMapping);
		modelOutput.accept(modelLocation, () -> createModel(map));
		return modelLocation;
	}

	protected JsonObject createModel(final Map<TextureSlot, Material> textureMap) {
		final var output = new JsonObject();

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
	}

	private Map<TextureSlot, Material> createMap(final TextureMapping textureMapping) {
		return Streams.concat(
				requiredSlots.stream(), textureMapping.getForced()
		).collect(ImmutableMap.toImmutableMap(Function.identity(), textureMapping::get));
	}
}
