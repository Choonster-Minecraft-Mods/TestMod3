package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * A block that doesn't render in the world.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php?/topic/44951-obj-models/
 *
 * @author Choonster
 */
public class BlockInvisible extends Block {
	public BlockInvisible() {
		super(Material.ROCK);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}
}
