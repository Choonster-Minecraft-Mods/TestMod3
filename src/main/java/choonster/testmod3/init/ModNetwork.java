package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.*;
import choonster.testmod3.network.capability.UpdateMenuFluidTankMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkProtocol;
import net.minecraftforge.network.SimpleChannel;

public class ModNetwork {
	public static final ResourceLocation CHANNEL_NAME = ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, "network");

	public static final int NETWORK_VERSION = 6;

	public static SimpleChannel getNetworkChannel() {
		final var channel = ChannelBuilder.named(CHANNEL_NAME)
				.clientAcceptedVersions(Channel.VersionTest.exact(NETWORK_VERSION))
				.serverAcceptedVersions(Channel.VersionTest.exact(NETWORK_VERSION))
				.networkProtocolVersion(NETWORK_VERSION)
				.simpleChannel();

		@SuppressWarnings("unchecked") final var openClientScreenMessageClass =
				(Class<OpenClientScreenMessage<?>>) (Class<?>) OpenClientScreenMessage.class;

		channel.protocol(NetworkProtocol.PLAY)
				.serverbound()
				.addMain(SaveSurvivalCommandBlockMessage.class, SaveSurvivalCommandBlockMessage.STREAM_CODEC, SaveSurvivalCommandBlockMessage::handle)
				.addMain(SetLockCodeMessage.class, SetLockCodeMessage.STREAM_CODEC, SetLockCodeMessage::handle)
				.addMain(LeftClickEmptyMessage.class, LeftClickEmptyMessage.STREAM_CODEC, LeftClickEmptyMessage::handle)
				.clientbound()
				.addMain(UpdateChunkEnergyValueMessage.class, UpdateChunkEnergyValueMessage.STREAM_CODEC, UpdateChunkEnergyValueMessage::handle)
				.addMain(UpdateMenuFluidTankMessage.class, UpdateMenuFluidTankMessage.STREAM_CODEC, UpdateMenuFluidTankMessage::handle)
				.addMain(openClientScreenMessageClass, OpenClientScreenMessage.STREAM_CODEC, OpenClientScreenMessage::handle)
				.build();

		return channel;
	}
}
