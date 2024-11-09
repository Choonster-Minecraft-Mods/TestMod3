package choonster.testmod3.world.item.component.pigspawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

/**
 * The type of {@link IPigSpawner}.
 *
 * @author Choonster
 */
public enum PigSpawnerType implements StringRepresentable {
	INFINITE(0, "infinite", InfinitePigSpawner.CODEC, InfinitePigSpawner.STREAM_CODEC),
	FINITE(1, "finite", FinitePigSpawner.CODEC, FinitePigSpawner.STREAM_CODEC);

	private static final IntFunction<PigSpawnerType> BY_ID = ByIdMap.continuous(
			PigSpawnerType::getId,
			values(),
			ByIdMap.OutOfBoundsStrategy.ZERO
	);

	public static final Codec<PigSpawnerType> CODEC = StringRepresentable.fromEnum(PigSpawnerType::values);
	
	public static final StreamCodec<ByteBuf, PigSpawnerType> STREAM_CODEC = ByteBufCodecs.idMapper(
			BY_ID,
			PigSpawnerType::getId
	);

	private final int id;
	private final String name;
	private final MapCodec<? extends IPigSpawner> codec;
	private final StreamCodec<ByteBuf, ? extends IPigSpawner> streamCodec;

	PigSpawnerType(
			final int id,
			final String name,
			final MapCodec<? extends IPigSpawner> codec,
			final StreamCodec<ByteBuf, ? extends IPigSpawner> streamCodec
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

	public MapCodec<? extends IPigSpawner> getCodec() {
		return codec;
	}

	public StreamCodec<ByteBuf, ? extends IPigSpawner> getStreamCodec() {
		return streamCodec;
	}
}
