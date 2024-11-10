package choonster.testmod3.world.item.component.fluidhandler;

import choonster.testmod3.fluid.ItemFluidTank;
import choonster.testmod3.fluid.UniversalBucketFluidHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

/**
 * @author Choonster
 */
public enum FluidHandlerType implements StringRepresentable {
	TANK(0, "tank", ItemFluidTank.CODEC, ItemFluidTank.STREAM_CODEC),
	BUCKET(1, "bucket", UniversalBucketFluidHandler.CODEC, UniversalBucketFluidHandler.STREAM_CODEC);

	private static final IntFunction<FluidHandlerType> BY_ID = ByIdMap.continuous(
			FluidHandlerType::getId,
			values(),
			ByIdMap.OutOfBoundsStrategy.ZERO
	);

	public static final Codec<FluidHandlerType> CODEC = StringRepresentable.fromEnum(FluidHandlerType::values);

	public static final StreamCodec<ByteBuf, FluidHandlerType> STREAM_CODEC = ByteBufCodecs.idMapper(
			BY_ID,
			FluidHandlerType::getId
	);

	private final int id;
	private final String name;
	private final MapCodec<? extends IFluidHandlerWithType> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, ? extends IFluidHandlerWithType> streamCodec;

	FluidHandlerType(
			final int id,
			final String name,
			final MapCodec<? extends IFluidHandlerWithType> codec,
			final StreamCodec<RegistryFriendlyByteBuf, ? extends IFluidHandlerWithType> streamCodec
	) {
		this.id = id;
		this.name = name;
		this.codec = codec;
		this.streamCodec = streamCodec;
	}

	public int getId() {
		return id;
	}

	@Override
	public String getSerializedName() {
		return name;
	}

	public MapCodec<? extends IFluidHandlerWithType> getCodec() {
		return codec;
	}

	public StreamCodec<RegistryFriendlyByteBuf, ? extends IFluidHandlerWithType> getStreamCodec() {
		return streamCodec;
	}
}
