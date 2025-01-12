package choonster.testmod3.data.models.model;

import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

/**
 * {@link net.minecraft.client.data.models.model.ModelTemplate} implementation that sets the
 * {@code render_type} property.
 *
 * @author Choonster
 */
public class RenderTypeModelTemplate extends BaseModelTemplate {
	private final ResourceLocation renderTypeHint;

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public RenderTypeModelTemplate(
			final Optional<ResourceLocation> model,
			final Optional<String> suffix,
			final ResourceLocation renderTypeHint,
			final TextureSlot... requiredSlots
	) {
		super(model, suffix, requiredSlots);
		this.renderTypeHint = renderTypeHint;
	}

	@Override
	protected JsonObject createModel(final Map<TextureSlot, ResourceLocation> textureMap) {
		final var output = super.createModel(textureMap);

		output.addProperty("render_type", renderTypeHint.toString());

		return output;
	}
}
