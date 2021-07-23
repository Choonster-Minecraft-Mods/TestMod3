package choonster.testmod3;

import choonster.testmod3.util.SwordUpgrades;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TestMod3CreativeModeTab extends CreativeModeTab {
	private final ItemStack sword;

	public TestMod3CreativeModeTab() {
		super(TestMod3.MODID);
		sword = SwordUpgrades.upgradeSword(Items.STONE_SWORD);
	}

	@Override
	public ItemStack makeIcon() {
		return sword;
	}

	@Override
	public void fillItemList(final NonNullList<ItemStack> items) {
		items.add(sword.copy());
		super.fillItemList(items);
	}
}
