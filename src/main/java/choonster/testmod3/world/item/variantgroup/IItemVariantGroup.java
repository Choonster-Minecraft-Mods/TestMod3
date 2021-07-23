package choonster.testmod3.world.item.variantgroup;

import choonster.testmod3.registry.IVariantGroup;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.Collection;

/**
 * A group consisting of a collection of variants with one or more items registered for each one.
 *
 * @author Choonster
 */
public interface IItemVariantGroup<VARIANT extends Enum<VARIANT> & StringRepresentable, ITEM extends Item> extends IVariantGroup<VARIANT, Item, ITEM> {
	/**
	 * Gets this group's items.
	 *
	 * @return The items
	 */
	default Collection<RegistryObject<ITEM>> getItems() {
		return getEntries();
	}
}
