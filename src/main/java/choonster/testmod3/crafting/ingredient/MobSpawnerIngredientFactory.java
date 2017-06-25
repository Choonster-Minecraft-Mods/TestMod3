package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.common.util.Constants;

/**
 * An ingredient factory that produces a mob spawner of the specified entity.
 * <p>
 * JSON Properties:
 * <ul>
 * <li>All properties used by {@link CraftingHelper#getItemStack}</li>
 * <li><code>entity</code> - The registry name of the {@link Entity} class (required string)</li>
 * </ul>
 *
 * @author Choonster
 */
@SuppressWarnings("unused")
public class MobSpawnerIngredientFactory implements IIngredientFactory {
	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		final ItemStack stack = CraftingHelper.getItemStack(json, context);

		final ResourceLocation entityName = new ResourceLocation(context.appendModId(JsonUtils.getString(json, "entity")));
		if (!EntityList.isRegistered(entityName)) {
			throw new JsonSyntaxException("Unknown entity '" + entityName.toString() + "'");
		}

		final NBTTagCompound tileEntityData = stack.getOrCreateSubCompound("BlockEntityTag");

		final NBTTagCompound spawnData = tileEntityData.getCompoundTag("SpawnData");
		spawnData.setString("id", entityName.toString());
		tileEntityData.setTag("SpawnData", spawnData);

		tileEntityData.setTag("SpawnPotentials", tileEntityData.getTagList("SpawnPotentials", Constants.NBT.TAG_COMPOUND));

		return new IngredientNBTTestMod3(stack);
	}
}
