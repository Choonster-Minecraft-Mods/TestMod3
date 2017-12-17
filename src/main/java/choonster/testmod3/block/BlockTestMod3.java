package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

/**
 * A base class for this mod's blocks.
 *
 * @author Choonster
 */
public class BlockTestMod3 extends Block {
	public BlockTestMod3(final Material material, final MapColor mapColor, final String blockName) {
		super(material, mapColor);
		setBlockName(this, blockName);
		setCreativeTab(TestMod3.creativeTab);
	}

	public BlockTestMod3(final Material materialIn, final String blockName) {
		this(materialIn, materialIn.getMaterialMapColor(), blockName);
	}

	/**
	 * Set the registry name of {@code block} to {@code blockName} and the unlocalised name to the full registry name.
	 *
	 * @param block     The block
	 * @param blockName The block's name
	 */
	public static void setBlockName(final Block block, final String blockName) {
		block.setRegistryName(TestMod3.MODID, blockName);
		final ResourceLocation registryName = Objects.requireNonNull(block.getRegistryName());
		block.setUnlocalizedName(registryName.toString());
	}
}
