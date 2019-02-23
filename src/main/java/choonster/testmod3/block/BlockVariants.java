package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A block with several variants.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2594064-metadata-blocks-dont-have-textures
 *
 * @author Choonster
 */
public class BlockVariants extends Block {
	private final BlockVariantGroup<EnumType, ? extends BlockVariants> variantGroup;
	private final EnumType type;

	public BlockVariants(final EnumType type, final Material material, final BlockVariantGroup<EnumType, ? extends BlockVariants> variantGroup) {
		super(material);
		this.type = type;
		this.variantGroup = variantGroup;
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		final EnumType newType = variantGroup.cycleVariant(type);
		final Block newSBlock = variantGroup.getBlock(newType);

		worldIn.setBlockState(pos, newSBlock.getDefaultState());

		return true;
	}

	public enum EnumType implements IStringSerializable {
		VARIANT_A(0, "a"),
		VARIANT_B(1, "b");

		// TODO: Remove in 1.13
		private final int meta;
		private final String name;

		EnumType(final int meta, final String name) {
			this.meta = meta;
			this.name = name;
		}

		// TODO: Remove in 1.13
		@Deprecated
		public int getMeta() {
			return meta;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
