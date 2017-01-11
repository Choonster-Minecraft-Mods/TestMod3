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
	 */
	void doClientRightClick();

	/**
	 * Get the client player if on the client, or null if on the dedicated server.
	 *
	 * @return The client player
	 */
	@Nullable
	EntityPlayer getClientPlayer();

	/**
	 * Get the client {@link World} if on the client, or null if on the dedicated server.
	 *
	 * @return The client World
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
	 * @param world  The lock's World
	 * @param pos    The lock's block position
	 * @param facing The lock's facing
	 */
	void displayLockGUI(World world, BlockPos pos, EnumFacing facing);
}
