package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
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

	public static final Codec<NeverIngredient> CODEC = Codec.unit(INSTANCE);

	private NeverIngredient() {
		super(Stream.of(
				new ItemValue(Util.make(
						new ItemStack(Items.BARRIER),
						stack -> stack.setHoverName(Component.translatable(TestMod3Lang.INGREDIENT_NEVER_BARRIER_NAME.getTranslationKey()))
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
		public Codec<? extends NeverIngredient> codec() {
			return CODEC;
		}

		@Override
		public NeverIngredient read(final FriendlyByteBuf buffer) {
			return NeverIngredient.INSTANCE;
		}

		@Override
		public void write(final FriendlyByteBuf buffer, final NeverIngredient ingredient) {
			// No-op
		}
	}
}
