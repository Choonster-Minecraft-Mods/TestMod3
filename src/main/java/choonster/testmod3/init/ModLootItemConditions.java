package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.storage.loot.predicates.IsChestLoot;
import choonster.testmod3.world.level.storage.loot.predicates.MatchBlockTag;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link LootItemCondition} codecs.
 *
 * @author Choonster
 */
public class ModLootItemConditions {
	private static final DeferredRegister<MapCodec<? extends LootItemCondition>> LOOT_ITEM_CONDITION_TYPES =
			DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<MapCodec<? extends LootItemCondition>> IS_CHEST_LOOT = register("is_chest_loot",
			IsChestLoot.CODEC
	);

	public static final RegistryObject<MapCodec<? extends LootItemCondition>> MATCH_BLOCK_TAG = register("match_block_tag",
			MatchBlockTag.CODEC
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modBusGroup The mod bus group
	 */
	public static void initialise(final BusGroup modBusGroup) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		LOOT_ITEM_CONDITION_TYPES.register(modBusGroup);

		isInitialised = true;
	}

	private static RegistryObject<MapCodec<? extends LootItemCondition>> register(final String name, final MapCodec<? extends LootItemCondition> codec) {
		return LOOT_ITEM_CONDITION_TYPES.register(name, () -> codec);
	}
}
