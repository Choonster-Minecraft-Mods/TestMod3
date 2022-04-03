package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.TestMod3;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * An ingredient serializer that produces another {@link Ingredient} type, but only if the
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
public class ConditionalIngredientSerializer implements IIngredientSerializer<Ingredient> {
	private static final Field CONTEXT = ObfuscationReflectionHelper.findField(RecipeManager.class, "context");

	private static ICondition.IContext context = ICondition.IContext.EMPTY;

	@Override
	public Ingredient parse(final JsonObject json) {
		if (CraftingHelper.processConditions(json, "conditions", context)) {
			return CraftingHelper.getIngredient(json.get("ingredient"));
		}

		return IngredientNever.INSTANCE;
	}

	@Override
	public Ingredient parse(final FriendlyByteBuf buffer) {
		throw new UnsupportedOperationException("Can't parse from PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}

	@Override
	public void write(final FriendlyByteBuf buffer, final Ingredient ingredient) {
		throw new UnsupportedOperationException("Can't write to PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
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
