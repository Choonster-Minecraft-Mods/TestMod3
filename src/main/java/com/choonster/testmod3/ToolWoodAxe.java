package com.choonster.testmod3;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

public class ToolWoodAxe extends ItemAxe {

	public ToolWoodAxe(ToolMaterial material) {
		super(material);
		this.setMaxDamage(59);
		this.maxStackSize = 1;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack) {
		return stack.getItemDamage() < 59;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		if (stack.attemptDamageItem(1, itemRand)) {
			return null;
		}

		return stack;
	}
}
