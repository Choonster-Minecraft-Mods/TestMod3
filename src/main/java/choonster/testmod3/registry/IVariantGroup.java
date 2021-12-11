package choonster.testmod3.registry;

import net.minecraft.util.StringRepresentable;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Choonster
 */
public interface IVariantGroup<VARIANT extends Enum<VARIANT> & StringRepresentable, BASE extends IForgeRegistryEntry<BASE>, ENTRY extends BASE> {
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
	 * Gets this group's entries.
	 *
	 * @return The entries
	 */
	Collection<RegistryObject<ENTRY>> getEntries();
}
