package com.choonster.testmod3.init;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.item.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
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

	public static Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE;

	public static void registerItems() {
		woodenAxe = new ToolWoodAxe(Item.ToolMaterial.WOOD).setCreativeTab(TestMod3.creativeTab).setUnlocalizedName("woodenAxe");
		GameRegistry.registerItem(woodenAxe, "wooden_axe_test");

		entityTest = new ItemEntityTest();
		GameRegistry.registerItem(entityTest, "entity_test");

		solarisRecord = new ItemRecordSolaris();
		GameRegistry.registerItem(solarisRecord, "solaris_record");

		heavy = new ItemHeavy();
		GameRegistry.registerItem(heavy, "heavy");

		bucket = registerItem(new ItemBucketTestMod3());

		entityInteractionTest = registerItem(new ItemEntityInteractionTest());

		blockDestroyer = registerItem(new ItemBlockDestroyer());

		subscripts = registerItem(new ItemWithSubscripts());
		superscripts = registerItem(new ItemWithSuperscripts());

		modelTest = registerItem(new ItemModelTest());

		snowballLauncher = registerItem(new ItemSnowballLauncher());

		slingshot = registerItem(new ItemSlingshot());

		unicodeTooltips = registerItem(new ItemUnicodeTooltips());

		swapTestA = registerItem(new ItemSwapTest("A"));
		swapTestB = registerItem(new ItemSwapTest("B"));
		swapTestA.setOtherItem(new ItemStack(swapTestB));
		swapTestB.setOtherItem(new ItemStack(swapTestA));

		blockDebugger = registerItem(new ItemBlockDebugger());

		TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10).setRepairItem(new ItemStack(Items.glowstone_dust));
	}

	private static <T extends Item> T registerItem(T item) {
		GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));
		return item;
	}
}
