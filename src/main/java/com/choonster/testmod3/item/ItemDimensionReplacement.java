package com.choonster.testmod3.item;

import com.choonster.testmod3.util.ItemStackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An item that's converted to another item when crafted in specific dimension types.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2662868-furnace-recipes
 *
 * @author Choonster
 */
public class ItemDimensionReplacement extends ItemTestMod3 {
	/**
	 * The NBT key used to indicate that the replacement logic has been run.
	 * <p>
	 * This is needed to ensure that items crafted in a dimension without a replacement don't get replaced as soon as the player enters a dimension with a replacement.
	 */
	private static final String KEY_REPLACED = "Replaced";

	/**
	 * The replacement {@link ItemStack} for each {@link DimensionType}.
	 */
	private final Map<DimensionType, ItemStack> replacements = new HashMap<>();

	public ItemDimensionReplacement(String itemName) {
		super(itemName);
	}

	/**
	 * Add a replacement for this item.
	 *
	 * @param dimensionType The dimension type
	 * @param itemStack     The replacement
	 */
	public void addReplacement(DimensionType dimensionType, ItemStack itemStack) {
		replacements.put(dimensionType, itemStack);
	}

	/**
	 * Get the replacement for the specified {@link World}.
	 *
	 * @param world The World
	 * @return The replacement
	 */
	@Nullable
	private ItemStack getReplacement(World world) {
		return replacements.get(world.provider.getDimensionType());
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		final NBTTagCompound stackTagCompound = ItemStackUtils.getOrCreateTagCompound(stack);

		if (!stackTagCompound.getBoolean(KEY_REPLACED)) { // If the replacement logic hasn't been run,
			stackTagCompound.setBoolean(KEY_REPLACED, true); // Mark it as run

			final ItemStack replacement = getReplacement(worldIn); // Get the replacement for this dimension
			if (replacement != null) { // If it exists,
				stack.deserializeNBT(replacement.serializeNBT()); // Replace this item
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		final ItemStack replacement = getReplacement(playerIn.getEntityWorld());

		if (replacement != null) {
			tooltip.add(I18n.translateToLocalFormatted("item.testmod3:dimensionReplacement.replacement.desc", replacement.getDisplayName()));
		} else {
			tooltip.add(I18n.translateToLocal("item.testmod3:dimensionReplacement.noReplacement.desc"));
		}
	}
}
