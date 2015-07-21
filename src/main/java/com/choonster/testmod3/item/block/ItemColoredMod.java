package com.choonster.testmod3.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemColored;

/**
 * Identical to the vanilla class ItemColored but uses a Boolean (object) parameter for the constructor instead of
 * a boolean (primitive) parameter, allowing GameRegistry to correctly instantiate it via reflection.
 */
public class ItemColoredMod extends ItemColored {
	public ItemColoredMod(Block block, Boolean hasSubtypes) {
		super(block, hasSubtypes);
	}
}