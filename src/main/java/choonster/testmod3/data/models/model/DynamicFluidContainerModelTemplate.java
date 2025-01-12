package choonster.testmod3.data.models.model;

import choonster.testmod3.util.RegistryUtil;
import com.google.gson.JsonObject;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;
import java.util.Optional;

/**
 * {@link net.minecraft.client.data.models.model.ModelTemplate} implementation that uses {@link net.minecraftforge.client.model.DynamicFluidContainerModel}.
 *
 * @author Choonster
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DynamicFluidContainerModelTemplate extends BaseModelTemplate {
	private static final ResourceLocation LOADER = ResourceLocation.fromNamespaceAndPath(
			"forge",
			"fluid_container"
	);

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
		this.fluid = fluid;
		this.flipGas = flipGas;
	}

	@Override
	protected JsonObject createModel(final Map<TextureSlot, ResourceLocation> textureMap) {
		final JsonObject output = super.createModel(textureMap);

		output.addProperty("loader", LOADER.toString());
		output.addProperty("fluid", RegistryUtil.getKey(fluid).toString());
		output.addProperty("flip_gas", flipGas);

		return output;
	}
}
