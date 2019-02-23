package choonster.testmod3.item.variantgroup;

import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collection;
import java.util.Iterator;

/**
 * A group consisting of a collection of variants with one or more items registered for each one.
 *
 * @author Choonster
 */
public interface IItemVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> {
	/**
	 * Gets the name of this group.
	 *
	 * @return The group name
	 */
	String getGroupName();

	/**
	 * Gets the next variant in the variants collection.
	 * <p>
	 * If the specified variant is the last in the collection, the first variant is returned.
	 * <p>
	 * This is similar to (and adapted from) {@code BlockStateBase.cyclePropertyValue}.
	 *
	 * @param currentVariant The current variant
	 * @return The next variant in the variants collection.
	 */
	default VARIANT cycleVariant(final VARIANT currentVariant) {
		final Iterator<VARIANT> iterator = getVariants().iterator();

		while (iterator.hasNext()) {
			if (iterator.next().equals(currentVariant)) {
				if (iterator.hasNext()) {
					return iterator.next();
				}

				return getVariants().iterator().next();
			}
		}

		return iterator.next();
	}

	/**
	 * Gets this group's variants.
	 *
	 * @return The variants
	 */
	Iterable<VARIANT> getVariants();

	/**
	 * Gets this group's items.
	 *
	 * @return The items
	 */
	Collection<ITEM> getItems();

	/**
	 * Registers this group's items.
	 *
	 * @param registry The item registry
	 * @throws IllegalStateException If the items have already been registered
	 */
	void registerItems(IForgeRegistry<Item> registry);
}
