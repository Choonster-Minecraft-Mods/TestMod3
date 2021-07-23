package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.TestMod3;
import choonster.testmod3.crafting.ingredient.MobSpawnerIngredientSerializer;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.StackList;

import java.util.Collections;
import java.util.stream.Stream;

/**
 * Builds an {@link Ingredient} that can be deserialised by {@link MobSpawnerIngredientSerializer}.
 *
 * @author Choonster
 */
public class MobSpawnerIngredientBuilder {
	private final ItemStack spawner;
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
	public static MobSpawnerIngredientBuilder mobSpawnerIngredient(final IItemProvider spawner) {
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

		if (entityType.getRegistryName() == null) {
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
	 * An {@link Ingredient} that serialises into JSON that can be deserialised by {@link MobSpawnerIngredientSerializer}.
	 * <p>
	 * Note: This is only intended for use during recipe generation, it won't match any items if used in a recipe during gameplay.
	 */
	public static class Result extends Ingredient {
		private final EntityType<?> entityType;

		private Result(final ItemStack spawner, final EntityType<?> entityType) {
			super(Stream.of(new StackList(Collections.singleton(spawner))));
			this.entityType = entityType;
		}

		@Override
		public JsonElement toJson() {
			final JsonObject rootObject = super.toJson().getAsJsonObject();

			rootObject.addProperty("type", new ResourceLocation(TestMod3.MODID, "mob_spawner").toString());
			rootObject.addProperty("entity", Preconditions.checkNotNull(entityType.getRegistryName()).toString());

			return rootObject;
		}
	}
}
