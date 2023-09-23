package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.*;
import choonster.testmod3.network.capability.fluidhandler.UpdateMenuFluidTankMessage;
import choonster.testmod3.network.capability.hiddenblock.UpdateMenuHiddenBlockRevealerMessage;
import choonster.testmod3.network.capability.lastusetime.UpdateMenuLastUseTimeMessage;
import choonster.testmod3.network.capability.pigspawner.UpdateMenuPigSpawnerFiniteMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ModNetwork {
	public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(TestMod3.MODID, "network");

	public static final int NETWORK_VERSION = 3;

	public static SimpleChannel getNetworkChannel() {
		final SimpleChannel channel = ChannelBuilder.named(CHANNEL_NAME)
				.clientAcceptedVersions(Channel.VersionTest.exact(NETWORK_VERSION))
				.serverAcceptedVersions(Channel.VersionTest.exact(NETWORK_VERSION))
				.networkProtocolVersion(NETWORK_VERSION)
				.simpleChannel();

		channel.messageBuilder(SaveSurvivalCommandBlockMessage.class, 1)
				.decoder(SaveSurvivalCommandBlockMessage::decode)
				.encoder(SaveSurvivalCommandBlockMessage::encode)
				.consumerMainThread(SaveSurvivalCommandBlockMessage::handle)
				.add();

		channel.messageBuilder(FluidTankContentsMessage.class, 2)
				.decoder(FluidTankContentsMessage::decode)
				.encoder(FluidTankContentsMessage::encode)
				.consumerMainThread(FluidTankContentsMessage::handle)
				.add();

		channel.messageBuilder(SetLockCodeMessage.class, 3)
				.decoder(SetLockCodeMessage::decode)
				.encoder(SetLockCodeMessage::encode)
				.consumerMainThread(SetLockCodeMessage::handle)
				.add();

		channel.messageBuilder(UpdateChunkEnergyValueMessage.class, 4)
				.decoder(UpdateChunkEnergyValueMessage::decode)
				.encoder(UpdateChunkEnergyValueMessage::encode)
				.consumerMainThread(UpdateChunkEnergyValueMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuFluidTankMessage.class, 6)
				.decoder(UpdateMenuFluidTankMessage::decode)
				.encoder(UpdateMenuFluidTankMessage::encode)
				.consumerMainThread(UpdateMenuFluidTankMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuHiddenBlockRevealerMessage.class, 8)
				.decoder(UpdateMenuHiddenBlockRevealerMessage::decode)
				.encoder(UpdateMenuHiddenBlockRevealerMessage::encode)
				.consumerMainThread(UpdateMenuHiddenBlockRevealerMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuLastUseTimeMessage.class, 10)
				.decoder(UpdateMenuLastUseTimeMessage::decode)
				.encoder(UpdateMenuLastUseTimeMessage::encode)
				.consumerMainThread(UpdateMenuLastUseTimeMessage::handle)
				.add();

		channel.messageBuilder(UpdateMenuPigSpawnerFiniteMessage.class, 12)
				.decoder(UpdateMenuPigSpawnerFiniteMessage::decode)
				.encoder(UpdateMenuPigSpawnerFiniteMessage::encode)
				.consumerMainThread(UpdateMenuPigSpawnerFiniteMessage::handle)
				.add();

		channel.messageBuilder(OpenClientScreenMessage.class, 13)
				.decoder(OpenClientScreenMessage::decode)
				.encoder(OpenClientScreenMessage::encode)
				.consumerMainThread(OpenClientScreenMessage::handle)
				.add();

		channel.messageBuilder(LeftClickEmptyMessage.class, 14)
				.decoder(LeftClickEmptyMessage::decode)
				.encoder(LeftClickEmptyMessage::encode)
				.consumerMainThread(LeftClickEmptyMessage::handle)
				.add();

		return channel;
	}
}
