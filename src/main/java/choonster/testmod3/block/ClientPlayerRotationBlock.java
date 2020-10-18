package choonster.testmod3.block;

import choonster.testmod3.client.block.ClientOnlyBlockMethods;
import choonster.testmod3.client.util.ClientUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class ClientPlayerRotationBlock extends StaticPressurePlateBlock {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The yaw rotation in degrees to add to the player each tick. Positive values rotate clockwise, negative values rotate anticlockwise.
	 */
	private static final float ROTATION_YAW = 5.0f;

	/**
	 * The pitch rotation in degrees to add to the player each tick
	 */
	private static final float ROTATION_PITCH = 2.0f;

	public ClientPlayerRotationBlock(final Block.Properties properties) {
		super(properties);
	}

	/**
	 * Is the player currently pitching upwards?
	 * <p>
	 * This can safely be stored as a field because its value is determined by the client player's pitch.
	 */
	private boolean isPitchingUp;

	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
		final PlayerEntity clientPlayer = ClientUtil.getClientPlayer();

		if (world.isRemote && entity == clientPlayer) {
			if (MathHelper.epsilonEquals(Math.abs(entity.rotationPitch), 90.0f)) {
				isPitchingUp = !isPitchingUp;
				LOGGER.info("Switching pitch direction! Now pitching {}.", isPitchingUp ? "up" : "down");
			}

			DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientOnlyBlockMethods.rotateEntityTowards(entity, ROTATION_YAW, isPitchingUp ? ROTATION_PITCH : -ROTATION_PITCH));
		}
	}


}
