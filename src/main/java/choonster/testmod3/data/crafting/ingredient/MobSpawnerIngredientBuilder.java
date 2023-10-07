package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.util.ModJsonUtil;
import choonster.testmod3.world.item.crafting.ingredient.MobSpawnerIngredientSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Builds an {@link Ingredient} that can be deserialised by {MobSpawnerIngredientSerializer}.
 *
 * @author Choonster
 */
public class MobSpawnerIngredientBuilder {
	private final Item spawner;
	@Nullable
	private EntityType<?> entityType;

	private MobSpawnerIngredientBuilder(final Item spawner) {
		this.spawner = spawner;
	}

	/**
	 * Creates a new {@link MobSpawnerIngredientBuilder}.
	 *
	 * @param spawner The base spawner item
	 * @return The builder
	 */
	public static MobSpawnerIngredientBuilder mobSpawnerIngredient(final ItemLike spawner) {
		return new MobSpawnerIngredientBuilder(spawner.asItem());
	}

	/**
	 * Sets the {@link EntityType} to be spawned.
	 *
	 * @param entityType The EntityType
	 * @return This builder
	 */
	public MobSpawnerIngredientBuilder entity(final EntityType<?> entityType) {
		this.entityType = entityType;
		return this;
	}

	/**
	 * Builds the final {@link Ingredient}.
	 *
	 * @return The Ingredient
	 */
	public Result build() {
		if (spawner == Items.AIR) {
			throw new IllegalStateException("Mob Spawner ingredient has empty spawner item");
		}

		if (entityType == null) {
			throw new IllegalStateException("Mob spawner ingredient " + spawner + " has no EntityType");
		}

		if (ForgeRegistries.ENTITY_TYPES.getKey(entityType) == null) {
			throw new IllegalStateException("Mob spawner ingredient " + spawner + " has EntityType with no registry name");
		}

		return new Result(spawner, entityType);
	}

	/**
	 * An {@link Ingredient} that serialises into JSON that can be deserialised by {@link MobSpawnerIngredientSerializer}.
	 * <p>
	 * Note: This is only intended for use during recipe generation, it won't match any items if used in a recipe during gameplay.
	 */
	public static class Result extends AbstractIngredient {
		private final Item spawner;
		private final EntityType<?> entityType;

		private Result(final Item spawner, final EntityType<?> entityType) {
			super(Stream.empty());
			this.spawner = spawner;
			this.entityType = entityType;
		}

		@Override
		public JsonElement toJson(final boolean p_299391_) {
			final var data = new MobSpawnerIngredientSerializer.Data(spawner, entityType);

			final var output = (JsonObject) ModJsonUtil.toJson(MobSpawnerIngredientSerializer.DATA_CODEC, data);

			// Manually add the type to the output
			output.addProperty("type", ModCrafting.Ingredients.MOB_SPAWNER.getId().toString());

			return output;
		}

		@Override
		public boolean isSimple() {
			return false;
		}

		@Override
		public IIngredientSerializer<? extends Ingredient> serializer() {
			return ModCrafting.Ingredients.MOB_SPAWNER.get();
		}
	}
}
