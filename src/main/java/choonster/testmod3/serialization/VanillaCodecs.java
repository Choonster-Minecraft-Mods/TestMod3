package choonster.testmod3.serialization;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
	private static final Codec<Item> ITEM_NON_AIR_CODEC = ExtraCodecs.validate(
			ForgeRegistries.ITEMS.getCodec(),
			item -> item == Items.AIR ?
					DataResult.error(() -> "Item must not be minecraft:air") :
					DataResult.success(item)
	);

	/**
	 * A Codec for {@link  ItemStack} that uses lowercase field names, suitable for use in recipes.
	 */
	public static final Codec<ItemStack> RECIPE_RESULT = RecordCodecBuilder.create(instance -> instance.group(

			ITEM_NON_AIR_CODEC
					.fieldOf("item")
					.forGetter(ItemStack::getItem),

			ExtraCodecs.strictOptionalField(ExtraCodecs.POSITIVE_INT, "count", 1)
					.forGetter(ItemStack::getCount),

			CompoundTag.CODEC
					.optionalFieldOf("nbt")
					.forGetter(stack -> Optional.ofNullable(stack.getTag()))

	).apply(instance, (item, count, tag) -> new ItemStack(Holder.direct(item), count, tag)));

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

	public static final Codec<CommandBlockEntity.Mode> COMMAND_BLOCK_MODE = Util.make(() -> {
		final var values = CommandBlockEntity.Mode.values();

		return ExtraCodecs.idResolverCodec(
				CommandBlockEntity.Mode::ordinal,
				ordinal -> ordinal >= 0 && ordinal < values.length ? values[ordinal] : null,
				-1
		);
	});

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
