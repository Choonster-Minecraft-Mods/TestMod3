package choonster.testmod3.world.item.crafting.condition;

import com.mojang.serialization.*;
import net.minecraftforge.common.crafting.conditions.ConditionCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Copy of {@link ConditionCodec} that operates on {@link MapCodec} instead of {@link Codec}.
 *
 * @author Choonster
 */
public class ConditionMapCodec {
	public static <T> MapCodec<T> checkingDecode(final MapCodec<T> normal, final Supplier<T> _default) {
		return checkingDecode(normal, _default, ICondition.DEFAULT_FIELD);
	}

	public static <T> MapCodec<T> checkingDecode(final MapCodec<T> normal, final Supplier<T> _default, final String key) {
		return Codec.of(normal, new UnwrapMapDecoder<>(wrap(normal, key), _default));
	}

	public static <T> MapDecoder<Optional<T>> wrap(final MapDecoder<T> normal, final String key) {
		return new OptionalConditionalMapDecoder<>(normal, key);
	}

	public static <T> ICondition.IContext getContext(final DynamicOps<T> ops) {
		return ConditionCodec.getContext(ops);
	}

	private static final class UnwrapMapDecoder<A> extends MapDecoder.Implementation<A> {
		private final MapDecoder<Optional<A>> normal;
		private final Supplier<A> _default;

		private UnwrapMapDecoder(final MapDecoder<Optional<A>> normal, final Supplier<A> _default) {
			this.normal = normal;
			this._default = _default;
		}

		@Override
		public <T> Stream<T> keys(final DynamicOps<T> ops) {
			return normal.keys(ops);
		}

		@Override
		public <T> DataResult<A> decode(final DynamicOps<T> ops, final MapLike<T> input) {
			final var ret = normal.decode(ops, input);

			if (ret.result().isEmpty() || ret.result().get().isEmpty()) {
				return ret.map(v -> _default.get());
			}

			return ret.map(Optional::get);
		}

		@Override
		public String toString() {
			return "UnwrapMapDecoder[" +
					"normal=" + normal + ", " +
					"default=" + _default + ']';
		}
	}

	private static final class OptionalConditionalMapDecoder<A> extends MapDecoder.Implementation<Optional<A>> {
		private final MapDecoder<A> normal;
		private final String key;

		private OptionalConditionalMapDecoder(final MapDecoder<A> normal, final String key) {
			this.normal = normal;
			this.key = key;
		}

		@Override
		public <T> Stream<T> keys(final DynamicOps<T> ops) {
			return Stream.concat(Stream.of(ops.createString(key)), normal.keys(ops));
		}

		@Override
		public <T> DataResult<Optional<A>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
			final T conditionRaw = input.get(key);
			if (conditionRaw == null) {
				return normal.decode(ops, input).map(Optional::of);
			}

			final var conditionDecoded = ICondition.CODEC.parse(ops, conditionRaw);
			final var error = conditionDecoded.error();
			if (error.isPresent()) {
				return DataResult.error(() -> error.get().message());
			}

			final var condition = conditionDecoded.result().orElseThrow();
			if (!condition.test(getContext(ops), ops)) {
				return DataResult.success(Optional.empty());
			}

			return normal.decode(ops, input).map(Optional::of);
		}

		@Override
		public String toString() {
			return "OptionalConditionalMapDecoder[" +
					"normal=" + normal + ", " +
					"key=" + key + ']';
		}
	}
}
