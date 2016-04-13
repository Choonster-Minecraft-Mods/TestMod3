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
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public class ModItems {
	public static final Set<Item> items = new HashSet<>();

	public static ItemCuttingAxe woodenAxe;
	public static ItemEntityTest entityTest;
	public static ItemRecordMod solarisRecord;
	public static ItemHeavy heavy;
	public static ItemEntityInteractionTest entityInteractionTest;
	public static ItemBlockDestroyer blockDestroyer;
	public static ItemWithSubscripts subscripts;
	public static ItemWithSuperscripts superscripts;
	public static ItemLastUseTimeModel modelTest;
	public static ItemSnowballLauncher snowballLauncher;
	public static ItemSlingshot slingshot;
	public static ItemUnicodeTooltips unicodeTooltips;
	public static ItemSwapTest swapTestA;
	public static ItemSwapTest swapTestB;
	public static ItemBlockDebugger blockDebugger;
	public static ItemHarvestSword woodenHarvestSword;
	public static ItemHarvestSword diamondHarvestSword;
	public static ItemClearer clearer;
	public static ItemModBow modBow;
	public static Item modArrow;
	public static ItemHeightTester heightTester;
	public static ItemPigSpawner pigSpawnerFinite;
	public static ItemPigSpawner pigSpawnerInfinite;
	public static ItemContinuousBow continuousBow;
	public static ItemRespawner respawner;
	public static ItemLootTableTest lootTableTest;
	public static ItemMaxHealthSetter maxHealthSetter;
	public static ItemMaxHealthGetter maxHealthGetter;

	public static ItemArmourReplacement replacementHelmet;
	public static ItemArmourRestricted replacementChestplate;
	public static ItemArmourRestricted repacementLeggings;
	public static ItemArmourRestricted replacementBoots;

	public static final Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
	public static final ItemArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{2, 5, 4, 1}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN);

	public static void registerItems() {
		woodenAxe = registerItem(new ItemCuttingAxe(Item.ToolMaterial.WOOD, "woodenAxe"));
		entityTest = registerItem(new ItemEntityTest());
		solarisRecord = registerItem(new ItemRecordMod("solaris", ModSoundEvents.record_solaris));
		heavy = registerItem(new ItemHeavy());
		entityInteractionTest = registerItem(new ItemEntityInteractionTest());
		blockDestroyer = registerItem(new ItemBlockDestroyer());
		subscripts = registerItem(new ItemWithSubscripts());
		superscripts = registerItem(new ItemWithSuperscripts());
		modelTest = registerItem(new ItemLastUseTimeModel("modeltest"));
		snowballLauncher = registerItem(new ItemSnowballLauncher("snowballLauncher"));
		slingshot = registerItem(new ItemSlingshot());
		unicodeTooltips = registerItem(new ItemUnicodeTooltips());
		blockDebugger = registerItem(new ItemBlockDebugger());
		woodenHarvestSword = registerItem(new ItemHarvestSword(Item.ToolMaterial.WOOD, "harvestSwordWood"));
		diamondHarvestSword = registerItem(new ItemHarvestSword(Item.ToolMaterial.DIAMOND, "harvestSwordDiamond"));
		clearer = registerItem(new ItemClearer());
		modBow = registerItem(new ItemModBow("bow"));
		modArrow = registerItem(new ItemModArrow("arrow"));
		heightTester = registerItem(new ItemHeightTester());
		continuousBow = registerItem(new ItemContinuousBow("continuousBow"));
		respawner = registerItem(new ItemRespawner());

		swapTestA = registerItem(new ItemSwapTest("A"));
		swapTestB = registerItem(new ItemSwapTest("B"));
		swapTestA.setOtherItem(new ItemStack(swapTestB));
		swapTestB.setOtherItem(new ItemStack(swapTestA));

		replacementHelmet = registerItem(new ItemArmourReplacement(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.HEAD, "Replacement"));
		replacementChestplate = registerItem(new ItemArmourRestricted(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.CHEST, "Replacement"));
		repacementLeggings = registerItem(new ItemArmourRestricted(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.LEGS, "Replacement"));
		replacementBoots = registerItem(new ItemArmourRestricted(ARMOUR_MATERIAL_REPLACEMENT, EntityEquipmentSlot.FEET, "Replacement"));

		ItemStack chest = new ItemStack(replacementChestplate);
		chest.addEnchantment(Enchantments.SHARPNESS, 1);
		replacementHelmet.setReplacementItems(chest, new ItemStack(repacementLeggings), new ItemStack(replacementBoots));

		pigSpawnerFinite = registerItem(new ItemPigSpawner("finite", CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY::getDefaultInstance));
		pigSpawnerInfinite = registerItem(new ItemPigSpawner("infinite", PigSpawnerInfinite::new));

		lootTableTest = registerItem(new ItemLootTableTest());
		maxHealthSetter = registerItem(new ItemMaxHealthSetter());
		maxHealthGetter = registerItem(new ItemMaxHealthGetter());

		TOOL_MATERIAL_GLOWSTONE.setRepairItem(new ItemStack(Items.GLOWSTONE_DUST));
		ARMOUR_MATERIAL_REPLACEMENT.customCraftingMaterial = modArrow;
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
		items.add(item);

		return item;
	}
}
