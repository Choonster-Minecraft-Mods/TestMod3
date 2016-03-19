package com.choonster.testmod3.block;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A block that forces a player to rotate (from the client side) while standing on it.
 * <p>
 * Standing on multiple blocks will increase the rotation speed.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,36093.0.html
 *
 * @author Choonster
 */
public class BlockClientPlayerRotation extends BlockStaticPressurePlate {
	/**
	 * The yaw rotation in degrees to add to the player each tick. Positive values rotate clockwise, negative values rotate anticlockwise.
	 */
	private static final float ROTATION_YAW = 5.0f;

	/**
	 * The pitch rotation in degrees to add to the player each tick
	 */
	private static final float ROTATION_PITCH = 2.0f;

	public BlockClientPlayerRotation() {
		super(Material.rock, "clientPlayerRotation");
	}

	/**
	 * Is the player currently pitching upwards?
	 * <p>
	 * This can safely be stored as a field because its value is determined by the client player's pitch.
	 */
	@SideOnly(Side.CLIENT)
	private boolean isPitchingUp;

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if (worldIn.isRemote && entityIn == TestMod3.proxy.getClientPlayer()) {
			if (MathHelper.epsilonEquals(Math.abs(entityIn.rotationPitch), 90.0f)) {
				isPitchingUp = !isPitchingUp;
				Logger.info("Switching pitch direction! Now pitching %s.", isPitchingUp ? "up" : "down");
			}

			entityIn.setAngles(ROTATION_YAW, isPitchingUp ? ROTATION_PITCH : -ROTATION_PITCH);
		}
	}
}
