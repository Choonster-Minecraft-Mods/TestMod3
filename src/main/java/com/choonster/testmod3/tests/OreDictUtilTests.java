package com.choonster.testmod3.tests;

import com.choonster.testmod3.util.OreDictUtils;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Tests for {@link OreDictUtils}.
 *
 * @author Choonster
 */
public class OreDictUtilTests extends Test {
	public static final OreDictUtilTests INSTANCE = new OreDictUtilTests();

	private void assertRegistered(ItemStack itemStack, String oreName) {
		boolean isRegistered = OreDictUtils.INSTANCE.isItemStackRegisteredForName(itemStack, oreName);
		assertTrue(isRegistered, String.format("%s should be registered as %s", itemStack.getDisplayName(), oreName));
	}

	private void assertNotRegistered(ItemStack itemStack, String oreName) {
		boolean isRegistered = OreDictUtils.INSTANCE.isItemStackRegisteredForName(itemStack, oreName);
		assertFalse(isRegistered, String.format("%s should not be registered as %s", itemStack.getDisplayName(), oreName));
	}

	@Override
	protected void runTest() {
		ItemStack oakLog = new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.OAK.getMetadata());
		assertRegistered(oakLog, "logWood");
		assertNotRegistered(oakLog, "blahblahblah");

		ItemStack jungleLog = new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.JUNGLE.getMetadata());
		assertRegistered(jungleLog, "logWood");

		ItemStack ironIngot = new ItemStack(Items.iron_ingot);
		assertRegistered(ironIngot, "ingotIron");

		ItemStack stone = new ItemStack(Blocks.stone, 1, BlockStone.EnumType.STONE.getMetadata());
		assertRegistered(stone, "stone");

		ItemStack andesite = new ItemStack(Blocks.stone, 1, BlockStone.EnumType.ANDESITE.getMetadata());
		assertNotRegistered(andesite, "stone");

		ItemStack record13 = new ItemStack(Items.record_13);
		assertRegistered(record13, "record");
	}
}
