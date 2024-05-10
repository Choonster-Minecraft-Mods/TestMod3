package choonster.testmod3.world.level.block.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BaseCommandBlock;

import java.util.function.IntFunction;

public abstract class SurvivalCommandBlock extends BaseCommandBlock {
	private final Type type;

	public SurvivalCommandBlock(final Type type) {
		this.type = type;
		setCustomName(Component.literal("Server"));
	}

	public Type getType() {
		return type;
	}

	public enum Type implements StringRepresentable {
		BLOCK(0, "block"),
		MINECART(1, "minecart");

		private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Type::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

		public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
		public static final StreamCodec<ByteBuf, Type> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Type::getId);

		private final int id;
		private final String name;

		Type(int id, final String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
