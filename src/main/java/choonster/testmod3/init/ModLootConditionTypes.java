package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.storage.loot.predicates.IsChestLoot;
import choonster.testmod3.world.level.storage.loot.predicates.MatchBlockTag;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link LootItemConditionType}s.
 *
 * @author Choonster
 */
public class ModLootConditionTypes {
	private static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITION_TYPES =
			DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<LootItemConditionType> IS_CHEST_LOOT = register("is_chest_loot",
			IsChestLoot.CODEC
	);

	public static final RegistryObject<LootItemConditionType> MATCH_BLOCK_TAG = register("match_block_tag",
			MatchBlockTag.CODEC
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
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

	private static RegistryObject<LootItemConditionType> register(final String name, final Codec<? extends LootItemCondition> codec) {
		return LOOT_ITEM_CONDITION_TYPES.register(name, () -> new LootItemConditionType(codec));
	}
}
