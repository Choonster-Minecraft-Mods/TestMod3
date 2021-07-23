package choonster.testmod3.world.item.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.NBTIngredient;
import net.minecraftforge.common.util.Constants;
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
@SuppressWarnings("unused")
public class MobSpawnerIngredientSerializer implements IIngredientSerializer<NBTIngredient> {
	@Override
	public NBTIngredient parse(final JsonObject json) {
		final ItemStack stack = CraftingHelper.getItemStack(json, true);

		final ResourceLocation entityRegistryName = new ResourceLocation(GsonHelper.getAsString(json, "entity"));
		if (!ForgeRegistries.ENTITIES.containsKey(entityRegistryName)) {
			throw new JsonSyntaxException("Unknown entity type '" + entityRegistryName.toString() + "'");
		}

		final CompoundTag blockEntityData = stack.getOrCreateTagElement("BlockEntityTag");

		final CompoundTag spawnData = blockEntityData.getCompound("SpawnData");
		spawnData.putString("id", entityRegistryName.toString());
		blockEntityData.put("SpawnData", spawnData);

		blockEntityData.put("SpawnPotentials", blockEntityData.getList("SpawnPotentials", Constants.NBT.TAG_COMPOUND));

		return new TestMod3NBTIngredient(stack);
	}

	@Override
	public NBTIngredient parse(final FriendlyByteBuf buffer) {
		throw new UnsupportedOperationException("Can't parse from PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}

	@Override
	public void write(final FriendlyByteBuf buffer, final NBTIngredient ingredient) {
		throw new UnsupportedOperationException("Can't write to PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}
}
