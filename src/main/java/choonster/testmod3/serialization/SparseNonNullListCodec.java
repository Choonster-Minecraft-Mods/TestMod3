package choonster.testmod3.serialization;

import com.mojang.datafixers.util.Function3;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A codec for {@link NonNullList} that only serialises non-empty elements.
 * <p>
 * Adapted from {@link ContainerHelper#saveAllItems(CompoundTag, NonNullList, HolderLookup.Provider)},
 * {@link ContainerHelper#loadAllItems(CompoundTag, NonNullList, HolderLookup.Provider)} and
 * {@link com.mojang.serialization.codecs.ListCodec}.
 *
 * @author Choonster
 */
public record SparseNonNullListCodec<E>(
		Codec<E> elementCodec,
		int expectedSize,
		Predicate<E> isEmpty,
		E defaultValue
) implements Codec<NonNullList<E>> {
	private <R> DataResult<R> createTooLongError(final int size) {
		return DataResult.error(() -> "List is too long: " + size + ", expected size " + expectedSize);
	}

	@Override
	public <T> DataResult<T> encode(final NonNullList<E> input, final DynamicOps<T> ops, final T prefix) {
		if (input.size() > expectedSize) {
			return createTooLongError(input.size());
		}

		final var listBuilder = ops.listBuilder();

		for (var i = 0; i < input.size(); i++) {
			final var element = input.get(i);
			if (!isEmpty.test(element)) {
				final var mapResult = ops.mapBuilder()
						.add("slot", ops.createByte((byte) i))
						.build(ops.empty());

				final var stackResult = mapResult.flatMap(map -> elementCodec.encode(element, ops, map));
				listBuilder.add(stackResult);
			}
		}

		return listBuilder.build(prefix);
	}

	@Override
	public <T> DataResult<Pair<NonNullList<E>, T>> decode(final DynamicOps<T> ops, final T input) {
		return ops.getList(input).setLifecycle(Lifecycle.stable()).flatMap(stream -> {
			final var decoder = new DecoderState<>(ops);
			stream.accept(decoder::accept);
			return decoder.build();
		});
	}

	private class DecoderState<T> {
		private static final DataResult<Unit> INITIAL_RESULT = DataResult.success(Unit.INSTANCE, Lifecycle.stable());

		private final DynamicOps<T> ops;
		private final NonNullList<E> elements = NonNullList.withSize(expectedSize, defaultValue);
		private final Stream.Builder<T> failed = Stream.builder();
		private DataResult<Unit> result = INITIAL_RESULT;
		private int totalCount;

		private DecoderState(final DynamicOps<T> ops) {
			this.ops = ops;
		}

		public void accept(final T value) {
			totalCount++;

			if (elements.size() >= expectedSize) {
				failed.add(value);
				return;
			}

			final var slotResult = ops.get(value, "slot")
					.flatMap(ops::getNumberValue)
					.map(Number::byteValue);

			final var elementResult = elementCodec.parse(ops, value);

			slotResult.apply2(elements::set, elementResult)
					.error()
					.ifPresent(error -> failed.add(value));

			result = apply3stable((result, slot, element) -> result, result, slotResult, elementResult);
		}

		public DataResult<Pair<NonNullList<E>, T>> build() {
			final var errors = ops.createList(failed.build());
			final var pair = Pair.of(elements, errors);

			if (totalCount > expectedSize) {
				result = createTooLongError(totalCount);
			}

			return result.map(ignored -> pair).setPartial(pair);
		}

		/**
		 * @see DataResult#apply2stable(BiFunction, DataResult)
		 */
		private static <R1, R2, R3, S> DataResult<S> apply3stable(
				final Function3<R1, R2, R3, S> function,
				final DataResult<R1> first,
				final DataResult<R2> second,
				final DataResult<R3> third
		) {
			final var instance = DataResult.instance();
			final var f = DataResult.unbox(instance.point(function)).setLifecycle(Lifecycle.stable());
			return DataResult.unbox(instance.ap3(f, first, second, third));
		}
	}
}
