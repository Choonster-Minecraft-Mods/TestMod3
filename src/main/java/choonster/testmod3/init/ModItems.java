package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.entity.BlockDetectionArrow;
import choonster.testmod3.world.entity.ModArrow;
import choonster.testmod3.world.item.*;
import choonster.testmod3.world.item.component.lastusetime.LastUseTimeProperties;
import choonster.testmod3.world.item.component.pigspawner.FinitePigSpawner;
import choonster.testmod3.world.item.component.pigspawner.InfinitePigSpawner;
import choonster.testmod3.world.item.variantgroup.ItemVariantGroup;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.GatherComponentsEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<CuttingAxeItem> WOODEN_AXE = registerItem("wooden_axe",
			(properties) -> new CuttingAxeItem(ToolMaterial.WOOD, 6.0f, -3.2f, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<EntityTestItem> ENTITY_TEST = registerItem("entity_test",
			EntityTestItem::new,
			ModItems::defaultItemProperties
	);

	/*
	 * A music disc.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2408066-try-creating-a-music-disc-in-my-1-8-mod-please
	 */
	public static final RegistryObject<Item> MUSIC_DISC_SOLARIS = registerItem("music_disc_solaris",
			Item::new,
			() -> defaultItemProperties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.SOLARIS)
	);

	public static final RegistryObject<HeavyItem> HEAVY = registerItem("heavy",
			HeavyItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<EntityInteractionTestItem> ENTITY_INTERACTION_TEST = registerItem("entity_interaction_test",
			EntityInteractionTestItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<BlockDestroyerItem> BLOCK_DESTROYER = registerItem("block_destroyer",
			BlockDestroyerItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SubscriptsItem> SUBSCRIPTS = registerItem("subscripts",
			SubscriptsItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SuperscriptsItem> SUPERSCRIPTS = registerItem("superscripts",
			SuperscriptsItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<LastUseTimeModelItem> MODEL_TEST = registerItem("model_test",
			LastUseTimeModelItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<SnowballLauncherItem> SNOWBALL_LAUNCHER = registerItem("snowball_launcher",
			SnowballLauncherItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SlingshotItem> SLINGSHOT = registerItem("slingshot",
			SlingshotItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<UnicodeTooltipsItem> UNICODE_TOOLTIPS = registerItem("unicode_tooltips",
			UnicodeTooltipsItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SwapTestItem> SWAP_TEST_A;

	public static final RegistryObject<SwapTestItem> SWAP_TEST_B;

	static {
		final var swapTestA = "swap_test_a";
		final var swapTestB = "swap_test_b";

		// Initialise the fields with lazy references to the items first,
		// allowing them to be referenced from the constructors below
		SWAP_TEST_A = RegistryObject.create(ITEMS.key(swapTestA).location(), ForgeRegistries.ITEMS);
		SWAP_TEST_B = RegistryObject.create(ITEMS.key(swapTestB).location(), ForgeRegistries.ITEMS);

		// Then register the items
		registerItem(swapTestA,
				(properties) -> new SwapTestItem(() -> new ItemStack(SWAP_TEST_B.get()), properties),
				ModItems::defaultItemProperties
		);

		registerItem(swapTestB,
				(properties) -> new SwapTestItem(() -> new ItemStack(SWAP_TEST_A.get()), properties),
				ModItems::defaultItemProperties
		);
	}

	public static final RegistryObject<BlockDebuggerItem> BLOCK_DEBUGGER = registerItem("block_debugger",
			BlockDebuggerItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<HarvestSwordItem> WOODEN_HARVEST_SWORD = registerItem("wooden_harvest_sword",
			(properties) -> new HarvestSwordItem(ToolMaterial.WOOD, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<HarvestSwordItem> DIAMOND_HARVEST_SWORD = registerItem("diamond_harvest_sword",
			(properties) -> new HarvestSwordItem(ToolMaterial.DIAMOND, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<ClearerItem> CLEARER = registerItem("clearer",
			ClearerItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<ModBowItem> BOW = registerItem("bow",
			ModBowItem::new,
			() -> defaultItemProperties().durability(384)
	);

	public static final RegistryObject<ModArrowItem> ARROW = registerItem("arrow",
			(properties) -> new ModArrowItem(ModArrow::new, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<HeightTesterItem> HEIGHT_TESTER = registerItem("height_tester",
			HeightTesterItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<PigSpawnerItem> PIG_SPAWNER_FINITE = registerItem("pig_spawner_finite",
			PigSpawnerItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<PigSpawnerItem> PIG_SPAWNER_INFINITE = registerItem("pig_spawner_infinite",
			PigSpawnerItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<ContinuousBowItem> CONTINUOUS_BOW = registerItem("continuous_bow",
			ContinuousBowItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<RespawnerItem> RESPAWNER = registerItem("respawner",
			RespawnerItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<LootTableTestItem> LOOT_TABLE_TEST = registerItem("loot_table_test",
			LootTableTestItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<MaxHealthSetterItem> MAX_HEALTH_SETTER_ITEM = registerItem("max_health_setter_item",
			MaxHealthSetterItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<MaxHealthGetterItem> MAX_HEALTH_GETTER_ITEM = registerItem("max_health_getter_item",
			MaxHealthGetterItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SoundEffectItem> GUN = registerItem("gun",
			(properties) -> new SoundEffectItem(ModSoundEvents.NINE_MM_FIRE, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<DimensionReplacementItem> DIMENSION_REPLACEMENT = registerItem("dimension_replacement",
			(properties) -> new DimensionReplacementItem(
					Util.make(() -> {
						final var builder = ImmutableMap.<ResourceKey<DimensionType>, Supplier<ItemStack>>builder();

						builder.put(BuiltinDimensionTypes.NETHER, () -> new ItemStack(Items.NETHER_STAR));
						builder.put(BuiltinDimensionTypes.END, () -> new ItemStack(Items.ENDER_PEARL));

						return builder.build();
					}),
					properties
			),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SoundEffectItem> SADDLE = registerItem("saddle",
			(properties) -> new SoundEffectItem(ModSoundEvents.ACTION_SADDLE, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SlowSwordItem> WOODEN_SLOW_SWORD = registerItem("wooden_slow_sword",
			(properties) -> new SlowSwordItem(ToolMaterial.WOOD, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<SlowSwordItem> DIAMOND_SLOW_SWORD = registerItem("diamond_slow_sword",
			(properties) -> new SlowSwordItem(ToolMaterial.DIAMOND, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<RitualCheckerItem> RITUAL_CHECKER = registerItem("ritual_checker",
			RitualCheckerItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<HiddenBlockRevealerItem> HIDDEN_BLOCK_REVEALER = registerItem("hidden_block_revealer",
			HiddenBlockRevealerItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<Item> NO_MOD_NAME = registerItem("no_mod_name",
			Item::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<KeyItem> KEY = registerItem("key",
			KeyItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<ModArrowItem> BLOCK_DETECTION_ARROW = registerItem("block_detection_arrow",
			(properties) -> new ModArrowItem(BlockDetectionArrow::new, properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<Item> TRANSLUCENT_ITEM = registerItem("translucent_item",
			Item::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<EntityKillerItem> ENTITY_KILLER = registerItem("entity_killer",
			EntityKillerItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<ChunkEnergySetterItem> CHUNK_ENERGY_SETTER = registerItem("chunk_energy_setter",
			ChunkEnergySetterItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<ChunkEnergyGetterItem> CHUNK_ENERGY_GETTER = registerItem("chunk_energy_getter",
			ChunkEnergyGetterItem::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<Item> CHUNK_ENERGY_DISPLAY = registerItem("chunk_energy_display",
			Item::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<Item> BEACON_ITEM = registerItem("beacon_item",
			Item::new,
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<PotionEffectArmourItem> SATURATION_HELMET = registerItem("saturation_helmet",
			(properties) -> new PotionEffectArmourItem(
					new MobEffectInstance(MobEffects.SATURATION, 1, 0, true, false),
					properties
			),
			() -> humanoidArmor(defaultItemProperties(), ArmorMaterials.CHAINMAIL, ArmorType.HELMET)
	);

	public static final RegistryObject<EntityCheckerItem> ENTITY_CHECKER = registerItem("entity_checker",
			EntityCheckerItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<Item> RUBBER = registerItem("rubber",
			Item::new,
			ModItems::defaultItemProperties
	);


	public static final RegistryObject<ReplacementArmourItem> REPLACEMENT_HELMET;

	public static final RegistryObject<RestrictedArmourItem> REPLACEMENT_CHESTPLATE = registerItem("replacement_chestplate",
			RestrictedArmourItem::new,
			() -> humanoidArmor(defaultItemProperties(), ModArmorMaterials.REPLACEMENT, ArmorType.CHESTPLATE)
	);

	public static final RegistryObject<RestrictedArmourItem> REPLACEMENT_LEGGINGS = registerItem("replacement_leggings",
			RestrictedArmourItem::new,
			() -> humanoidArmor(defaultItemProperties(), ModArmorMaterials.REPLACEMENT, ArmorType.LEGGINGS)
	);

	public static final RegistryObject<RestrictedArmourItem> REPLACEMENT_BOOTS = registerItem("replacement_boots",
			RestrictedArmourItem::new,
			() -> humanoidArmor(defaultItemProperties(), ModArmorMaterials.REPLACEMENT, ArmorType.BOOTS)
	);

	static {
		REPLACEMENT_HELMET = registerItem("replacement_helmet",
				(properties) -> new ReplacementArmourItem(
						ImmutableSet.of(
								(registryAccess) -> {
									final var enchantments = registryAccess.lookupOrThrow(Registries.ENCHANTMENT);
									final var chest = new ItemStack(REPLACEMENT_CHESTPLATE.get());
									chest.enchant(enchantments.getOrThrow(Enchantments.SHARPNESS), 1);
									return chest;
								},
								(registryAccess) -> new ItemStack(REPLACEMENT_LEGGINGS.get()),
								(registryAccess) -> new ItemStack(REPLACEMENT_BOOTS.get())
						),
						properties
				),
				() -> humanoidArmor(defaultItemProperties(), ModArmorMaterials.REPLACEMENT, ArmorType.HELMET)
		);
	}

	public static final RegistryObject<FluidStackItem> FLUID_STACK_ITEM = registerItem("fluid_stack_item",
			FluidStackItem::new,
			ModItems::defaultItemProperties // Component registered in EventHandler
	);

	public static final RegistryObject<SpawnEggItem> PLAYER_AVOIDING_CREEPER_SPAWN_EGG = registerItem("player_avoiding_creeper_spawn_egg",
			(properties) -> new SpawnEggItem(ModEntities.PLAYER_AVOIDING_CREEPER.get(), properties),
			ModItems::defaultItemProperties
	);

	public static final RegistryObject<ModBucketItem> WOODEN_BUCKET = registerItem("wooden_bucket",
			ModBucketItem::new,
			() -> defaultItemProperties().stacksTo(16)
	);

	public static final RegistryObject<ModBucketItem> STONE_BUCKET = registerItem("stone_bucket",
			ModBucketItem::new,
			() -> defaultItemProperties().stacksTo(16)
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
	 * @param modBusGroup The mod bus group
	 */
	public static void initialise(final BusGroup modBusGroup) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		ITEMS.register(modBusGroup);

		isInitialised = true;
	}

	/**
	 * @return A collection of this mod's items in the order of their registration.
	 */
	static Collection<RegistryObject<Item>> orderedItems() {
		return ITEMS.getEntries();
	}

	/**
	 * Registers an item.
	 *
	 * @param name           The registry name of the item
	 * @param itemFactory    The factory used to create the item
	 * @param itemProperties The properties of the item
	 * @param <ITEM>         The item type
	 * @return A RegistryObject reference to the item
	 */
	private static <ITEM extends Item> RegistryObject<ITEM> registerItem(
			final String name,
			final IItemFactory<ITEM> itemFactory,
			final Supplier<Item.Properties> itemProperties
	) {
		final var itemId = ITEMS.key(name);

		return ITEMS.register(name, () -> itemFactory.create(itemProperties.get().setId(itemId)));
	}

	/**
	 * Gets an {@link Item.Properties} instance with the default item properties.
	 *
	 * @return The item properties
	 */
	private static Item.Properties defaultItemProperties() {
		return new Item.Properties();
	}

	/**
	 * <p>
	 * A wrapper around {@link Item.Properties#humanoidArmor(ArmorMaterial, ArmorType)} that binds the
	 * {@link ArmorMaterial#repairIngredient()} tag to an empty list to prevent an "Unbound tags" crash from
	 * {@link net.minecraft.client.resources.model.ClientItemInfoLoader}/
	 * {@link net.minecraft.client.multiplayer.ClientRegistryLayer}.
	 * </p>
	 * <p>
	 * Vanilla does this for its own armour/tool tags in {@link BuiltInRegistries#bootStrap()}.
	 * </p>
	 */
	private static Item.Properties humanoidArmor(final Item.Properties properties, final ArmorMaterial armorMaterial, final ArmorType armorType) {
		final var newProperties = properties.humanoidArmor(armorMaterial, armorType);

		final var items = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM);
		final var tag = items.getOrThrow(armorMaterial.repairIngredient());
		tag.bind(List.of());

		return newProperties;
	}

	/**
	 * A factory function used to create items.
	 *
	 * @param <ITEM>> The item type
	 */
	private interface IItemFactory<ITEM extends Item> {
		ITEM create(Item.Properties properties);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	private static class EventHandler {
		/**
		 * Adds mod components to this mod's items.
		 * <p>
		 * Vanilla components are added to this mod's items at registration time,
		 * components are added to Vanilla items in dedicated event handlers.
		 */
		@SubscribeEvent
		public static void gatherItemComponents(final GatherComponentsEvent.Item event) {
			final var owner = event.getOwner();

			if (owner == ENTITY_INTERACTION_TEST.get()) {
				event.register(ModDataComponents.ENTITY_INTERACTION_COUNT.get(), 0);
			} else if (owner == MODEL_TEST.get()) {
				event.register(
						ModDataComponents.LAST_USE_TIME_PROPERTIES.get(),
						new LastUseTimeProperties(0, true)
				);
			} else if (owner == SLINGSHOT.get()) {
				event.register(
						ModDataComponents.LAST_USE_TIME_PROPERTIES.get(),
						new LastUseTimeProperties(0, false)
				);
			} else if (owner == CLEARER.get()) {
				event.register(ModDataComponents.CLEARER_MODE.get(), ClearerItem.ClearerMode.WHITELIST);
			} else if (owner == PIG_SPAWNER_FINITE.get()) {
				event.register(ModDataComponents.PIG_SPAWNER.get(), FinitePigSpawner.empty(20));
			} else if (owner == PIG_SPAWNER_INFINITE.get()) {
				event.register(ModDataComponents.PIG_SPAWNER.get(), InfinitePigSpawner.INSTANCE);
			} else if (owner == ENTITY_CHECKER.get()) {
				event.register(
						ModDataComponents.ENTITY_CHECKER_PROPERTIES.get(),
						EntityCheckerItem.EntityCheckerProperties.DEFAULT
				);
			} else if (owner == FLUID_STACK_ITEM.get()) {
				event.register(ModDataComponents.FLUID_STACK.get(), FluidStack.EMPTY);
			}
		}
	}
}
