package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.registry.DeferredVanillaRegister;
import choonster.testmod3.registry.VanillaRegistryObject;
import choonster.testmod3.world.level.storage.loot.predicates.IsChestLoot;
import choonster.testmod3.world.level.storage.loot.predicates.MatchBlockTag;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link LootItemConditionType}s.
 *
 * @author Choonster
 */
public class ModLootConditionTypes {
	private static final DeferredVanillaRegister<LootItemConditionType> LOOT_ITEM_CONDITION_TYPES =
			DeferredVanillaRegister.create(Registry.LOOT_CONDITION_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final VanillaRegistryObject<LootItemConditionType> IS_CHEST_LOOT = register("is_chest_loot",
			IsChestLoot.ConditionSerializer::new
	);

	public static final VanillaRegistryObject<LootItemConditionType> MATCH_BLOCK_TAG = register("match_block_tag",
			MatchBlockTag.ConditionSerializer::new
	);

	/**
	 * Registers the {@link DeferredVanillaRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		LOOT_ITEM_CONDITION_TYPES.register(modEventBus);

		isInitialised = true;
	}

	private static VanillaRegistryObject<LootItemConditionType> register(final String name, final Supplier<Serializer<? extends LootItemCondition>> serializerFactory) {
		return LOOT_ITEM_CONDITION_TYPES.register(name, () -> new LootItemConditionType(serializerFactory.get()));
	}
}
