package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.TestMod3;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

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
@SuppressWarnings("unused")
public class IngredientNever extends Ingredient {
	private static final ResourceLocation TYPE = new ResourceLocation(TestMod3.MODID, "never");
	public static final IngredientNever INSTANCE = new IngredientNever();

	public static Codec<IngredientNever> CODEC = RecordCodecBuilder.create(instance -> instance.group(

					ResourceLocation.CODEC
							.fieldOf("type")
							.forGetter((x) -> TYPE)

			).apply(instance, (_type) -> INSTANCE)
	);

	private IngredientNever() {
		super(Stream.empty());
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
	public JsonElement toJson(final boolean allowEmpty) {
		return IngredientUtil.toJson(CODEC, this);
	}
}
