package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.util.RegistryUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.common.crafting.ingredients.PartialNBTIngredient;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * An ingredient serializer that produces a mob spawner of the specified entity.
 * <p>
 * JSON Properties:
 * <ul>
 * <li>All properties used by {@link CraftingHelper#getItemStack} (including NBT)</li>
 * <li><code>entity</code> - The registry name of the {@link EntityType} (required string)</li>
 * </ul>
 *
 * @author Choonster
 */
public class MobSpawnerIngredientSerializer implements IIngredientSerializer<Ingredient> {
	public static final Codec<Data> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(

					ForgeRegistries.ITEMS.getCodec()
							.fieldOf("item")
							.forGetter(Data::item),

					ForgeRegistries.ENTITY_TYPES.getCodec()
							.fieldOf("entity")
							.forGetter(Data::entityType)

			).apply(instance, Data::new)
	);

	public static final Codec<Ingredient> CODEC = DATA_CODEC.flatComapMap(
			data -> PartialNBTIngredient.builder()
					.item(data.item)
					.nbt(
							CompoundTag.builder()
									.tag(
											"BlockEntityTag",
											CompoundTag.builder()
													.tag(
															"SpawnData",
															CompoundTag.builder()
																	.put("id", RegistryUtil.getKey(data.entityType).toString())
																	.build()
													)
													.build()

									)
									.build()
					)
					.build(),
			ingredient -> DataResult.error(() -> "Can't convert Ingredient back to MobSpawnerIngredientSerializer.Data")
	);

	@Override
	public Codec<? extends Ingredient> codec() {
		return CODEC;
	}

	@Override
	public void write(final FriendlyByteBuf buffer, final Ingredient value) {
		throw new UnsupportedOperationException("Can't write to FriendlyByteBuf, use the Ingredient's own IIngredientSerializer instead");
	}

	@Override
	public Ingredient read(final FriendlyByteBuf buffer) {
		throw new UnsupportedOperationException("Can't read from FriendlyByteBuf, use the Ingredient's own IIngredientSerializer instead");
	}

	public record Data(Item item, EntityType<?> entityType) {
	}
}
