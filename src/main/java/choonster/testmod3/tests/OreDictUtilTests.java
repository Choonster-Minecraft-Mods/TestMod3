package choonster.testmod3.tests;

import choonster.testmod3.util.OreDictUtils;
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
		final boolean isRegistered = OreDictUtils.INSTANCE.isItemStackRegisteredForName(itemStack, oreName);
		assertTrue(isRegistered, String.format("%s should be registered as %s", itemStack.getDisplayName(), oreName));
	}

	private void assertNotRegistered(ItemStack itemStack, String oreName) {
		final boolean isRegistered = OreDictUtils.INSTANCE.isItemStackRegisteredForName(itemStack, oreName);
		assertFalse(isRegistered, String.format("%s should not be registered as %s", itemStack.getDisplayName(), oreName));
	}

	@Override
	protected void runTest() {
		final ItemStack oakLog = new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.getMetadata());
		assertRegistered(oakLog, "logWood");
		assertNotRegistered(oakLog, "blahblahblah");

		final ItemStack jungleLog = new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.JUNGLE.getMetadata());
		assertRegistered(jungleLog, "logWood");

		final ItemStack ironIngot = new ItemStack(Items.IRON_INGOT);
		assertRegistered(ironIngot, "ingotIron");

		final ItemStack stone = new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.STONE.getMetadata());
		assertRegistered(stone, "stone");

		final ItemStack andesite = new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.getMetadata());
		assertNotRegistered(andesite, "stone");

		final ItemStack record13 = new ItemStack(Items.RECORD_13);
		assertRegistered(record13, "record");
	}
}
