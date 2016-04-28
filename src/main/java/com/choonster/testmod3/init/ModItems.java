package com.choonster.testmod3.init;

import com.choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import com.choonster.testmod3.capability.pigspawner.PigSpawnerInfinite;
import com.choonster.testmod3.item.*;
import com.choonster.testmod3.util.Constants;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess,unused")
public class ModItems {
	public static final Set<Item> ITEMS = new HashSet<>();

	public static final ItemCuttingAxe WOODEN_AXE;
	public static final ItemEntityTest ENTITY_TEST;
	public static final ItemRecordMod RECORD_SOLARIS;
	public static final ItemHeavy HEAVY;
	public static final ItemEntityInteractionTest ENTITY_INTERACTION_TEST;
	public static final ItemBlockDestroyer BLOCK_DESTROYER;
	public static final ItemWithSubscripts SUBSCRIPTS;
	public static final ItemWithSuperscripts SUPERSCRIPTS;
	public static final ItemLastUseTimeModel MODEL_TEST;
	public static final ItemSnowballLauncher SNOWBALL_LAUNCHER;
	public static final ItemSlingshot SLINGSHOT;
	public static final ItemUnicodeTooltips UNICODE_TOOLTIPS;
	public static final ItemSwapTest SWAP_TEST_A;
	public static final ItemSwapTest SWAP_TEST_B;
	public static final ItemBlockDebugger BLOCK_DEBUGGER;
	public static final ItemHarvestSword HARVEST_SWORD_WOOD;
	public static final ItemHarvestSword HARVEST_SWORD_DIAMOND;
	public static final ItemClearer CLEARER;
	public static final ItemModBow BOW;
	public static final Item ARROW;
	public static final ItemHeightTester HEIGHT_TESTER;
	public static final ItemPigSpawner PIG_SPAWNER_FINITE;
	public static final ItemPigSpawner PIG_SPAWNER_INFINITE;
	public static final ItemContinuousBow CONTINUOUS_BOW;
	public static final ItemRespawner RESPAWNER;
	public static final ItemLootTableTest LOOT_TABLE_TEST;
	public static final ItemMaxHealthSetter MAX_HEALTH_SETTER;
	public static final ItemMaxHealthGetter MAX_HEALTH_GETTER;
	public static final ItemSoundEffect GUN;
	public static final ItemDimensionReplacement DIMENSION_REPLACEMENT;
	public static final ItemSoundEffect SADDLE;

	public static final ItemArmourReplacement REPLACEMENT_HELMET;
	public static final ItemArmourRestricted REPLACEMENT_CHESTPLATE;
	public static final ItemArmourRestricted REPACEMENT_LEGGINGS;
	public static final ItemArmourRestricted REPLACEMENT_BOOTS;

	public static final Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
	public static final ItemArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{2, 5, 4, 1}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN);

	static {
		WOODEN_AXE = registerItem(new ItemCuttingAxe(Item.ToolMaterial.WOOD, "woodenAxe"));
		ENTITY_TEST = registerItem(new ItemEntityTest());
		RECORD_SOLARIS = registerItem(new ItemRecordMod("solaris", ModSoundEvents.RECORD_SOLARIS));
		HEAVY = registerItem(new ItemHeavy());
		ENTITY_INTERACTION_TEST = registerItem(new ItemEntityInteractionTest());
		BLOCK_DESTROYER = registerItem(new ItemBlockDestroyer());
		SUBSCRIPTS = registerItem(new ItemWithSubscripts());
		SUPERSCRIPTS = registerItem(new ItemWithSuperscripts());
		MODEL_TEST = registerItem(new ItemLastUseTimeModel("modeltest"));
		SNOWBALL_LAUNCHER = registerItem(new ItemSnowballLauncher("snowballLauncher"));
		SLINGSHOT = registerItem(new ItemSlingshot());
		UNICODE_TOOLTIPS = registerItem(new ItemUnicodeTooltips());
		BLOCK_DEBUGGER = registerItem(new ItemBlockDebugger());
		HARVEST_SWORD_WOOD = registerItem(new ItemHarvestSword(Item.ToolMaterial.WOOD, "harvestSwordWood"));
		HARVEST_SWORD_DIAMOND = registerItem(new ItemHarvestSword(Item.ToolMaterial.DIAMOND, "harvestSwordDiamond"));
		CLEARER = registerItem(new ItemClearer());
		BOW = registerItem(new ItemModBow("bow"));
		ARROW = registerItem(new ItemModArrow("arrow"));
		HEIGHT_TESTER = registerItem(new ItemHeightTester());
		CONTINUOUS_BOW = registerItem(new ItemContinuousBow("continuousBow"));
		RESPAWNER = registerItem(new ItemRespawner());

		SWAP_TEST_A = registerItem(new ItemSwapTest("A"));
		SWAP_TEST_B = registerItem(new ItemSwapTest("B"));
		SWAP_TEST_A.setOtherItem(new ItemStack(SWAP_TEST_B));
		SWAP_TEST_B.setOtherItem(new ItemStack(SWAP_TEST_A));

		REPLACEMENT_HELMET = registerItem(new ItemArmourReplacement(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.HEAD, "Replacement"));
		REPLACEMENT_CHESTPLATE = registerItem(new ItemArmourRestricted(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.CHEST, "Replacement"));
		REPACEMENT_LEGGINGS = registerItem(new ItemArmourRestricted(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.LEGS, "Replacement"));
		REPLACEMENT_BOOTS = registerItem(new ItemArmourRestricted(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.FEET, "Replacement"));

		final ItemStack chest = new ItemStack(REPLACEMENT_CHESTPLATE);
		chest.addEnchantment(Enchantments.SHARPNESS, 1);
		REPLACEMENT_HELMET.setReplacementItems(chest, new ItemStack(REPACEMENT_LEGGINGS), new ItemStack(REPLACEMENT_BOOTS));

		PIG_SPAWNER_FINITE = registerItem(new ItemPigSpawner("finite", CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY::getDefaultInstance));
		PIG_SPAWNER_INFINITE = registerItem(new ItemPigSpawner("infinite", PigSpawnerInfinite::new));

		LOOT_TABLE_TEST = registerItem(new ItemLootTableTest());
		MAX_HEALTH_SETTER = registerItem(new ItemMaxHealthSetter());
		MAX_HEALTH_GETTER = registerItem(new ItemMaxHealthGetter());
		GUN = registerItem(new ItemSoundEffect("gun", ModSoundEvents.NINE_MM_FIRE));

		DIMENSION_REPLACEMENT = registerItem(new ItemDimensionReplacement("dimensionReplacement"));
		DIMENSION_REPLACEMENT.addReplacement(DimensionType.NETHER, new ItemStack(Items.NETHER_STAR));
		DIMENSION_REPLACEMENT.addReplacement(DimensionType.THE_END, new ItemStack(Items.ENDER_PEARL));

		SADDLE = registerItem(new ItemSoundEffect("saddle", ModSoundEvents.ACTION_SADDLE));

		TOOL_MATERIAL_GLOWSTONE.setRepairItem(new ItemStack(Items.GLOWSTONE_DUST));
		ARMOUR_MATERIAL_REPLACEMENT.customCraftingMaterial = ARROW;
	}

	public static void registerItems() {
		// Dummy method to make sure the static initialiser runs
	}

	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T>  The Item type
	 * @return The Item instance
	 */
	private static <T extends Item> T registerItem(T item) {
		GameRegistry.register(item);
		ITEMS.add(item);

		return item;
	}
}
