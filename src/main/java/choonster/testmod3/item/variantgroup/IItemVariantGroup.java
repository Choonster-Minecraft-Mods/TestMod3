package choonster.testmod3.item.variantgroup;

import choonster.testmod3.registry.IVariantGroup;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;

/**
 * A group consisting of a collection of variants with one or more items registered for each one.
 *
 * @author Choonster
 */
public interface IItemVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> extends IVariantGroup<VARIANT, Item, ITEM> {
	/**
	 * Gets this group's items.
	 *
	 * @return The items
	 */
	default Collection<RegistryObject<ITEM>> getItems() {
		return getEntries();
	}
}
