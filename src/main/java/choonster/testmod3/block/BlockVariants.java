package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import net.minecraft.block.Block;
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

	public BlockVariants(final Block.Properties properties, final EnumType type, final BlockVariantGroup<EnumType, ? extends BlockVariants> variantGroup) {
		super(properties);
		this.type = type;
		this.variantGroup = variantGroup;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final EnumType newType = variantGroup.cycleVariant(type);
		final IBlockState newState = variantGroup.getBlock(newType).getDefaultState();

		world.setBlockState(pos, newState);

		return true;
	}

	public enum EnumType implements IStringSerializable {
		VARIANT_A("a"),
		VARIANT_B("b");

		private final String name;

		EnumType(final String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
