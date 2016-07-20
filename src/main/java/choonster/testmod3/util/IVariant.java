package choonster.testmod3.util;

import net.minecraft.util.IStringSerializable;

/**
 * An interface representing a block or item variant.
 *
 * @author Choonster
 */
public interface IVariant extends IStringSerializable {

	/**
	 * Get the metadata value of this variant.
	 *
	 * @return The metadata value
	 */
	int getMeta();
}
