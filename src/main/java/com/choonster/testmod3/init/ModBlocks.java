package com.choonster.testmod3.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {


	public static void registerBlocks() {

	}

	// Register a Block with the default ItemBlock class
	private static <T extends Block> T registerBlock(T block) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));
		return block;
	}

	// Register a Block with a custom ItemBlock class
	private static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass) {
		GameRegistry.registerBlock(block, itemClass, block.getUnlocalizedName().replace("tile.", ""));
		return block;
	}
}
