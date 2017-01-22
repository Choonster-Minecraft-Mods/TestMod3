package choonster.testmod3.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class DedicatedServerProxy implements IProxy {
	@Override
	public void preInit() {

	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	/**
	 * Perform a right click on the client side.
	 */
	@Override
	public void doClientRightClick() {

	}

	/**
	 * Get the client player if on the client, or null if on the dedicated server.
	 *
	 * @return The client player
	 */
	@Nullable
	@Override
	public EntityPlayer getClientPlayer() {
		return null;
	}

	@Nullable
	@Override
	public World getClientWorld() {
		return null;
	}

	@Override
	public IThreadListener getThreadListener(MessageContext context) {
		return context.getServerHandler().player.mcServer;
	}

	@Override
	public EntityPlayer getPlayer(MessageContext context) {
		return context.getServerHandler().player;
	}

	@Override
	public void displayLockGUI(World world, BlockPos pos, EnumFacing facing) {

	}
}
