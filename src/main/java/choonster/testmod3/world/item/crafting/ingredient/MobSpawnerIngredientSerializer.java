package choonster.testmod3.world.item.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

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
@SuppressWarnings("unused")
public class MobSpawnerIngredientSerializer implements IIngredientSerializer<PartialNBTIngredient> {
	@Override
	public PartialNBTIngredient parse(final JsonObject json) {
		final var stack = CraftingHelper.getItemStack(json, true);

		final var entityRegistryName = new ResourceLocation(GsonHelper.getAsString(json, "entity"));
		if (!ForgeRegistries.ENTITY_TYPES.containsKey(entityRegistryName)) {
			throw new JsonSyntaxException("Unknown entity type '" + entityRegistryName + "'");
		}

		final var blockEntityData = stack.getOrCreateTagElement("BlockEntityTag");

		final var spawnData = blockEntityData.getCompound("SpawnData");
		spawnData.putString("id", entityRegistryName.toString());
		blockEntityData.put("SpawnData", spawnData);

		blockEntityData.put("SpawnPotentials", blockEntityData.getList("SpawnPotentials", Tag.TAG_COMPOUND));

		return PartialNBTIngredient.of(stack.getItem(), Objects.requireNonNull(stack.getTag()));
	}

	@Override
	public PartialNBTIngredient parse(final FriendlyByteBuf buffer) {
		throw new UnsupportedOperationException("Can't parse from PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}

	@Override
	public void write(final FriendlyByteBuf buffer, final PartialNBTIngredient ingredient) {
		throw new UnsupportedOperationException("Can't write to PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}
}
