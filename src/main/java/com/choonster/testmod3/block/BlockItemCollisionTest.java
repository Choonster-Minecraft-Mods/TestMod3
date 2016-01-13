package com.choonster.testmod3.block;

import com.choonster.testmod3.Logger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A block that writes a message to the log when an item collides with it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34022.0.html
 *
 * @author Choonster
 */
public class BlockItemCollisionTest extends BlockTestMod3 {
	// A small value to offset each side of the block's bounding box by to allow entities to collide with the block
	// and thus call onEntityCollidedWithBlock
	private static final float ENTITY_COLLISION_MIN = 0.01f;

	public BlockItemCollisionTest() {
		super(Material.cloth, "itemCollisionTest");
		setBlockBounds();
	}

	private void setBlockBounds() {
		float min = ENTITY_COLLISION_MIN;
		float max = 1 - min;
		setBlockBounds(min, min, min, max, max, max);
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		if (!worldIn.isRemote && entityIn instanceof EntityItem) {
			Logger.info("Collision at %s: %s", pos, entityIn);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 1.0, pos.getZ() + 1.0);
	}
}
