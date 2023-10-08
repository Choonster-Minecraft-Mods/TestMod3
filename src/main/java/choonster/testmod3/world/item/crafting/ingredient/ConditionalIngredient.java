package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ConditionCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

/**
 * An {@link Ingredient} that produces another {@link Ingredient} type, but only if the
 * specified conditions are met. If they aren't, it produces {@link IngredientNever#INSTANCE} instead.
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
public class ConditionalIngredient extends AbstractDelegatingIngredient {
	public static final Codec<ConditionalIngredient> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(

					ICondition.CODEC
							.fieldOf(ICondition.DEFAULT_FIELD)
							.forGetter(ingredient -> ingredient.condition),

					Ingredient.CODEC_NONEMPTY
							.fieldOf("ingredient")
							.forGetter(ingredient -> ingredient.ingredient)

			).apply(instance, ConditionalIngredient::new)
	);

	public static final Codec<Ingredient> CODEC = ConditionCodec.checkingDecode(
			DATA_CODEC.flatComapMap(
					conditionalIngredient -> conditionalIngredient.ingredient,
					ingredient -> ingredient instanceof ConditionalIngredient conditionalIngredient ?
							DataResult.success(conditionalIngredient) :
							DataResult.error(() -> "Can't convert Ingredient to ConditionalIngredient")
			),
			() -> IngredientNever.INSTANCE
	);

	public static final AbstractDelegatingIngredient.Serializer SERIALIZER = new AbstractDelegatingIngredient.Serializer(CODEC);

	private final ICondition condition;
	private final Ingredient ingredient;

	public ConditionalIngredient(final ICondition condition, final Ingredient ingredient) {
		this.condition = condition;
		this.ingredient = ingredient;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> serializer() {
		return ModCrafting.Ingredients.CONDITIONAL.get();
	}
}
