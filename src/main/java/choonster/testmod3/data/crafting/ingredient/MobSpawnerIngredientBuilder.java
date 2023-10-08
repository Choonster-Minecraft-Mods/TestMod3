package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.world.item.crafting.ingredient.MobSpawnerIngredient;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

/**
 * Builder for {@link MobSpawnerIngredient}.
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
	public MobSpawnerIngredient build() {
		if (spawner == Items.AIR) {
			throw new IllegalStateException("Mob Spawner ingredient has empty spawner item");
		}

		if (entityType == null) {
			throw new IllegalStateException("Mob spawner ingredient " + spawner + " has no EntityType");
		}

		if (ForgeRegistries.ENTITY_TYPES.getKey(entityType) == null) {
			throw new IllegalStateException("Mob spawner ingredient " + spawner + " has EntityType with no registry name");
		}

		return new MobSpawnerIngredient(spawner, entityType);
	}
}
