package com.choonster.testmod3.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Identical to {@link ItemMultiTexture}, but has a constructor that makes it possible for {@link GameRegistry} to instantiate it.
 *
 * @author Choonster
 */
public class ItemMultiTextureMod extends ItemMultiTexture {

	/**
	 * Create an instance.
	 *
	 * @param block       The Block
	 * @param namesByMeta The name for each metadata value
	 * @param dummy       A dummy value so the String array isn't expanded in {@link GameRegistry#registerBlock}
	 */
	public ItemMultiTextureMod(Block block, String[] namesByMeta, Boolean dummy) {
		super(block, block, namesByMeta);
	}
}
