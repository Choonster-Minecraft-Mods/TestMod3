package com.choonster.testmod3.init;

import com.choonster.testmod3.TestMod3;
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

	public static Item woodenAxe;
	public static Item entityTest;
	public static Item solarisRecord;
	public static Item heavy;
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

	public static Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE;

	public static void registerItems() {
		woodenAxe = registerItem(new ToolWoodAxe(Item.ToolMaterial.WOOD).setCreativeTab(TestMod3.creativeTab).setUnlocalizedName("woodenAxe"));
		entityTest = registerItem(new ItemEntityTest());
		solarisRecord = registerItem(new ItemRecordSolaris(), "solaris_record");
		heavy = registerItem(new ItemHeavy());
		bucket = registerItem(new ItemBucketTestMod3(), "bucket");
		entityInteractionTest = registerItem(new ItemEntityInteractionTest());
		blockDestroyer = registerItem(new ItemBlockDestroyer());
		subscripts = registerItem(new ItemWithSubscripts());
		superscripts = registerItem(new ItemWithSuperscripts());
		modelTest = registerItem(new ItemModelTest());
		snowballLauncher = registerItem(new ItemSnowballLauncher());
		slingshot = registerItem(new ItemSlingshot());
		unicodeTooltips = registerItem(new ItemUnicodeTooltips());
		blockDebugger = registerItem(new ItemBlockDebugger());
		woodenHarvestSword = (ItemHarvestSword) registerItem(new ItemHarvestSword(Item.ToolMaterial.WOOD).setUnlocalizedName("harvestSwordWood"));
		diamondHarvestSword = (ItemHarvestSword) registerItem(new ItemHarvestSword(Item.ToolMaterial.EMERALD).setUnlocalizedName("harvestSwordDiamond"));
		clearer = registerItem(new ItemClearer());

		swapTestA = registerItem(new ItemSwapTest("A"));
		swapTestB = registerItem(new ItemSwapTest("B"));
		swapTestA.setOtherItem(new ItemStack(swapTestB));
		swapTestB.setOtherItem(new ItemStack(swapTestA));

		TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10).setRepairItem(new ItemStack(Items.glowstone_dust));
	}

	/**
	 * Register an Item with the default name.
	 *
	 * @param item The Item instance
	 * @param <T>  The Item type
	 * @return The Item instance
	 */
	private static <T extends Item> T registerItem(T item) {
		return registerItem(item, item.getUnlocalizedName().replaceFirst("item.", ""));
	}

	/**
	 * Register an Item with a custom name.
	 *
	 * @param item The Item instance
	 * @param <T>  The Item type
	 * @return The Item instance
	 */
	private static <T extends Item> T registerItem(T item, String name) {
		GameRegistry.registerItem(item, name);
		items.add(item);
		return item;
	}
}
