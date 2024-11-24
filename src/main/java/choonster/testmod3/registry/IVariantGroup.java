package choonster.testmod3.registry;

import net.minecraft.util.StringRepresentable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Choonster
 */
public interface IVariantGroup<VARIANT extends Enum<VARIANT> & StringRepresentable, BASE, ENTRY extends BASE> {
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

				return getVariants().getFirst();
			}
		}

		return iterator.next();
	}

	/**
	 * Gets this group's variants.
	 *
	 * @return The variants
	 */
	List<VARIANT> getVariants();

	/**
	 * Gets this group's entries.
	 *
	 * @return The entries
	 */
	Collection<? extends Supplier<ENTRY>> getEntries();
}
