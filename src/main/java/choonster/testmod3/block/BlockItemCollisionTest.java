package choonster.testmod3.block;

import choonster.testmod3.Logger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A block that writes a message to the log when an item collides with it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34022.0.html
 *
 * @author Choonster
 */
public class BlockItemCollisionTest extends BlockTestMod3 {
	private static final AxisAlignedBB BOUNDING_BOX;

	static {
		// A small value to offset each side of the block's bounding box by to allow entities to collide with the block
		// and thus call onEntityCollidedWithBlock
		final float minBound = 0.01f;
		final float maxBound = 1 - minBound;

		BOUNDING_BOX = new AxisAlignedBB(minBound, minBound, minBound, maxBound, maxBound, maxBound);
	}

	public BlockItemCollisionTest() {
		super(Material.CLOTH, "item_collision_test");
	}

	@Override
	public void onEntityCollision(final World worldIn, final BlockPos pos, final IBlockState state, final Entity entityIn) {
		super.onEntityCollision(worldIn, pos, state, entityIn);

		if (!worldIn.isRemote && entityIn instanceof EntityItem) {
			Logger.info("Collision at %s: %s", pos, entityIn);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		return BOUNDING_BOX;
	}
}
