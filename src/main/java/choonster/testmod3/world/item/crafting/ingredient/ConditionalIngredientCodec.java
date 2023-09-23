package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.TestMod3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * An ingredient codec that produces another {@link Ingredient} type, but only if the
 * specified conditions are met. If they aren't, it produces {@link IngredientNever#INSTANCE} instead.
 * <p>
 * JSON Properties:
 * <ul>
 * <li><code>conditions</code> - An array of condition objects, using the same format and condition types as the
 * <code>conditions</code> property of the top-level recipe object</li>
 * <li><code>ingredient</code> - An ingredient object or an array of ingredient objects, using the same format and
 * ingredient types as recipes</li>
 * </ul>
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
public class ConditionalIngredientCodec {
	public static final ResourceLocation TYPE = new ResourceLocation(TestMod3.MODID, "conditional");
	private static final Field CONTEXT = ObfuscationReflectionHelper.findField(RecipeManager.class, "context");

	private static ICondition.IContext context = ICondition.IContext.EMPTY;

	public static final Codec<Data> DATA_CODEC = RecordCodecBuilder.<Data>create(builder -> builder.group(

					ICondition.CODEC
							.listOf()
							.fieldOf("conditions")
							.forGetter(Data::conditions),

					Ingredient.CODEC_NONEMPTY
							.fieldOf("ingredient")
							.forGetter(Data::ingredient),

					ResourceLocation.CODEC
							.fieldOf("type")
							.forGetter((_data) -> TYPE)

			).apply(builder, (conditions, ingredient, _type) -> new Data(conditions, ingredient))
	);

	public static Codec<Ingredient> CODEC = DATA_CODEC.flatComapMap(
			data -> {
				var conditionsMatch = data.conditions
						.stream()
						.allMatch(condition -> condition.test(context));

				return conditionsMatch ? data.ingredient() : IngredientNever.INSTANCE;
			},
			ingredient -> DataResult.error(() -> "Can't convert Ingredient back to ConditionalIngredientCodec.Data")
	);

	public record Data(List<ICondition> conditions, Ingredient ingredient) {
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class EventHandler {
		@SubscribeEvent
		public static void captureServerResources(final AddReloadListenerEvent event) {
			// Capture the IContext before recipes are loaded
			final var serverResources = event.getServerResources();
			final var recipeManager = serverResources.getRecipeManager();

			try {
				context = (ICondition.IContext) CONTEXT.get(recipeManager);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Failed to get IContext from RecipeManager", e);
			}

			// Clear the IContext after recipes are loaded
			event.addListener((ResourceManagerReloadListener) resourceManager -> context = ICondition.IContext.EMPTY);
		}
	}
}
