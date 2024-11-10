package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.condition.ConditionMapCodec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

/**
 * An {@link Ingredient} that produces another {@link Ingredient} type, but only if the
 * specified conditions are met. If they aren't, it produces {@link NeverIngredient#INSTANCE} instead.
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
public class ConditionalIngredient extends AbstractDelegatingIngredient {
	public static final MapCodec<ConditionalIngredient> DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(

					ICondition.CODEC
							.fieldOf(ICondition.DEFAULT_FIELD)
							.forGetter(ingredient -> ingredient.condition),

					Ingredient.CODEC
							.fieldOf("ingredient")
							.forGetter(ingredient -> ingredient.ingredient)

			).apply(instance, ConditionalIngredient::new)
	);

	public static final MapCodec<Ingredient> CODEC = ConditionMapCodec.checkingDecode(
			DATA_CODEC.flatXmap(
					conditionalIngredient -> DataResult.success(conditionalIngredient.ingredient),
					ingredient -> ingredient instanceof ConditionalIngredient conditionalIngredient ?
							DataResult.success(conditionalIngredient) :
							DataResult.error(() -> "Can't convert Ingredient to ConditionalIngredient")
			),
			() -> NeverIngredient.INSTANCE
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
