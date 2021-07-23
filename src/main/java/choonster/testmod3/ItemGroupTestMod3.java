package choonster.testmod3;

import choonster.testmod3.util.SwordUpgrades;
import net.minecraft.item.Items;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemGroupTestMod3 extends ItemGroup {
	private final ItemStack sword;

	public ItemGroupTestMod3() {
		super(TestMod3.MODID);
		sword = SwordUpgrades.upgradeSword(Items.STONE_SWORD);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack makeIcon() {
		return sword;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void fillItemList(final NonNullList<ItemStack> items) {
		items.add(sword.copy());
		super.fillItemList(items);
	}
}
