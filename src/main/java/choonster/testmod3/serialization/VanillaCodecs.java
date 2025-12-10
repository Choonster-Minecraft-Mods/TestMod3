package choonster.testmod3.serialization;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.util.Function7;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link Codec} implementations for various Vanilla and Forge classes.
 *
 * @author Choonster
 */
public class VanillaCodecs {
	/**
	 * Replace with {@link ItemStack#STRICT_CODEC}
	 */
	@Deprecated(forRemoval = true)
	public static final Codec<ItemStack> RECIPE_RESULT = ItemStack.STRICT_CODEC;

	/**
	 * Prepares a Codec for {@link FluidStack} that uses lowercase field names, suitable for use in recipes/ingredients.
	 */
	public static Products.P3<
			RecordCodecBuilder.Mu<FluidStack>,
			Fluid,
			Integer,
			Optional<CompoundTag>
			> fluidStack(final RecordCodecBuilder.Instance<FluidStack> instance) {
		return instance.group(

				ForgeRegistries.FLUIDS.getCodec()
						.fieldOf("fluid")
						.forGetter(FluidStack::getFluid),

				Codec.INT
						.fieldOf("amount")
						.forGetter(FluidStack::getAmount),

				CompoundTag.CODEC
						.optionalFieldOf("nbt")
						.forGetter(stack -> Optional.ofNullable(stack.getTag()))

		);
	}

	public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> FLUID_STACK_OPTIONAL_STREAM_CODEC = new StreamCodec<>() {
		private static final StreamCodec<RegistryFriendlyByteBuf, Holder<Fluid>> FLUID_STREAM_CODEC = ByteBufCodecs.holderRegistry(Registries.FLUID);

		@Override
		public FluidStack decode(final RegistryFriendlyByteBuf buffer) {
			final var amount = buffer.readVarInt();
			if (amount <= 0) {
				return FluidStack.EMPTY;
			} else {
				final var fluidHolder = FLUID_STREAM_CODEC.decode(buffer);
				final var tag = buffer.readNbt();

				final var fluidStack = new FluidStack(fluidHolder.get(), amount);

				if (tag != null) {
					fluidStack.setTag(tag);
				}

				return fluidStack;
			}
		}

		@Override
		public void encode(final RegistryFriendlyByteBuf buffer, final FluidStack value) {
			if (value.isEmpty()) {
				buffer.writeVarInt(0);
			} else {
				buffer.writeVarInt(value.getAmount());
				FLUID_STREAM_CODEC.encode(buffer, Holder.direct(value.getFluid()));
				buffer.writeNbt(value.getTag());
			}
		}
	};

	public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> FLUID_STACK_STREAM_CODEC = new StreamCodec<>() {
		@Override
		public FluidStack decode(final RegistryFriendlyByteBuf buffer) {
			final var fluidStack = FLUID_STACK_OPTIONAL_STREAM_CODEC.decode(buffer);
			if (fluidStack.isEmpty()) {
				throw new DecoderException("Empty FluidStack not allowed");
			} else {
				return fluidStack;
			}
		}

		@Override
		public void encode(final RegistryFriendlyByteBuf buffer, final FluidStack value) {
			if (value.isEmpty()) {
				throw new EncoderException("Empty FluidStack not allowed");
			} else {
				FLUID_STACK_OPTIONAL_STREAM_CODEC.encode(buffer, value);
			}
		}
	};

	public static final Codec<CommandBlockEntity.Mode> COMMAND_BLOCK_MODE = Util.make(() -> {
		final var values = CommandBlockEntity.Mode.values();

		return ExtraCodecs.idResolverCodec(
				CommandBlockEntity.Mode::ordinal,
				ordinal -> ordinal >= 0 && ordinal < values.length ? values[ordinal] : null,
				-1
		);
	});

	public static final StreamCodec<ByteBuf, CommandBlockEntity.Mode> COMMAND_BLOCK_MODE_STREAM_CODEC = Util.make(() -> {
		final var idMapper = ByIdMap.continuous(CommandBlockEntity.Mode::ordinal, CommandBlockEntity.Mode.values(), ByIdMap.OutOfBoundsStrategy.ZERO);

		return ByteBufCodecs.idMapper(idMapper, CommandBlockEntity.Mode::ordinal);
	});

	public static final StreamCodec<FriendlyByteBuf, ChunkPos> CHUNK_POS_STREAM_CODEC = new StreamCodec<>() {
		@Override
		public ChunkPos decode(final FriendlyByteBuf buf) {
			return buf.readChunkPos();
		}

		@Override
		public void encode(final FriendlyByteBuf buf, final ChunkPos value) {
			buf.writeChunkPos(value);
		}
	};

