package choonster.testmod3.registry;

import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * A Forge Registry Entry type with no functionality.
 *
 * @author Choonster
 */
public class TestRegistryEntry extends ForgeRegistryEntry<TestRegistryEntry> {
	@Override
	public String toString() {
		return "TestRegistryEntry{" + getRegistryName() + "}";
	}
}
