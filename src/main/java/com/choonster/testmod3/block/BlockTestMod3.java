package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * A base class for this mod's blocks.
 *
 * @author Choonster
 */
public class BlockTestMod3 extends Block {
	public BlockTestMod3(Material material, MapColor mapColor, String blockName) {
		super(material, mapColor);
		setBlockName(this, blockName);
		setCreativeTab(TestMod3.creativeTab);
	}

	public BlockTestMod3(Material materialIn, String blockName) {
		this(materialIn, materialIn.getMaterialMapColor(), blockName);
	}

	/**
	 * Set the registry name of {@code block} to {@code blockName} and the unlocalised name to the full registry name.
	 *
	 * @param block     The block
	 * @param blockName The block's name
	 */
	public static void setBlockName(Block block, String blockName) {
		block.setRegistryName(blockName);
		block.setUnlocalizedName(block.getRegistryName());
	}
}
