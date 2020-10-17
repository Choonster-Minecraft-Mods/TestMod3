package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.pigspawner.InfinitePigSpawner;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import choonster.testmod3.entity.BlockDetectionArrowEntity;
import choonster.testmod3.entity.ModArrowEntity;
import choonster.testmod3.item.*;
import choonster.testmod3.item.variantgroup.IItemVariantGroup;
import choonster.testmod3.item.variantgroup.ItemVariantGroup;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.DimensionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;

import static choonster.testmod3.util.InjectionUtil.Null;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModItems {
	public static final CuttingAxeItem WOODEN_AXE = Null();

	public static final EntityTestItem ENTITY_TEST = Null();

	public static final MusicDiscItem RECORD_SOLARIS = Null();

	public static final HeavyItem HEAVY = Null();

	public static final EntityInteractionTestItem ENTITY_INTERACTION_TEST = Null();

	public static final BlockDestroyerItem BLOCK_DESTROYER = Null();

	public static final SubscriptsItem SUBSCRIPTS = Null();

	public static final SuperscriptsItem SUPERSCRIPTS = Null();

	public static final LastUseTimeModelItem MODEL_TEST = Null();

	public static final SnowballLauncherItem SNOWBALL_LAUNCHER = Null();

	public static final SlingshotItem SLINGSHOT = Null();

	public static final UnicodeTooltipsItem UNICODE_TOOLTIPS = Null();

	public static final SwapTestItem SWAP_TEST_A = Null();

	public static final SwapTestItem SWAP_TEST_B = Null();

	public static final BlockDebuggerItem BLOCK_DEBUGGER = Null();

	public static final HarvestSwordItem WOODEN_HARVEST_SWORD = Null();

	public static final HarvestSwordItem DIAMOND_HARVEST_SWORD = Null();

	public static final ClearerItem CLEARER = Null();

	public static final ModBowItem BOW = Null();

	public static final Item ARROW = Null();

	public static final HeightTesterItem HEIGHT_TESTER = Null();

	public static final PigSpawnerItem PIG_SPAWNER_FINITE = Null();

	public static final PigSpawnerItem PIG_SPAWNER_INFINITE = Null();

	public static final ContinuousBowItem CONTINUOUS_BOW = Null();

	public static final RespawnerItem RESPAWNER = Null();

	public static final LootTableTestItem LOOT_TABLE_TEST = Null();

	public static final MaxHealthSetterItem MAX_HEALTH_SETTER_ITEM = Null();

	public static final MaxHealthGetterItem MAX_HEALTH_GETTER_ITEM = Null();

	public static final SoundEffectItem GUN = Null();

	public static final DimensionReplacementItem DIMENSION_REPLACEMENT = Null();

	public static final SoundEffectItem SADDLE = Null();

	public static final SlowSwordItem WOODEN_SLOW_SWORD = Null();

	public static final SlowSwordItem DIAMOND_SLOW_SWORD = Null();

	public static final RitualCheckerItem RITUAL_CHECKER = Null();

	public static final HiddenBlockRevealerItem HIDDEN_BLOCK_REVEALER = Null();

	public static final Item NO_MOD_NAME = Null();

	public static final KeyItem KEY = Null();

	public static final ModArrowItem BLOCK_DETECTION_ARROW = Null();

	public static final Item TRANSLUCENT_ITEM = Null();

	public static final EntityKillerItem ENTITY_KILLER = Null();

	public static final ChunkEnergySetterItem CHUNK_ENERGY_SETTER = Null();

	public static final ChunkEnergyGetterItem CHUNK_ENERGY_GETTER = Null();

	public static final Item CHUNK_ENERGY_DISPLAY = Null();

	public static final Item BEACON_ITEM = Null();

	public static final PotionEffectArmourItem SATURATION_HELMET = Null();

	public static final EntityCheckerItem ENTITY_CHECKER = Null();

	public static final Item RUBBER = Null();


	public static final ReplacementArmourItem REPLACEMENT_HELMET = Null();

	public static final RestrictedArmourItem REPLACEMENT_CHESTPLATE = Null();

	public static final RestrictedArmourItem REPLACEMENT_LEGGINGS = Null();

	public static final RestrictedArmourItem REPLACEMENT_BOOTS = Null();


//	public static final TestMod3BucketItem WOODEN_BUCKET = Null();
//
//	public static final TestMod3BucketItem STONE_BUCKET = Null();

	public static class VariantGroups {
		public static final ItemVariantGroup<VariantsItem.Type, VariantsItem> VARIANTS_ITEMS = ItemVariantGroup.Builder.<VariantsItem.Type, VariantsItem>create()
				.groupName("variants_item")
				.suffix()
				.variants(VariantsItem.Type.values())
				.itemFactory(VariantsItem::new)
				.build();
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		public static final Set<Item> ITEMS = new HashSet<>();

		/**
		 * Register this mod's {@link Item}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			ModSoundEvents.RegistrationHandler.initialiseSoundEvents();

			final SwapTestItem swapTestA = new SwapTestItem(defaultItemProperties());
			final SwapTestItem swapTestB = new SwapTestItem(defaultItemProperties());

			final DimensionReplacementItem dimensionReplacement = new DimensionReplacementItem(defaultItemProperties());

			final ReplacementArmourItem replacementHelmet = new ReplacementArmourItem(ModArmourMaterial.REPLACEMENT, EquipmentSlotType.HEAD, defaultItemProperties());
			final RestrictedArmourItem replacementChestplate = new RestrictedArmourItem(ModArmourMaterial.REPLACEMENT, EquipmentSlotType.CHEST, defaultItemProperties());
			final RestrictedArmourItem replacementLeggings = new RestrictedArmourItem(ModArmourMaterial.REPLACEMENT, EquipmentSlotType.LEGS, defaultItemProperties());
			final RestrictedArmourItem replacementBoots = new RestrictedArmourItem(ModArmourMaterial.REPLACEMENT, EquipmentSlotType.FEET, defaultItemProperties());

			// Capabilities are registered and injected in FMLCommonSetupEvent, which is fired after RegistryEvent.Register.
			// This means that item constructors can't directly reference Capability fields (e.g. CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY).
			@SuppressWarnings("Convert2MethodRef")
			final Item[] items = {
					new CuttingAxeItem(ItemTier.WOOD, 6.0f, -3.2f, defaultItemProperties()).setRegistryName("wooden_axe"),
					new EntityTestItem(defaultItemProperties()).setRegistryName("entity_test"),

					/*
					 * A music disc.
					 * <p>
					 * Test for this thread:
					 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2408066-try-creating-a-music-disc-in-my-1-8-mod-please
					*/
					new MusicDiscItem(13, () -> ModSoundEvents.RECORD_SOLARIS /* TODO: Convert to DeferredRegister/RegistryObject */, defaultItemProperties()).setRegistryName("record_solaris"),

					new HeavyItem(defaultItemProperties()).setRegistryName("heavy"),
					new EntityInteractionTestItem(defaultItemProperties()).setRegistryName("entity_interaction_test"),
					new BlockDestroyerItem(defaultItemProperties()).setRegistryName("block_destroyer"),
					new SubscriptsItem(defaultItemProperties()).setRegistryName("subscripts"),
					new SuperscriptsItem(defaultItemProperties()).setRegistryName("superscripts"),
					new LastUseTimeModelItem(defaultItemProperties()).setRegistryName("model_test"),
					new SnowballLauncherItem(defaultItemProperties()).setRegistryName("snowball_launcher"),
					new SlingshotItem(defaultItemProperties()).setRegistryName("slingshot"),
					new UnicodeTooltipsItem(defaultItemProperties()).setRegistryName("unicode_tooltips"),
					swapTestA.setRegistryName("swap_test_a"),
					swapTestB.setRegistryName("swap_test_b"),
					new BlockDebuggerItem(defaultItemProperties()).setRegistryName("block_debugger"),
					new HarvestSwordItem(ItemTier.WOOD, HarvestSwordItem.addToolTypes(ItemTier.WOOD, defaultItemProperties())).setRegistryName("wooden_harvest_sword"),
					new HarvestSwordItem(ItemTier.DIAMOND, HarvestSwordItem.addToolTypes(ItemTier.DIAMOND, defaultItemProperties())).setRegistryName("diamond_harvest_sword"),
					new ClearerItem(defaultItemProperties()).setRegistryName("clearer"),
					new ModBowItem(defaultItemProperties().defaultMaxDamage(384)).setRegistryName("bow"),
					new ModArrowItem(ModArrowEntity::new, defaultItemProperties()).setRegistryName("arrow"),
					new HeightTesterItem(defaultItemProperties()).setRegistryName("height_tester"),
					new PigSpawnerItem(() -> PigSpawnerCapability.PIG_SPAWNER_CAPABILITY.getDefaultInstance(), defaultItemProperties()).setRegistryName("pig_spawner_finite"),
					new PigSpawnerItem(InfinitePigSpawner::new, defaultItemProperties()).setRegistryName("pig_spawner_infinite"),
					new ContinuousBowItem(defaultItemProperties()).setRegistryName("continuous_bow"),
					new RespawnerItem(defaultItemProperties()).setRegistryName("respawner"),
					new LootTableTestItem(defaultItemProperties()).setRegistryName("loot_table_test"),
					new MaxHealthSetterItem(defaultItemProperties()).setRegistryName("max_health_setter_item"),
					new MaxHealthGetterItem(defaultItemProperties()).setRegistryName("max_health_getter_item"),
					new SoundEffectItem(() -> ModSoundEvents.NINE_MM_FIRE, defaultItemProperties()).setRegistryName("gun"),
					dimensionReplacement.setRegistryName("dimension_replacement"),
					new SoundEffectItem(() -> ModSoundEvents.ACTION_SADDLE, defaultItemProperties()).setRegistryName("saddle"),
					new SlowSwordItem(ItemTier.WOOD, defaultItemProperties()).setRegistryName("wooden_slow_sword"),
					new SlowSwordItem(ItemTier.DIAMOND, defaultItemProperties()).setRegistryName("diamond_slow_sword"),
					new RitualCheckerItem(defaultItemProperties()).setRegistryName("ritual_checker"),
					new HiddenBlockRevealerItem(defaultItemProperties()).setRegistryName("hidden_block_revealer"),
					new Item(defaultItemProperties()).setRegistryName("no_mod_name"),
					new KeyItem(defaultItemProperties()).setRegistryName("key"),
					new ModArrowItem(BlockDetectionArrowEntity::new, defaultItemProperties()).setRegistryName("block_detection_arrow"),
					new Item(defaultItemProperties()).setRegistryName("translucent_item"),
					new EntityKillerItem(defaultItemProperties()).setRegistryName("entity_killer"),
					new ChunkEnergySetterItem(defaultItemProperties()).setRegistryName("chunk_energy_setter"),
					new ChunkEnergyGetterItem(defaultItemProperties()).setRegistryName("chunk_energy_getter"),
					new Item(defaultItemProperties()).setRegistryName("chunk_energy_display"),
					new Item(defaultItemProperties()).setRegistryName("beacon_item"),
					new PotionEffectArmourItem(ArmorMaterial.CHAIN, EquipmentSlotType.HEAD, new EffectInstance(Effects.SATURATION, 1, 0, true, false), defaultItemProperties()).setRegistryName("saturation_helmet"),
					new EntityCheckerItem(defaultItemProperties()).setRegistryName("entity_checker"),
					new Item(defaultItemProperties()).setRegistryName("rubber"),

					replacementHelmet.setRegistryName("replacement_helmet"),
					replacementChestplate.setRegistryName("replacement_chestplate"),
					replacementLeggings.setRegistryName("replacement_leggings"),
					replacementBoots.setRegistryName("replacement_boots"),

//					new TestMod3BucketItem(defaultItemProperties()).setRegistryName("wooden_bucket"),
//					new TestMod3BucketItem(defaultItemProperties()).setRegistryName("stone_bucket"),
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}

			registerItemVariantGroup(registry, VariantGroups.VARIANTS_ITEMS);

			swapTestA.setOtherItem(new ItemStack(swapTestB));
			swapTestB.setOtherItem(new ItemStack(swapTestA));

			final ItemStack chest = new ItemStack(replacementChestplate);
			chest.addEnchantment(Enchantments.SHARPNESS, 1);
			replacementHelmet.setReplacementItems(chest, new ItemStack(replacementLeggings), new ItemStack(replacementBoots));

			dimensionReplacement.addReplacement(DimensionType.THE_NETHER, new ItemStack(Items.NETHER_STAR));
			dimensionReplacement.addReplacement(DimensionType.THE_END, new ItemStack(Items.ENDER_PEARL));
		}

		/**
		 * Gets an {@link Item.Properties} instance with the {@link ItemGroup} set to {@link TestMod3#ITEM_GROUP}.
		 *
		 * @return The item properties
		 */
		private static Item.Properties defaultItemProperties() {
			return new Item.Properties().group(TestMod3.ITEM_GROUP);
		}

		private static void registerItemVariantGroup(final IForgeRegistry<Item> registry, final IItemVariantGroup<?, ?> variantGroup) {
			variantGroup.registerItems(registry);
			ITEMS.addAll(variantGroup.getItems());
		}
	}
}
