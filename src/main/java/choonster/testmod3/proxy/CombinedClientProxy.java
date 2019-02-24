package choonster.testmod3.proxy;


import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.CapabilityLock;
import choonster.testmod3.client.command.ModCommandsClient;
import choonster.testmod3.client.gui.GuiLock;
import choonster.testmod3.client.init.ModKeyBindings;
import choonster.testmod3.client.renderer.entity.ModRenderers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class CombinedClientProxy implements IProxy {

	private final Minecraft MINECRAFT = Minecraft.getMinecraft();

	@Override
	public void preInit() {
		ModRenderers.register();
		ModCommandsClient.registerCommands();
		ModKeyBindings.registerKeyBindings();
	}

	@Override
	public void init() {

	}

	@Override
	public void postInit() {

	}

	@Override
	public void doClientRightClick() {

	}

	@Nullable
	@Override
	public EntityPlayer getClientPlayer() {
		return MINECRAFT.player;
	}

	@Nullable
	@Override
	public World getClientWorld() {
		return MINECRAFT.world;
	}

	@Override
	public IThreadListener getThreadListener(final MessageContext context) {
		if (context.side.isClient()) {
			return MINECRAFT;
		} else {
			return context.getServerHandler().player.server;
		}
	}

	@Override
	public EntityPlayer getPlayer(final MessageContext context) {
		if (context.side.isClient()) {
			return MINECRAFT.player;
		} else {
			return context.getServerHandler().player;
		}
	}

	@Override
	public void displayLockGUI(final BlockPos pos, final EnumFacing facing) {
		final ILock lock = CapabilityLock.getLock(MINECRAFT.world, pos, facing);
		if (lock != null) {
			MINECRAFT.displayGuiScreen(new GuiLock(lock, pos, facing));
		}
	}
}
