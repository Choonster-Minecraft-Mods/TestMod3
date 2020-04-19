package choonster.testmod3.network;

import choonster.testmod3.block.FluidTankBlock;
import choonster.testmod3.fluid.FluidTankSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sent by {@link FluidTankBlock} to notify the player of the tank's contents.
 *
 * @author Choonster
 */
public class FluidTankContentsMessage {
	private final FluidTankSnapshot[] fluidTankSnapshots;

	public FluidTankContentsMessage(final FluidTankSnapshot[] fluidTankSnapshots) {
		this.fluidTankSnapshots = fluidTankSnapshots;
	}

	public static FluidTankContentsMessage decode(final PacketBuffer buffer) {
		final int numTanks = buffer.readInt();
		final FluidTankSnapshot[] fluidTankSnapshots = new FluidTankSnapshot[numTanks];

		for (int i = 0; i < numTanks; i++) {
			final FluidStack contents = FluidStack.readFromPacket(buffer);

			final int capacity = buffer.readInt();

			fluidTankSnapshots[i] = new FluidTankSnapshot(contents, capacity);
		}

		return new FluidTankContentsMessage(fluidTankSnapshots);
	}

	public static void encode(final FluidTankContentsMessage message, final PacketBuffer buffer) {
		buffer.writeInt(message.fluidTankSnapshots.length);

		for (final FluidTankSnapshot snapshot : message.fluidTankSnapshots) {
			final FluidStack contents = snapshot.getContents();
			contents.writeToPacket(buffer);

			buffer.writeInt(snapshot.getCapacity());
		}
	}

	public static void handle(final FluidTankContentsMessage message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final ClientPlayerEntity player = Minecraft.getInstance().player;

			FluidTankBlock.getFluidDataForDisplay(message.fluidTankSnapshots).forEach(player::sendMessage);
		}));

		ctx.get().setPacketHandled(true);
	}
}
