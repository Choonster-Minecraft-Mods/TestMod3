package choonster.testmod3.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A block with a bounding box slightly smaller than a full cube so entities can collide with it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38530.0.html
 *
 * @author Choonster
 */
public class BlockSmallCollisionTest extends BlockTestMod3 {
	/**
	 * The block's collision bounding box.
	 */
	private static final AxisAlignedBB BOUNDING_BOX;

	static {
		final double offset = 0.0011;
		final double min = 0 + offset;
		final double max = 1 - offset;
		BOUNDING_BOX = new AxisAlignedBB(min, min, min, max, max, max);
	}

	public BlockSmallCollisionTest() {
		super(Material.IRON, "small_collision_test");
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (entityIn instanceof EntityLivingBase) {
			((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 10, 0));
		}
	}
}
