package choonster.testmod3.serialization;

import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.*;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifierManager;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link Codec} implementations for various Vanilla classes.
 *
 * @author Choonster
 */
public class VanillaCodecs {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final Codec<DyeColor> DYE_COLOR = StringRepresentable.fromEnum(DyeColor::values);

	public static final Codec<LootItemFunction[]> LOOT_FUNCTIONS_CODEC = Codec.PASSTHROUGH.flatXmap(
			d -> {
				try {
					var functions = LootModifierManager.GSON_INSTANCE.fromJson(
							IGlobalLootModifier.getJson(d),
							LootItemFunction[].class
					);

					return DataResult.success(functions);
				} catch (JsonSyntaxException e) {
					LOGGER.warn("Unable to decode loot functions", e);
					return DataResult.error(e::getMessage);
				}
			},
			functions -> {
				try {
					var element = LootModifierManager.GSON_INSTANCE.toJsonTree(functions);
					return DataResult.success(new Dynamic<>(JsonOps.INSTANCE, element));
				} catch (JsonSyntaxException e) {
					LOGGER.warn("Unable to encode loot functions", e);
					return DataResult.error(e::getMessage);
				}
			}
	);

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
