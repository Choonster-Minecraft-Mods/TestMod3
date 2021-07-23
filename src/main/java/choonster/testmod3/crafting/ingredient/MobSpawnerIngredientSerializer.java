package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
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

		final ResourceLocation entityRegistryName = new ResourceLocation(JSONUtils.getAsString(json, "entity"));
		if (!ForgeRegistries.ENTITIES.containsKey(entityRegistryName)) {
			throw new JsonSyntaxException("Unknown entity type '" + entityRegistryName.toString() + "'");
		}

		final CompoundNBT tileEntityData = stack.getOrCreateTagElement("BlockEntityTag");

		final CompoundNBT spawnData = tileEntityData.getCompound("SpawnData");
		spawnData.putString("id", entityRegistryName.toString());
		tileEntityData.put("SpawnData", spawnData);

		tileEntityData.put("SpawnPotentials", tileEntityData.getList("SpawnPotentials", Constants.NBT.TAG_COMPOUND));

		return new TestMod3NBTIngredient(stack);
	}

	@Override
	public NBTIngredient parse(final PacketBuffer buffer) {
		throw new UnsupportedOperationException("Can't parse from PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}

	@Override
	public void write(final PacketBuffer buffer, final NBTIngredient ingredient) {
		throw new UnsupportedOperationException("Can't write to PacketBuffer, use the Ingredient's own IIngredientSerializer instead");
	}
}
