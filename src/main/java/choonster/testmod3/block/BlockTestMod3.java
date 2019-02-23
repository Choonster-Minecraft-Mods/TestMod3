package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * A base class for this mod's blocks.
 *
 * @author Choonster
 */
public class BlockTestMod3 extends Block {
	public BlockTestMod3(final Material material, final MapColor mapColor, final String blockName) {
		super(material, mapColor);
		RegistryUtil.setBlockName(this, blockName);
		setCreativeTab(TestMod3.creativeTab);
	}

	public BlockTestMod3(final Material materialIn, final String blockName) {
		this(materialIn, materialIn.getMaterialMapColor(), blockName);
	}
}
