package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.util.ModJsonUtil;
import choonster.testmod3.world.item.crafting.ingredient.MobSpawnerIngredientCodec;
import com.google.gson.JsonElement;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Builds an {@link Ingredient} that can be deserialised by {MobSpawnerIngredientSerializer}.
 *
 * @author Choonster
 */
public class MobSpawnerIngredientBuilder {
	private final ItemStack spawner;
	@Nullable
	private EntityType<?> entityType;

	private MobSpawnerIngredientBuilder(final ItemStack spawner) {
		this.spawner = spawner;
	}

	/**
	 * Creates a new {@link MobSpawnerIngredientBuilder}.
	 *
	 * @param spawner The spawner Item
	 * @return The builder
	 */
	public static MobSpawnerIngredientBuilder mobSpawnerIngredient(final ItemLike spawner) {
		return mobSpawnerIngredient(new ItemStack(spawner));
	}

	/**
	 * Creates a new {@link MobSpawnerIngredientBuilder}.
	 *
	 * @param spawner The base spawner ItemStack
	 * @return The builder
	 */
	public static MobSpawnerIngredientBuilder mobSpawnerIngredient(final ItemStack spawner) {
		return new MobSpawnerIngredientBuilder(spawner);
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
	 * Validates that the spawner {@link ItemStack} is non-empty and that the {@link EntityType} has been set and has a registry name.
	 */
	private void validate() {
		if (spawner.isEmpty()) {
			throw new IllegalStateException("Mob Spawner ingredient has empty spawner ItemStack");
		}

		if (entityType == null) {
			throw new IllegalStateException("Mob spawner ingredient " + spawner + " has no EntityType");
		}

		if (ForgeRegistries.ENTITY_TYPES.getKey(entityType) == null) {
			throw new IllegalStateException("Mob spawner ingredient " + spawner + " has EntityType with no registry name");
		}
	}

	/**
	 * Builds the final {@link Ingredient}.
	 *
	 * @return The Ingredient
	 */
	public Result build() {
		validate();
		return new Result(spawner, entityType);
	}

	/**
	 * An {@link Ingredient} that serialises into JSON that can be deserialised by {@link MobSpawnerIngredientCodec}.
	 * <p>
	 * Note: This is only intended for use during recipe generation, it won't match any items if used in a recipe during gameplay.
	 */
	public static class Result extends Ingredient {
		private final ItemStack spawner;
		private final EntityType<?> entityType;

		private Result(final ItemStack spawner, final EntityType<?> entityType) {
			super(Stream.empty());
			this.spawner = spawner;
			this.entityType = entityType;
		}

		@Override
		public JsonElement toJson(final boolean p_299391_) {
			final var data = new MobSpawnerIngredientCodec.Data(spawner, entityType);

			return ModJsonUtil.toJson(MobSpawnerIngredientCodec.DATA_CODEC, data);
		}
	}
}
