package com.choonster.testmod3.item.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import org.apache.commons.lang3.tuple.ImmutablePair;

/**
 * @author Choonster
 */
public class ItemSlabTestMod3<SLAB extends BlockSlab> extends ItemSlab {
	public ItemSlabTestMod3(Block block, ImmutablePair<SLAB, SLAB> slabs) {
		super(block, slabs.left, slabs.right);
	}
}
