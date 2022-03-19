package choonster.testmod3.world.level.block;

import choonster.testmod3.client.util.ClientUtil;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.slf4j.Logger;

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
	private static final Logger LOGGER = LogUtils.getLogger();

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
	public void entityInside(final BlockState state, final Level world, final BlockPos pos, final Entity entity) {
		if (!world.isClientSide) {
			return;
		}

		final Player clientPlayer = ClientUtil.getClientPlayer();

		if (entity == clientPlayer) {
			if (Mth.equal(Math.abs(entity.getXRot()), 90.0f)) {
				isPitchingUp = !isPitchingUp;
				LOGGER.info("Switching pitch direction! Now pitching {}.", isPitchingUp ? "up" : "down");
			}

			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> entity.turn(ROTATION_YAW, isPitchingUp ? ROTATION_PITCH : -ROTATION_PITCH));
		}
	}
}
