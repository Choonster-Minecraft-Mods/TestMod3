package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.storage.loot.modifiers.BlockEntityNBTLootModifier;
import choonster.testmod3.world.level.storage.loot.modifiers.ItemLootModifier;
import choonster.testmod3.world.level.storage.loot.modifiers.LootTableLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link GlobalLootModifierSerializer}s.
 *
 * @author Choonster
 */
public class ModLootModifierSerializers {
	private static final DeferredRegister<GlobalLootModifierSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<LootTableLootModifier.Serializer> LOOT_TABLE = SERIALIZERS.register(
			"loot_table",
			LootTableLootModifier.Serializer::new
	);

	public static final RegistryObject<BlockEntityNBTLootModifier.Serializer> TILE_ENTITY_NBT = SERIALIZERS.register(
			"tile_entity_nbt",
			BlockEntityNBTLootModifier.Serializer::new
	);

	public static final RegistryObject<ItemLootModifier.Serializer> ITEM = SERIALIZERS.register(
			"item",
			ItemLootModifier.Serializer::new
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

		SERIALIZERS.register(modEventBus);

		isInitialised = true;
	}
}
