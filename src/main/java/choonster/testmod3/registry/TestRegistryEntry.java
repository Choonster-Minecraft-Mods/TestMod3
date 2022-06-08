package choonster.testmod3.registry;

import choonster.testmod3.init.ModTestRegistryEntries;

/**
 * A Registry Entry type with no functionality.
 *
 * @author Choonster
 */
public class TestRegistryEntry {
	@Override
	public String toString() {
		return "TestRegistryEntry{" + ModTestRegistryEntries.REGISTRY.get().getKey(this) + "}";
	}
}
