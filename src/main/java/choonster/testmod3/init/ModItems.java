package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import choonster.testmod3.capability.pigspawner.PigSpawnerInfinite;
import choonster.testmod3.entity.EntityBlockDetectionArrow;
import choonster.testmod3.entity.EntityModArrow;
import choonster.testmod3.item.*;
import choonster.testmod3.item.variantgroup.IItemVariantGroup;
import choonster.testmod3.item.variantgroup.ItemVariantGroup;
import choonster.testmod3.util.Constants;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

import static choonster.testmod3.util.InjectionUtil.Null;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModItems {
	public static class ArmorMaterials {
		public static final ItemArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, (float) 0);
	}

	public static class ToolMaterials {
		public static final Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
	}

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

	public static final ItemTestMod3 NO_MOD_NAME = Null();

	public static final ItemKey KEY = Null();

	public static final ItemModArrow BLOCK_DETECTION_ARROW = Null();

	public static final ItemTestMod3 TRANSLUCENT_ITEM = Null();

	public static final ItemEntityKiller ENTITY_KILLER = Null();

	public static final ItemChunkEnergySetter CHUNK_ENERGY_SETTER = Null();

	public static final ItemChunkEnergyGetter CHUNK_ENERGY_GETTER = Null();

	public static final ItemTestMod3 CHUNK_ENERGY_DISPLAY = Null();

	public static final ItemTestMod3 BEACON_ITEM = Null();

	public static final ItemArmourPotionEffect SATURATION_HELMET = Null();

	public static final ItemEntityChecker ENTITY_CHECKER = Null();

	public static final ItemTestMod3 RUBBER = Null();


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

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RegistrationHandler {
		public static final Set<Item> ITEMS = new HashSet<>();

		/**
		 * Register this mod's {@link Item}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final ItemSwapTest swapTestA = new ItemSwapTest("a");
			final ItemSwapTest swapTestB = new ItemSwapTest("b");

			final ItemDimensionReplacement dimensionReplacement = new ItemDimensionReplacement("dimension_replacement");

			final ItemArmourReplacement replacementHelmet = new ItemArmourReplacement(ArmorMaterials.ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.HEAD, "replacement");
			final ItemArmourRestricted replacementChestplate = new ItemArmourRestricted(ArmorMaterials.ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.CHEST, "replacement");
			final ItemArmourRestricted replacementLeggings = new ItemArmourRestricted(ArmorMaterials.ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.LEGS, "replacement");
			final ItemArmourRestricted replacementBoots = new ItemArmourRestricted(ArmorMaterials.ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.FEET, "replacement");

			final Item[] items = {
					new ItemCuttingAxe(Item.ToolMaterial.WOOD, "wooden_axe"),
					new ItemEntityTest(),
					new ItemRecordMod("solaris", ModSoundEvents.RECORD_SOLARIS),
					new ItemHeavy(),
					new ItemEntityInteractionTest(),
					new ItemBlockDestroyer(),
					new ItemWithSubscripts(),
					new ItemWithSuperscripts(),
					new ItemLastUseTimeModel("model_test"),
					new ItemSnowballLauncher("snowball_launcher"),
					new ItemSlingshot(),
					new ItemUnicodeTooltips(),
					swapTestA,
					swapTestB,
					new ItemBlockDebugger(),
					new ItemHarvestSword(Item.ToolMaterial.WOOD, "wooden_harvest_sword"),
					new ItemHarvestSword(Item.ToolMaterial.DIAMOND, "diamond_harvest_sword"),
					new ItemClearer(),
					new ItemModBow("bow"),
					new ItemModArrow("arrow", EntityModArrow::new),
					new ItemHeightTester(),
					new ItemPigSpawner("finite", CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY::getDefaultInstance),
					new ItemPigSpawner("infinite", PigSpawnerInfinite::new),
					new ItemContinuousBow("continuous_bow"),
					new ItemRespawner(),
					new ItemLootTableTest(),
					new ItemMaxHealthSetter(),
					new ItemMaxHealthGetter(),
					new ItemSoundEffect("gun", ModSoundEvents.NINE_MM_FIRE),
					dimensionReplacement,
					new ItemSoundEffect("saddle", ModSoundEvents.ACTION_SADDLE),
					new ItemSlowSword(Item.ToolMaterial.WOOD, "wooden_slow_sword"),
					new ItemSlowSword(Item.ToolMaterial.DIAMOND, "diamond_slow_sword"),
					new ItemRitualChecker(),
					new ItemHiddenBlockRevealer(),
					new ItemTestMod3("no_mod_name"),
					new ItemKey(),
					new ItemModArrow("block_detection_arrow", EntityBlockDetectionArrow::new),
					new ItemTestMod3("translucent_item"),
					new ItemEntityKiller(),
					new ItemChunkEnergySetter(),
					new ItemChunkEnergyGetter(),
					new ItemTestMod3("chunk_energy_display"),
					new ItemTestMod3("beacon_item"),
					new ItemArmourPotionEffect(ItemArmor.ArmorMaterial.CHAIN, EntityEquipmentSlot.HEAD, "saturation", new PotionEffect(MobEffects.SATURATION, 1, 0, true, false)),
					new ItemEntityChecker(),
					new ItemTestMod3("rubber"),

					replacementHelmet,
					replacementChestplate,
					replacementLeggings,
					replacementBoots,

					new ItemBucketTestMod3("wooden_bucket"),
					new ItemBucketTestMod3("stone_bucket"),
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final Item item : items) {
				registry.register(item);
				ITEMS.add(item);
			}

			registerItemVariantGroup(registry, VariantGroups.VARIANTS_ITEMS);

			ToolMaterials.TOOL_MATERIAL_GLOWSTONE.setRepairItem(new ItemStack(Items.GLOWSTONE_DUST));
			ArmorMaterials.ARMOUR_MATERIAL_REPLACEMENT.setRepairItem(new ItemStack(ARROW));

			swapTestA.setOtherItem(new ItemStack(swapTestB));
			swapTestB.setOtherItem(new ItemStack(swapTestA));

			final ItemStack chest = new ItemStack(replacementChestplate);
			chest.addEnchantment(Enchantments.SHARPNESS, 1);
			replacementHelmet.setReplacementItems(chest, new ItemStack(replacementLeggings), new ItemStack(replacementBoots));

			dimensionReplacement.addReplacement(DimensionType.NETHER, new ItemStack(Items.NETHER_STAR));
			dimensionReplacement.addReplacement(DimensionType.THE_END, new ItemStack(Items.ENDER_PEARL));
		}

		private static void registerItemVariantGroup(final IForgeRegistry<Item> registry, final IItemVariantGroup<?, ?> variantGroup) {
			variantGroup.registerItems(registry);
			ITEMS.addAll(variantGroup.getItems());
		}
	}
}
