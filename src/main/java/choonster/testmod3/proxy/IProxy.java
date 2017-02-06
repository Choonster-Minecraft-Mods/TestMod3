package choonster.testmod3.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public interface IProxy {
	void preInit();

	void init();

	void postInit();

	/**
	 * Perform a right click on the client side.
	 *
	 * @throws WrongSideException If called on the dedicated server.
	 */
	void doClientRightClick();

	/**
	 * Get the client player.
	 *
	 * @return The client player
	 * @throws WrongSideException If called on the dedicated server.
	 */
	@Nullable
	EntityPlayer getClientPlayer();

	/**
	 * Get the client {@link World}.
	 *
	 * @return The client World
	 * @throws WrongSideException If called on the dedicated server.
	 */
	@Nullable
	World getClientWorld();

	/**
	 * Get the {@link IThreadListener} for the {@link MessageContext}'s {@link Side}.
	 *
	 * @param context The message context
	 * @return The thread listener
	 */
	IThreadListener getThreadListener(MessageContext context);

	/**
	 * Get the {@link EntityPlayer} from the {@link MessageContext}.
	 *
	 * @param context The message context
	 * @return The player
	 */
	EntityPlayer getPlayer(MessageContext context);

	/**
	 * Display the lock GUI.
	 *
	 * @param pos    The lock's block position
	 * @param facing The lock's facing
	 * @throws WrongSideException If called on the dedicated server.
	 */
	void displayLockGUI(BlockPos pos, EnumFacing facing);

	/**
	 * Thrown when a proxy method is called from the wrong side.
	 */
	class WrongSideException extends RuntimeException {
		public WrongSideException(String s) {
		}

		public WrongSideException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
