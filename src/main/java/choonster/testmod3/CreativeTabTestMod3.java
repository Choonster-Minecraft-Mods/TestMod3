package choonster.testmod3;

import choonster.testmod3.util.SwordUpgrades;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabTestMod3 extends CreativeTabs {
	private final ItemStack sword;

	public CreativeTabTestMod3() {
		super(TestMod3.MODID);
		sword = SwordUpgrades.upgradeSword(Items.STONE_SWORD);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem() {
		return sword;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> items) {
		items.add(sword.copy());
		super.displayAllRelevantItems(items);
	}
}
