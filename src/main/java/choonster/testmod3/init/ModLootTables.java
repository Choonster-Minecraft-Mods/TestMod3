package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Store this mod's {@link LootTable} keys.
 *
 * @author Choonster
 */
public class ModLootTables {
	private static final Set<ResourceKey<LootTable>> KEYS = new HashSet<>();
	private static final Set<ResourceKey<LootTable>> IMMUTABLE_KEYS = Collections.unmodifiableSet(KEYS);

	public static final ResourceKey<LootTable> LOOT_TABLE_TEST = register("loot_table_test");

	public static final ResourceKey<LootTable> CONDITIONAL_TEST = register("conditional_test");


	private static ResourceKey<LootTable> register(final String name) {
		return register(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, name)));
	}

	private static ResourceKey<LootTable> register(final ResourceKey<LootTable> p_330139_) {
		if (KEYS.add(p_330139_)) {
			return p_330139_;
		} else {
			throw new IllegalArgumentException(p_330139_.location() + " is already a registered TestMod3 loot table");
		}
	}

	public static Set<ResourceKey<LootTable>> all() {
		return IMMUTABLE_KEYS;
	}
}
