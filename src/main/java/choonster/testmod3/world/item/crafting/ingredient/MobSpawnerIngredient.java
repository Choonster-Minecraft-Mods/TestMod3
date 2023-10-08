package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.util.RegistryUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.common.crafting.ingredients.PartialNBTIngredient;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * An ingredient that matches a mob spawner of the specified entity.
 *
 * @author Choonster
 */
public class MobSpawnerIngredient extends AbstractDelegatingIngredient {
	public static final Codec<MobSpawnerIngredient> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(

					ForgeRegistries.ITEMS.getCodec()
							.fieldOf("item")
							.forGetter(i -> i.item),

					ForgeRegistries.ENTITY_TYPES.getCodec()
							.fieldOf("entity")
							.forGetter(i -> i.entityType)

			).apply(instance, MobSpawnerIngredient::new)
	);

	public static final Codec<Ingredient> CODEC = DATA_CODEC.flatComapMap(
			mobSpawnerIngredient -> PartialNBTIngredient.builder()
					.item(mobSpawnerIngredient.item)
					.nbt(
							CompoundTag.builder()
									.tag(
											"BlockEntityTag",
											CompoundTag.builder()
													.tag(
															"SpawnData",
															CompoundTag.builder()
																	.put("id", RegistryUtil.getKey(mobSpawnerIngredient.entityType).toString())
																	.build()
													)
													.build()

									)
									.build()
					)
					.build(),
			ingredient -> ingredient instanceof MobSpawnerIngredient mobSpawnerIngredient ?
					DataResult.success(mobSpawnerIngredient) :
					DataResult.error(() -> "Can't convert Ingredient to MobSpawnerIngredient")
	);

	public static final AbstractDelegatingIngredient.Serializer SERIALIZER = new AbstractDelegatingIngredient.Serializer(CODEC);

	private final Item item;
	private final EntityType<?> entityType;

	public MobSpawnerIngredient(final Item item, final EntityType<?> entityType) {
		this.item = item;
		this.entityType = entityType;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> serializer() {
		return ModCrafting.Ingredients.MOB_SPAWNER.get();
	}
}
