package com.choonster.testmod3.init;

import com.choonster.testmod3.block.*;
import com.choonster.testmod3.block.pipe.BlockPipeBasic;
import com.choonster.testmod3.item.block.ItemColoredMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockWaterGrass waterGrass;
	public static BlockLargeCollisionTest largeCollisionTest;
	public static BlockRightClickTest rightClickTest;
	public static BlockClientPlayerRightClick clientPlayerRightClick;
	public static BlockRotatableLamp rotatableLamp;

	public static BlockPipeBasic pipeBasic;

	public static void registerBlocks() {
		waterGrass = registerBlock(new BlockWaterGrass(), ItemColoredMod.class, true);
		largeCollisionTest = registerBlock(new BlockLargeCollisionTest());
		rightClickTest = registerBlock(new BlockRightClickTest());
		clientPlayerRightClick = registerBlock(new BlockClientPlayerRightClick());
		rotatableLamp = registerBlock(new BlockRotatableLamp());

		pipeBasic = registerBlock(new BlockPipeBasic());
	}

	// Register a Block with the default ItemBlock class
	private static <T extends Block> T registerBlock(T block) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));
		return block;
	}

	// Register a Block with a custom ItemBlock class
	private static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass, Object... constructorArgs) {
		GameRegistry.registerBlock(block, itemClass, block.getUnlocalizedName().replace("tile.", ""), constructorArgs);
		return block;
	}
}
