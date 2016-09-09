package choonster.testmod3.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Utility methods for {@link ItemStack}s
 *
 * @author Choonster
 */
public class ItemStackUtils {

	/**
	 * Get or create the compound tag of an ItemStack
	 *
	 * @param itemStack The ItemStack
	 * @return The compound tag
	 */
	public static NBTTagCompound getOrCreateTagCompound(ItemStack itemStack) {
		if (!itemStack.hasTagCompound()) {
			itemStack.setTagCompound(new NBTTagCompound());
		}

		final NBTTagCompound tagCompound = itemStack.getTagCompound();
		assert tagCompound != null;

		return tagCompound;
	}
}
