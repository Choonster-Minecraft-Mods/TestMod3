package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * An {@link Ingredient} that never matches any {@link ItemStack}.
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
public class NeverIngredient extends AbstractIngredient {
	public static final NeverIngredient INSTANCE = new NeverIngredient();

	public static final MapCodec<NeverIngredient> CODEC = MapCodec.unit(INSTANCE);

	private NeverIngredient() {
		super(Stream.of(
				new ItemValue(Util.make(
						new ItemStack(Items.BARRIER),
						stack -> stack.set(DataComponents.CUSTOM_NAME, Component.translatable(TestMod3Lang.INGREDIENT_NEVER_BARRIER_NAME.getTranslationKey()))
				))
		));
	}

	@Override
	public boolean test(@Nullable final ItemStack stack) {
		return false;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> serializer() {
		return ModCrafting.Ingredients.NEVER.get();
	}

	public static class Serializer implements IIngredientSerializer<NeverIngredient> {
		@Override
		public MapCodec<? extends NeverIngredient> codec() {
			return CODEC;
		}

		@Override
		public void write(final RegistryFriendlyByteBuf buffer, final NeverIngredient value) {
			// No-op
		}

		@Override
		public NeverIngredient read(final RegistryFriendlyByteBuf buffer) {
			return NeverIngredient.INSTANCE;
		}
	}
}
