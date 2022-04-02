package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.pigspawner.FinitePigSpawner;
import choonster.testmod3.capability.pigspawner.InfinitePigSpawner;
import choonster.testmod3.world.entity.BlockDetectionArrow;
import choonster.testmod3.world.entity.ModArrow;
import choonster.testmod3.world.item.*;
import choonster.testmod3.world.item.variantgroup.ItemVariantGroup;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<CuttingAxeItem> WOODEN_AXE = ITEMS.register("wooden_axe",
			() -> new CuttingAxeItem(Tiers.WOOD, 6.0f, -3.2f, defaultItemProperties())
	);

	public static final RegistryObject<EntityTestItem> ENTITY_TEST = ITEMS.register("entity_test",
			() -> new EntityTestItem(defaultItemProperties())
	);

	/*
	 * A music disc.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2408066-try-creating-a-music-disc-in-my-1-8-mod-please
	 */
	public static final RegistryObject<RecordItem> RECORD_SOLARIS = ITEMS.register("record_solaris",
			() -> new RecordItem(13, ModSoundEvents.RECORD_SOLARIS, defaultItemProperties())
	);

	public static final RegistryObject<HeavyItem> HEAVY = ITEMS.register("heavy",
			() -> new HeavyItem(defaultItemProperties())
	);

	public static final RegistryObject<EntityInteractionTestItem> ENTITY_INTERACTION_TEST = ITEMS.register("entity_interaction_test",
			() -> new EntityInteractionTestItem(defaultItemProperties())
	);

	public static final RegistryObject<BlockDestroyerItem> BLOCK_DESTROYER = ITEMS.register("block_destroyer",
			() -> new BlockDestroyerItem(defaultItemProperties())
	);

	public static final RegistryObject<SubscriptsItem> SUBSCRIPTS = ITEMS.register("subscripts",
			() -> new SubscriptsItem(defaultItemProperties())
	);

	public static final RegistryObject<SuperscriptsItem> SUPERSCRIPTS = ITEMS.register("superscripts",
			() -> new SuperscriptsItem(defaultItemProperties())
	);

	public static final RegistryObject<LastUseTimeModelItem> MODEL_TEST = ITEMS.register("model_test",
			() -> new LastUseTimeModelItem(defaultItemProperties())
	);

	public static final RegistryObject<SnowballLauncherItem> SNOWBALL_LAUNCHER = ITEMS.register("snowball_launcher",
			() -> new SnowballLauncherItem(defaultItemProperties())
	);

	public static final RegistryObject<SlingshotItem> SLINGSHOT = ITEMS.register("slingshot",
			() -> new SlingshotItem(defaultItemProperties())
	);

	public static final RegistryObject<UnicodeTooltipsItem> UNICODE_TOOLTIPS = ITEMS.register("unicode_tooltips",
			() -> new UnicodeTooltipsItem(defaultItemProperties())
	);

	public static final RegistryObject<SwapTestItem> SWAP_TEST_A;

	public static final RegistryObject<SwapTestItem> SWAP_TEST_B;

	static {
		final String swapTestA = "swap_test_a";
		final String swapTestB = "swap_test_b";

		// Initialise the fields with lazy references to the items first,
		// allowing them to be referenced from the constructors below
		SWAP_TEST_A = RegistryObject.create(new ResourceLocation(TestMod3.MODID, swapTestA), ForgeRegistries.ITEMS);
		SWAP_TEST_B = RegistryObject.create(new ResourceLocation(TestMod3.MODID, swapTestB), ForgeRegistries.ITEMS);

		// Then register the items
		ITEMS.register(swapTestA,
				() -> new SwapTestItem(defaultItemProperties(), () -> new ItemStack(SWAP_TEST_B.get()))
		);

		ITEMS.register(swapTestB,
				() -> new SwapTestItem(defaultItemProperties(), () -> new ItemStack(SWAP_TEST_A.get()))
		);
	}

	public static final RegistryObject<BlockDebuggerItem> BLOCK_DEBUGGER = ITEMS.register("block_debugger",
			() -> new BlockDebuggerItem(defaultItemProperties())
	);

	public static final RegistryObject<HarvestSwordItem> WOODEN_HARVEST_SWORD = ITEMS.register("wooden_harvest_sword",
			() -> new HarvestSwordItem(Tiers.WOOD, defaultItemProperties())
	);

	public static final RegistryObject<HarvestSwordItem> DIAMOND_HARVEST_SWORD = ITEMS.register("diamond_harvest_sword",
			() -> new HarvestSwordItem(Tiers.DIAMOND, defaultItemProperties())
	);

	public static final RegistryObject<ClearerItem> CLEARER = ITEMS.register("clearer",
			() -> new ClearerItem(defaultItemProperties())
	);

	public static final RegistryObject<ModBowItem> BOW = ITEMS.register("bow",
			() -> new ModBowItem(defaultItemProperties().defaultDurability(384))
	);

	public static final RegistryObject<ModArrowItem> ARROW = ITEMS.register("arrow",
			() -> new ModArrowItem(ModArrow::new, defaultItemProperties())
	);

	public static final RegistryObject<HeightTesterItem> HEIGHT_TESTER = ITEMS.register("height_tester",
			() -> new HeightTesterItem(defaultItemProperties())
	);

	// Capabilities are registered and injected in FMLCommonSetupEvent, which is fired after RegistryEvent.Register.
	// This means that item constructors can't directly reference Capability fields (e.g. CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY).
	public static final RegistryObject<PigSpawnerItem> PIG_SPAWNER_FINITE = ITEMS.register("pig_spawner_finite",
			() -> new PigSpawnerItem(() -> new FinitePigSpawner(20), defaultItemProperties())
	);

	public static final RegistryObject<PigSpawnerItem> PIG_SPAWNER_INFINITE = ITEMS.register("pig_spawner_infinite",
			() -> new PigSpawnerItem(InfinitePigSpawner::new, defaultItemProperties())
	);

	public static final RegistryObject<ContinuousBowItem> CONTINUOUS_BOW = ITEMS.register("continuous_bow",
			() -> new ContinuousBowItem(defaultItemProperties())
	);

	public static final RegistryObject<RespawnerItem> RESPAWNER = ITEMS.register("respawner",
			() -> new RespawnerItem(defaultItemProperties())
	);

	public static final RegistryObject<LootTableTestItem> LOOT_TABLE_TEST = ITEMS.register("loot_table_test",
			() -> new LootTableTestItem(defaultItemProperties())
	);

	public static final RegistryObject<MaxHealthSetterItem> MAX_HEALTH_SETTER_ITEM = ITEMS.register("max_health_setter_item",
			() -> new MaxHealthSetterItem(defaultItemProperties())
	);

	public static final RegistryObject<MaxHealthGetterItem> MAX_HEALTH_GETTER_ITEM = ITEMS.register("max_health_getter_item",
			() -> new MaxHealthGetterItem(defaultItemProperties())
	);

	public static final RegistryObject<SoundEffectItem> GUN = ITEMS.register("gun",
			() -> new SoundEffectItem(ModSoundEvents.NINE_MM_FIRE, defaultItemProperties())
	);

	public static final RegistryObject<DimensionReplacementItem> DIMENSION_REPLACEMENT = ITEMS.register("dimension_replacement",
			() -> new DimensionReplacementItem(
					defaultItemProperties(),
					Util.make(() -> {
						final ImmutableMap.Builder<ResourceKey<DimensionType>, Supplier<ItemStack>> builder = ImmutableMap.builder();

						builder.put(DimensionType.NETHER_LOCATION, () -> new ItemStack(Items.NETHER_STAR));
						builder.put(DimensionType.END_LOCATION, () -> new ItemStack(Items.ENDER_PEARL));

						return builder.build();
					})
			)
	);

	public static final RegistryObject<SoundEffectItem> SADDLE = ITEMS.register("saddle",
			() -> new SoundEffectItem(ModSoundEvents.ACTION_SADDLE, defaultItemProperties())
	);

	public static final RegistryObject<SlowSwordItem> WOODEN_SLOW_SWORD = ITEMS.register("wooden_slow_sword",
			() -> new SlowSwordItem(Tiers.WOOD, defaultItemProperties())
	);

	public static final RegistryObject<SlowSwordItem> DIAMOND_SLOW_SWORD = ITEMS.register("diamond_slow_sword",
			() -> new SlowSwordItem(Tiers.DIAMOND, defaultItemProperties())
	);

	public static final RegistryObject<RitualCheckerItem> RITUAL_CHECKER = ITEMS.register("ritual_checker",
			() -> new RitualCheckerItem(defaultItemProperties())
	);

	public static final RegistryObject<HiddenBlockRevealerItem> HIDDEN_BLOCK_REVEALER = ITEMS.register("hidden_block_revealer",
			() -> new HiddenBlockRevealerItem(defaultItemProperties())
	);

	public static final RegistryObject<Item> NO_MOD_NAME = ITEMS.register("no_mod_name",
			() -> new Item(defaultItemProperties())
	);

	public static final RegistryObject<KeyItem> KEY = ITEMS.register("key",
			() -> new KeyItem(defaultItemProperties())
	);

	public static final RegistryObject<ModArrowItem> BLOCK_DETECTION_ARROW = ITEMS.register("block_detection_arrow",
			() -> new ModArrowItem(BlockDetectionArrow::new, defaultItemProperties())
	);

	public static final RegistryObject<Item> TRANSLUCENT_ITEM = ITEMS.register("translucent_item",
			() -> new Item(defaultItemProperties())
	);

	public static final RegistryObject<EntityKillerItem> ENTITY_KILLER = ITEMS.register("entity_killer",
			() -> new EntityKillerItem(defaultItemProperties())
	);

	public static final RegistryObject<ChunkEnergySetterItem> CHUNK_ENERGY_SETTER = ITEMS.register("chunk_energy_setter",
			() -> new ChunkEnergySetterItem(defaultItemProperties())
	);

	public static final RegistryObject<ChunkEnergyGetterItem> CHUNK_ENERGY_GETTER = ITEMS.register("chunk_energy_getter",
			() -> new ChunkEnergyGetterItem(defaultItemProperties())
	);

	public static final RegistryObject<Item> CHUNK_ENERGY_DISPLAY = ITEMS.register("chunk_energy_display",
			() -> new Item(defaultItemProperties())
	);

	public static final RegistryObject<Item> BEACON_ITEM = ITEMS.register("beacon_item",
			() -> new Item(defaultItemProperties())
	);

	public static final RegistryObject<PotionEffectArmourItem> SATURATION_HELMET = ITEMS.register("saturation_helmet",
			() -> new PotionEffectArmourItem(ArmorMaterials.CHAIN, EquipmentSlot.HEAD, new MobEffectInstance(MobEffects.SATURATION, 1, 0, true, false), defaultItemProperties())
	);

	public static final RegistryObject<EntityCheckerItem> ENTITY_CHECKER = ITEMS.register("entity_checker",
			() -> new EntityCheckerItem(defaultItemProperties())
	);

	public static final RegistryObject<Item> RUBBER = ITEMS.register("rubber",
			() -> new Item(defaultItemProperties())
	);


	public static final RegistryObject<ReplacementArmourItem> REPLACEMENT_HELMET;

	public static final RegistryObject<RestrictedArmourItem> REPLACEMENT_CHESTPLATE = ITEMS.register("replacement_chestplate",
			() -> new RestrictedArmourItem(ModArmourMaterial.REPLACEMENT, EquipmentSlot.CHEST, defaultItemProperties())
	);

	public static final RegistryObject<RestrictedArmourItem> REPLACEMENT_LEGGINGS = ITEMS.register("replacement_leggings",
			() -> new RestrictedArmourItem(ModArmourMaterial.REPLACEMENT, EquipmentSlot.LEGS, defaultItemProperties())
	);

	public static final RegistryObject<RestrictedArmourItem> REPLACEMENT_BOOTS = ITEMS.register("replacement_boots",
			() -> new RestrictedArmourItem(ModArmourMaterial.REPLACEMENT, EquipmentSlot.FEET, defaultItemProperties())
	);

	static {
		REPLACEMENT_HELMET = ITEMS.register("replacement_helmet",
				() -> new ReplacementArmourItem(
						ModArmourMaterial.REPLACEMENT,
						EquipmentSlot.HEAD,
						defaultItemProperties(),
						ImmutableSet.of(
								() -> {
									final ItemStack chest = new ItemStack(REPLACEMENT_CHESTPLATE.get());
									chest.enchant(Enchantments.SHARPNESS, 1);
									return chest;
								},
								() -> new ItemStack(REPLACEMENT_LEGGINGS.get()),
								() -> new ItemStack(REPLACEMENT_BOOTS.get())
						)
				)
		);
	}

	public static final RegistryObject<FluidStackItem> FLUID_STACK_ITEM = ITEMS.register("fluid_stack_item",
			() -> new FluidStackItem(new Item.Properties())
	);

	public static final RegistryObject<ForgeSpawnEggItem> PLAYER_AVOIDING_CREEPER_SPAWN_EGG = ITEMS.register("player_avoiding_creeper_spawn_egg",
			() -> new ForgeSpawnEggItem(ModEntities.PLAYER_AVOIDING_CREEPER, 0xda70b, 0, defaultItemProperties())
	);

	public static final RegistryObject<ModBucketItem> WOODEN_BUCKET = ITEMS.register("wooden_bucket",
			() -> new ModBucketItem(defaultItemProperties().stacksTo(16))
	);

	public static final RegistryObject<ModBucketItem> STONE_BUCKET = ITEMS.register("stone_bucket",
			() -> new ModBucketItem(defaultItemProperties().stacksTo(16))
	);


	public static final ItemVariantGroup<VariantsItem.Type, VariantsItem> VARIANTS_ITEMS = ItemVariantGroup.Builder.<VariantsItem.Type, VariantsItem>create(ITEMS)
			.groupName("variants_item")
			.suffix()
			.variants(VariantsItem.Type.values())
			.itemFactory(VariantsItem::new)
			.build();


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

		ITEMS.register(modEventBus);

		isInitialised = true;
	}

	/**
	 * Gets an {@link Item.Properties} instance with the {@link CreativeModeTab} set to {@link TestMod3#CREATIVE_MODE_TAB}.
	 *
	 * @return The item properties
	 */
	private static Item.Properties defaultItemProperties() {
		return new Item.Properties().tab(TestMod3.CREATIVE_MODE_TAB);
	}
}
