package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

/**
 * A shapeless recipe that damages any {@link AxeItem} ingredients.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipe extends BaseShapelessRecipe {
	public static final MapCodec<ShapelessCuttingRecipe> MAP_CODEC =
			ShapelessRecipeCodecs.mapCodec(ShapelessCuttingRecipe::new);

	public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessCuttingRecipe> STREAM_CODEC =
			ShapelessRecipeCodecs.streamCodec(ShapelessCuttingRecipe::new);

	public static final RecipeSerializer<ShapelessCuttingRecipe> SERIALIZER =
			new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public ShapelessCuttingRecipe(
			final CommonInfo commonInfo,
			final CraftingRecipe.CraftingBookInfo bookInfo,
			final ItemStackTemplate result,
			final List<Ingredient> ingredients
	) {
		super(commonInfo, bookInfo, result, ingredients);
	}

	@SuppressWarnings("UnstableApiUsage")
	private ItemStack damageAxe(final ItemStack stack) {
		final var craftingPlayer = ForgeHooks.getCraftingPlayer();

		if (craftingPlayer.level() instanceof final ServerLevel serverLevel) {
			stack.hurtAndBreak(
					1,
					serverLevel,
					craftingPlayer instanceof final ServerPlayer serverPlayer ? serverPlayer : null,
					item -> stack.setCount(0)
			);
		}

		return stack;
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingInput input) {
		final var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (var i = 0; i < remainingItems.size(); ++i) {
			final var stack = input.getItem(i);

			if (!stack.isEmpty() && stack.getItem() instanceof AxeItem) {
				remainingItems.set(i, damageAxe(stack.copy()));
			} else {
				remainingItems.set(i, ForgeHooks.getCraftingRemainingItem(stack));
			}
		}

		return remainingItems;
	}

	@Override
	public RecipeSerializer<ShapelessCuttingRecipe> getSerializer() {
		return ModCrafting.Recipes.CUTTING_SHAPELESS.get();
	}
}
