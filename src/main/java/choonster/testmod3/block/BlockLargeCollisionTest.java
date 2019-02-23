package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * A Block with a 3x3x3 bounding box.
 * <p>
 * Currently only the selection bounding box works.
 * Entity collision still treats the bounding box as 1x1x1 and glitches out if you try to enter this bounding box.
 *
 * @author Choonster
 */
public class BlockLargeCollisionTest extends Block {
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(-1, -1, -1, 2, 2, 2);

	public BlockLargeCollisionTest() {
		super(Material.CLOTH);
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		return BOUNDING_BOX;
	}
}
