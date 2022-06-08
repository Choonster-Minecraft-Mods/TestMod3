package choonster.testmod3.util;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.Nullable;

/**
 * Utility methods for JSON serialisation/deserialisation.
 *
 * @author Choonster
 */
public class ModJsonUtil {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

	/**
	 * Gets a fluid from the specified member of the JSON object.
	 *
	 * @param object     The JSON object
	 * @param memberName The member containing the fluid's registry name
	 * @return The fluid
	 * @throws JsonSyntaxException If the fluid registry name is missing or invalid
	 */
	public static Fluid getFluid(final JsonObject object, final String memberName) {
		final ResourceLocation registryName = new ResourceLocation(GsonHelper.getAsString(object, memberName));
		final Fluid fluid = ForgeRegistries.FLUIDS.getValue(registryName);

		if (fluid == null) {
			throw new JsonSyntaxException("Unknown fluid '" + registryName + "'");
		}

		return fluid;
	}

	/**
	 * Gets an optional compound tag from the specified member of the JSON object.
	 * <p>
	 * The member can be an object or a string containing the JSON representation of the NBT.
	 *
	 * @param json       The JSON object
	 * @param memberName The member name
	 * @return The compound tag, if any
	 * @throws JsonSyntaxException If the NBT is malformed
	 */
	@Nullable
	public static CompoundTag getCompoundTag(final JsonObject json, final String memberName) {
		final CompoundTag nbt;

		if (json.has(memberName)) {
			final JsonElement element = json.get(memberName);

			try {
				if (element.isJsonObject()) {
					nbt = TagParser.parseTag(GSON.toJson(element));
				} else {
					nbt = TagParser.parseTag(GsonHelper.convertToString(element, memberName));
				}
			} catch (final CommandSyntaxException e) {
				throw new JsonSyntaxException("Malformed NBT tag", e);
			}
		} else {
			nbt = null;
		}

		return nbt;
	}

	public static void setCompoundTag(final JsonObject json, final String memberName, final CompoundTag nbt) {
		final JsonObject object = JsonParser.parseString(nbt.toString()).getAsJsonObject();

		json.add(memberName, object);
	}
}
