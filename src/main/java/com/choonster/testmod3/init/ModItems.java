package com.choonster.testmod3.init;

import com.choonster.testmod3.item.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

public class ModItems {
	public static final Set<Item> items = new HashSet<>();

	public static ItemCuttingAxe woodenAxe;
	public static ItemEntityTest entityTest;
	public static ItemRecordMod solarisRecord;
	public static ItemHeavy heavy;
	public static ItemBucketTestMod3 bucket;
	public static ItemEntityInteractionTest entityInteractionTest;
	public static ItemBlockDestroyer blockDestroyer;
	public static ItemWithSubscripts subscripts;
	public static ItemWithSuperscripts superscripts;
	public static ItemModelTest modelTest;
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

	public static Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE;

	public static void registerItems() {
		woodenAxe = registerItem(new ItemCuttingAxe(Item.ToolMaterial.WOOD, "woodenAxe"));
		entityTest = registerItem(new ItemEntityTest());
		solarisRecord = registerItem(new ItemRecordMod("solaris"));
		heavy = registerItem(new ItemHeavy());
		bucket = registerItem(new ItemBucketTestMod3());
		entityInteractionTest = registerItem(new ItemEntityInteractionTest());
		blockDestroyer = registerItem(new ItemBlockDestroyer());
		subscripts = registerItem(new ItemWithSubscripts());
		superscripts = registerItem(new ItemWithSuperscripts());
		modelTest = registerItem(new ItemModelTest());
		snowballLauncher = registerItem(new ItemSnowballLauncher());
		slingshot = registerItem(new ItemSlingshot());
		unicodeTooltips = registerItem(new ItemUnicodeTooltips());
		blockDebugger = registerItem(new ItemBlockDebugger());
		woodenHarvestSword = registerItem(new ItemHarvestSword(Item.ToolMaterial.WOOD, "harvestSwordWood"));
		diamondHarvestSword = registerItem(new ItemHarvestSword(Item.ToolMaterial.EMERALD, "harvestSwordDiamond"));
		clearer = registerItem(new ItemClearer());
		modBow = registerItem(new ItemModBow());
		modArrow = registerItem(new ItemTestMod3("arrow"));
		heightTester = registerItem(new ItemHeightTester());

		swapTestA = registerItem(new ItemSwapTest("A"));
		swapTestB = registerItem(new ItemSwapTest("B"));
		swapTestA.setOtherItem(new ItemStack(swapTestB));
		swapTestB.setOtherItem(new ItemStack(swapTestA));

		TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10).setRepairItem(new ItemStack(Items.glowstone_dust));
	}

	/**
	 * Register an Item
	 *
	 * @param item The Item instance
	 * @param <T>  The Item type
	 * @return The Item instance
	 */
	private static <T extends Item> T registerItem(T item) {
		GameRegistry.registerItem(item);
		items.add(item);

		return item;
	}
}
