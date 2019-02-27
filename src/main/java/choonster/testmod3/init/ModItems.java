package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import choonster.testmod3.capability.pigspawner.PigSpawnerInfinite;
import choonster.testmod3.entity.EntityBlockDetectionArrow;
import choonster.testmod3.entity.EntityModArrow;
import choonster.testmod3.item.*;
import choonster.testmod3.item.variantgroup.IItemVariantGroup;
import choonster.testmod3.item.variantgroup.ItemVariantGroup;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
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
	public static final ItemCuttingAxe WOODEN_AXE = Null();

	public static final ItemEntityTest ENTITY_TEST = Null();

	public static final ItemRecordMod RECORD_SOLARIS = Null();

	public static final ItemHeavy HEAVY = Null();

	public static final ItemEntityInteractionTest ENTITY_INTERACTION_TEST = Null();

	public static final ItemBlockDestroyer BLOCK_DESTROYER = Null();

	public static final ItemWithSubscripts SUBSCRIPTS = Null();

	public static final ItemWithSuperscripts SUPERSCRIPTS = Null();

	public static final ItemLastUseTimeModel MODEL_TEST = Null();

	public static final ItemSnowballLauncher SNOWBALL_LAUNCHER = Null();

	public static final ItemSlingshot SLINGSHOT = Null();

	public static final ItemUnicodeTooltips UNICODE_TOOLTIPS = Null();

	public static final ItemSwapTest SWAP_TEST_A = Null();

	public static final ItemSwapTest SWAP_TEST_B = Null();

	public static final ItemBlockDebugger BLOCK_DEBUGGER = Null();

	public static final ItemHarvestSword WOODEN_HARVEST_SWORD = Null();

	public static final ItemHarvestSword DIAMOND_HARVEST_SWORD = Null();

	public static final ItemClearer CLEARER = Null();

	public static final ItemModBow BOW = Null();

	public static final Item ARROW = Null();

	public static final ItemHeightTester HEIGHT_TESTER = Null();

	public static final ItemPigSpawner PIG_SPAWNER_FINITE = Null();

	public static final ItemPigSpawner PIG_SPAWNER_INFINITE = Null();

	public static final ItemContinuousBow CONTINUOUS_BOW = Null();

	public static final ItemRespawner RESPAWNER = Null();

	public static final ItemLootTableTest LOOT_TABLE_TEST = Null();

	public static final ItemMaxHealthSetter MAX_HEALTH_SETTER_ITEM = Null();

	public static final ItemMaxHealthGetter MAX_HEALTH_GETTER_ITEM = Null();

	public static final ItemSoundEffect GUN = Null();

	public static final ItemDimensionReplacement DIMENSION_REPLACEMENT = Null();

	public static final ItemSoundEffect SADDLE = Null();

	public static final ItemSlowSword WOODEN_SLOW_SWORD = Null();

	public static final ItemSlowSword DIAMOND_SLOW_SWORD = Null();

	public static final ItemVariants VARIANTS_ITEM = Null();

	public static final ItemRitualChecker RITUAL_CHECKER = Null();

	public static final ItemHiddenBlockRevealer HIDDEN_BLOCK_REVEALER = Null();

	public static final Item NO_MOD_NAME = Null();

	public static final ItemKey KEY = Null();

	public static final ItemModArrow BLOCK_DETECTION_ARROW = Null();

	public static final Item TRANSLUCENT_ITEM = Null();

	public static final ItemEntityKiller ENTITY_KILLER = Null();

	public static final ItemChunkEnergySetter CHUNK_ENERGY_SETTER = Null();

	public static final ItemChunkEnergyGetter CHUNK_ENERGY_GETTER = Null();

	public static final Item CHUNK_ENERGY_DISPLAY = Null();

	public static final Item BEACON_ITEM = Null();

	public static final ItemArmourPotionEffect SATURATION_HELMET = Null();

	public static final ItemEntityChecker ENTITY_CHECKER = Null();

	public static final Item RUBBER = Null();


	public static final ItemArmourReplacement REPLACEMENT_HELMET = Null();

	public static final ItemArmourRestricted REPLACEMENT_CHESTPLATE = Null();

	public static final ItemArmourRestricted REPLACEMENT_LEGGINGS = Null();

	public static final ItemArmourRestricted REPLACEMENT_BOOTS = Null();


	public static final ItemBucketTestMod3 WOODEN_BUCKET = Null();

	public static final ItemBucketTestMod3 STONE_BUCKET = Null();

	public static class VariantGroups {
		public static final ItemVariantGroup<ItemVariants.EnumType, ItemVariants> VARIANTS_ITEMS = ItemVariantGroup.Builder.<ItemVariants.EnumType, ItemVariants>create()
				.groupName("variants_item")
				.suffix()
				.variants(ItemVariants.EnumType.values())
				.itemFactory(ItemVariants::new)
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
			final ItemSwapTest swapTestA = new ItemSwapTest(defaultItemProperties());
			final ItemSwapTest swapTestB = new ItemSwapTest(defaultItemProperties());

			final ItemDimensionReplacement dimensionReplacement = new ItemDimensionReplacement(defaultItemProperties());

			final ItemArmourReplacement replacementHelmet = new ItemArmourReplacement(ModArmourMaterial.REPLACEMENT, EntityEquipmentSlot.HEAD, defaultItemProperties());
			final ItemArmourRestricted replacementChestplate = new ItemArmourRestricted(ModArmourMaterial.REPLACEMENT, EntityEquipmentSlot.CHEST, defaultItemProperties());
			final ItemArmourRestricted replacementLeggings = new ItemArmourRestricted(ModArmourMaterial.REPLACEMENT, EntityEquipmentSlot.LEGS, defaultItemProperties());
			final ItemArmourRestricted replacementBoots = new ItemArmourRestricted(ModArmourMaterial.REPLACEMENT, EntityEquipmentSlot.FEET, defaultItemProperties());

			final Item[] items = {
					new ItemCuttingAxe(ItemTier.WOOD, 6.0f, -3.2f, defaultItemProperties()).setRegistryName("wooden_axe"),
					new ItemEntityTest(defaultItemProperties()).setRegistryName("entity_test"),
					new ItemRecordMod(13, ModSoundEvents.RegistrationHandler.getSoundEvent(new ResourceLocation(TestMod3.MODID, "record.solaris")), defaultItemProperties()).setRegistryName("record_solaris"),
					new ItemHeavy(defaultItemProperties()).setRegistryName("heavy"),
					new ItemEntityInteractionTest(defaultItemProperties()).setRegistryName("entity_interaction_test"),
					new ItemBlockDestroyer(defaultItemProperties()).setRegistryName("block_destroyer"),
					new ItemWithSubscripts(defaultItemProperties()).setRegistryName("subscripts"),
					new ItemWithSuperscripts(defaultItemProperties()).setRegistryName("superscripts"),
					new ItemLastUseTimeModel(defaultItemProperties()).setRegistryName("model_test"),
					new ItemSnowballLauncher(defaultItemProperties()).setRegistryName("snowball_launcher"),
					new ItemSlingshot(defaultItemProperties()).setRegistryName("slingshot"),
					new ItemUnicodeTooltips(defaultItemProperties()).setRegistryName("unicode_tooltips"),
					swapTestA.setRegistryName("swap_test_a"),
					swapTestB.setRegistryName("swap_test_b"),
					new ItemBlockDebugger(defaultItemProperties()).setRegistryName("block_debugger"),
					new ItemHarvestSword(ItemTier.WOOD, ItemHarvestSword.addToolTypes(ItemTier.WOOD, defaultItemProperties())).setRegistryName("wooden_harvest_sword"),
					new ItemHarvestSword(ItemTier.DIAMOND, ItemHarvestSword.addToolTypes(ItemTier.DIAMOND, defaultItemProperties())).setRegistryName("diamond_harvest_sword"),
					new ItemClearer(defaultItemProperties()).setRegistryName("clearer"),
					new ItemModBow(defaultItemProperties().defaultMaxDamage(384).maxStackSize(1)).setRegistryName("bow"),
					new ItemModArrow(EntityModArrow::new, defaultItemProperties()).setRegistryName("arrow"),
					new ItemHeightTester(defaultItemProperties()).setRegistryName("height_tester"),
					new ItemPigSpawner(CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY::getDefaultInstance, defaultItemProperties()).setRegistryName("pig_spawner_finite"),
					new ItemPigSpawner(PigSpawnerInfinite::new, defaultItemProperties()).setRegistryName("pig_spawner_infinite"),
					new ItemContinuousBow(defaultItemProperties()).setRegistryName("continuous_bow"),
					new ItemRespawner(defaultItemProperties()).setRegistryName("respawner"),
					new ItemLootTableTest(defaultItemProperties()).setRegistryName("loot_table_test"),
					new ItemMaxHealthSetter(defaultItemProperties()).setRegistryName("max_health_setter_item"),
					new ItemMaxHealthGetter(defaultItemProperties()).setRegistryName("max_health_getter_item"),
					new ItemSoundEffect(() -> ModSoundEvents.NINE_MM_FIRE, defaultItemProperties()).setRegistryName("gun"),
					dimensionReplacement.setRegistryName("dimension_replacement"),
					new ItemSoundEffect(() -> ModSoundEvents.ACTION_SADDLE, defaultItemProperties()).setRegistryName("saddle"),
					new ItemSlowSword(ItemTier.WOOD, defaultItemProperties()).setRegistryName("wooden_slow_sword"),
					new ItemSlowSword(ItemTier.DIAMOND, defaultItemProperties()).setRegistryName("diamond_slow_sword"),
					new ItemRitualChecker(defaultItemProperties()).setRegistryName("ritual_checker"),
					new ItemHiddenBlockRevealer(defaultItemProperties()).setRegistryName("hidden_block_revealer"),
					new Item(defaultItemProperties()).setRegistryName("no_mod_name"),
					new ItemKey(defaultItemProperties()).setRegistryName("key"),
					new ItemModArrow(EntityBlockDetectionArrow::new, defaultItemProperties()).setRegistryName("block_detection_arrow"),
					new Item(defaultItemProperties()).setRegistryName("translucent_item"),
					new ItemEntityKiller(defaultItemProperties()).setRegistryName("entity_killer"),
					new ItemChunkEnergySetter(defaultItemProperties()).setRegistryName("chunk_energy_setter"),
					new ItemChunkEnergyGetter(defaultItemProperties()).setRegistryName("chunk_energy_getter"),
					new Item(defaultItemProperties()).setRegistryName("chunk_energy_display"),
					new Item(defaultItemProperties()).setRegistryName("beacon_item"),
					new ItemArmourPotionEffect(ArmorMaterial.CHAIN, EntityEquipmentSlot.HEAD, new PotionEffect(MobEffects.SATURATION, 1, 0, true, false), defaultItemProperties()).setRegistryName("saturation_helmet"),
					new ItemEntityChecker(defaultItemProperties()).setRegistryName("entity_checker"),
					new Item(defaultItemProperties()).setRegistryName("rubber"),

					replacementHelmet.setRegistryName("replacement_helmet"),
					replacementChestplate.setRegistryName("replacement_chestplate"),
					replacementLeggings.setRegistryName("replacement_leggings"),
					replacementBoots.setRegistryName("replacement_boots"),

					new ItemBucketTestMod3(defaultItemProperties()).setRegistryName("wooden_bucket"),
					new ItemBucketTestMod3(defaultItemProperties()).setRegistryName("stone_bucket"),
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

			dimensionReplacement.addReplacement(DimensionType.NETHER, new ItemStack(Items.NETHER_STAR));
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
