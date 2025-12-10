package choonster.testmod3.fluid;

import net.minecraft.resources.Identifier;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

/**
 * Basic implementation of {@link FluidType} that supports specifying still and flowing textures in the constructor.
 *
 * @author Choonster
 */
public class BasicFluidType extends FluidType {
	private final Identifier stillTexture;
	private final Identifier flowingTexture;

	public BasicFluidType(final Identifier stillTexture, final Identifier flowingTexture, final Properties properties) {
		super(properties);
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
	}

	public Identifier getStillTexture() {
		return stillTexture;
	}

	public Identifier getFlowingTexture() {
		return flowingTexture;
	}

	@Override
	public void initializeClient(final Consumer<IClientFluidTypeExtensions> consumer) {
		consumer.accept(new IClientFluidTypeExtensions() {
			@Override
			public Identifier getStillTexture() {
				return stillTexture;
			}

			@Override
			public Identifier getFlowingTexture() {
				return flowingTexture;
			}
		});
	}
}
