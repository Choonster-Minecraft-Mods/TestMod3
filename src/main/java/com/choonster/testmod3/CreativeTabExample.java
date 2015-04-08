package com.choonster.testmod3;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

import java.util.List;

public class CreativeTabExample extends CreativeTabs {
	public CreativeTabExample() {
		super("example");
	}

	@Override
	public Item getTabIconItem() {
		return Items.stone_sword;
	}

	@Override
	public void displayAllReleventItems(List items) {
		items.add(SwordUpgrades.upgradeSword((ItemSword) Items.stone_sword));
		super.displayAllReleventItems(items);
	}
}
