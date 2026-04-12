package choonster.testmod3.data.models.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
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
			final TextureMapping textures,
			final BiConsumer<Identifier, ModelInstance> modelOutput
	) {
		final var slots = createMap(textures);
		modelOutput.accept(modelLocation, () -> createModel(slots));
		return modelLocation;
	}

	protected JsonObject createModel(final Map<TextureSlot, Material> slots) {
		final var object = new JsonObject();
		 
		model.ifPresent(m -> object.addProperty("parent", m.toString()));

		if (!slots.isEmpty()) {
			final var textureObj = new JsonObject();

			slots.forEach((slot, value) -> {
				final var valueJson = Material.CODEC.encodeStart(JsonOps.INSTANCE, value).getOrThrow();
				textureObj.add(slot.getId(), valueJson);
			});

			object.add("textures", textureObj);
		}

		return object;
	}

	private Map<TextureSlot, Material> createMap(final TextureMapping textureMapping) {
		return Streams.concat(
				requiredSlots.stream(), textureMapping.getForced()
		).collect(ImmutableMap.toImmutableMap(Function.identity(), textureMapping::get));
	}
}
