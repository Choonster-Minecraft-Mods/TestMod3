package choonster.testmod3.world.item.component.fluidhandler;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraftforge.fluids.capability.IFluidHandler;

/**
 * @author Choonster
 */
public interface IFluidHandlerWithType extends IFluidHandler {
	Codec<IFluidHandlerWithType> CODEC = FluidHandlerType.CODEC.dispatch(
			IFluidHandlerWithType::getType,
			FluidHandlerType::getCodec
	);

	StreamCodec<RegistryFriendlyByteBuf, IFluidHandlerWithType> STREAM_CODEC = FluidHandlerType.STREAM_CODEC
			.<RegistryFriendlyByteBuf>cast()
			.dispatch(
					IFluidHandlerWithType::getType,
					FluidHandlerType::getStreamCodec
			);

	FluidHandlerType getType();
}