	/**
	 * @see StreamCodec#composite
	 */
	public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> compositeStreamCodec(
			final StreamCodec<? super B, T1> codec1,
			final Function<C, T1> getter1,
			final StreamCodec<? super B, T2> codec2,
			final Function<C, T2> getter2,
			final StreamCodec<? super B, T3> codec3,
			final Function<C, T3> getter3,
			final StreamCodec<? super B, T4> codec4,
			final Function<C, T4> getter4,
			final StreamCodec<? super B, T5> codec5,
			final Function<C, T5> getter5,
			final StreamCodec<? super B, T6> codec6,
			final Function<C, T6> getter6,
			final StreamCodec<? super B, T7> codec7,
			final Function<C, T7> getter7,
			final Function7<T1, T2, T3, T4, T5, T6, T7, C> constructor
	) {
		return new StreamCodec<>() {
			@Override
			public C decode(final B buf) {
				final var t1 = codec1.decode(buf);
				final var t2 = codec2.decode(buf);
				final var t3 = codec3.decode(buf);
				final var t4 = codec4.decode(buf);
				final var t5 = codec5.decode(buf);
				final var t6 = codec6.decode(buf);
				final var t7 = codec7.decode(buf);
				return constructor.apply(t1, t2, t3, t4, t5, t6, t7);
			}

			@Override
			public void encode(final B buf, final C value) {
				codec1.encode(buf, getter1.apply(value));
				codec2.encode(buf, getter2.apply(value));
				codec3.encode(buf, getter3.apply(value));
				codec4.encode(buf, getter4.apply(value));
				codec5.encode(buf, getter5.apply(value));
				codec6.encode(buf, getter6.apply(value));
				codec7.encode(buf, getter7.apply(value));
			}
		};
	}

	public static Codec<NonNullList<ItemStack>> itemListCodec(final int expectedSize) {
		return new SparseNonNullListCodec<>(ItemStack.CODEC, expectedSize, ItemStack::isEmpty, ItemStack.EMPTY);
	}

	/**
	 * Creates a function that converts a name to its corresponding enum value by iterating through the array returned
	 * by {@code elementsSupplier} until {@code toNameFunction} returns a matching value.
	 *
	 * @param elementsSupplier A supplier that returns an array of enum values, usually a reference to the {@code values()} function
	 * @param toNameFunction   A function that converts an enum value to its name
	 * @param <E>              The enum type
	 * @return The function
	 */
	private static <E extends Enum<E>> Function<String, ? extends E> createFromNameFunction(
			final Supplier<E[]> elementsSupplier,
			final Function<E, String> toNameFunction
	) {
		return name -> {
			final var elements = elementsSupplier.get();

			for (final var element : elements) {
				if (toNameFunction.apply(element).equals(name)) {
					return element;
				}
			}

			return null;
		};
	}

	/**
	 * Creates a {@link Codec} for an {@link IExtensibleEnum} that serialises to/from a name.
	 * <p>
	 * Based on {@link StringRepresentable#fromEnum(Supplier)}, but never uses the ordinal value as that can change for modded
	 * enum values.
	 *
	 * @param elementsSupplier A supplier that returns an array of enum values, usually a reference to the {@code values()} function
	 * @param toNameFunction   A function that converts an enum value to its name
	 * @param <E>              The enum type
	 * @return The codec
	 */
	private static <E extends Enum<E> & IExtensibleEnum> Codec<E> createExtensibleEnumCodec(
			final Supplier<E[]> elementsSupplier,
			final Function<E, String> toNameFunction
	) {
		return createExtensibleEnumCodec(toNameFunction, createFromNameFunction(elementsSupplier, toNameFunction));
	}

	/**
	 * Creates a {@link Codec} for an {@link IExtensibleEnum} that serialises to/from a name.
	 * <p>
	 * Based on {@link StringRepresentable#fromEnum(Supplier)}, but never uses the ordinal value as that can change for modded
	 * enum values.
	 *
	 * @param toNameFunction   A function that converts an enum value to its name
	 * @param fromNameFunction A function that converts a name to the corresponding enum value
	 * @param <E>              The enum type
	 * @return The codec
	 */
	private static <E extends Enum<E> & IExtensibleEnum> Codec<E> createExtensibleEnumCodec(
			final Function<E, String> toNameFunction,
			final Function<String, ? extends E> fromNameFunction
	) {
		return new Codec<>() {
			@Override
			public <T> DataResult<T> encode(final E input, final DynamicOps<T> ops, final T prefix) {
				return ops.mergeToPrimitive(prefix, ops.createString(toNameFunction.apply(input)));
			}

			@Override
			public <T> DataResult<Pair<E, T>> decode(final DynamicOps<T> ops, final T input) {
				return ops.getStringValue(input)
						.flatMap(name ->
								Optional.ofNullable(fromNameFunction.apply(name))
										.map(DataResult::success)
										.orElseGet(() -> DataResult.error(() -> "Unknown element name: " + name))
						)
						.map(serializable -> Pair.of(serializable, ops.empty()));
			}

			@Override
			public String toString() {
				return "ExtensibleEnum[" + toNameFunction + "]";
			}
		};
	}

}
