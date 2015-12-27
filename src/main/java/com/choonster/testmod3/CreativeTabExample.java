package com.choonster.testmod3;

import com.choonster.testmod3.util.SwordUpgrades;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class CreativeTabExample extends CreativeTabs {
	public final ItemStack sword;

	public CreativeTabExample() {
		super("example");
		sword = SwordUpgrades.upgradeSword((ItemSword) Items.stone_sword);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem() {
		return Items.stone_sword;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayAllReleventItems(List<ItemStack> items) {
		items.add(sword.copy());
		super.displayAllReleventItems(items);
	}
}
