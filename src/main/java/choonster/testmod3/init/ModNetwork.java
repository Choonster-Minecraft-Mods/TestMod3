package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.*;
import choonster.testmod3.network.capability.fluidhandler.BulkUpdateContainerFluidTanksMessage;
import choonster.testmod3.network.capability.fluidhandler.UpdateContainerFluidTankMessage;
import choonster.testmod3.network.capability.hiddenblock.BulkUpdateContainerHiddenBlockRevealersMessage;
import choonster.testmod3.network.capability.hiddenblock.UpdateContainerHiddenBlockRevealerMessage;
import choonster.testmod3.network.capability.lastusetime.BulkUpdateContainerLastUseTimesMessage;
import choonster.testmod3.network.capability.lastusetime.UpdateContainerLastUseTimeMessage;
import choonster.testmod3.network.capability.pigspawner.BulkUpdateContainerPigSpawnerFinitesMessage;
import choonster.testmod3.network.capability.pigspawner.UpdateContainerPigSpawnerFiniteMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetwork {
	public static final ResourceLocation CHANNEL_NAME = new ResourceLocation(TestMod3.MODID, "network");

	public static final String NETWORK_VERSION = new ResourceLocation(TestMod3.MODID, "1").toString();

	public static SimpleChannel getNetworkChannel() {
		final SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
				.clientAcceptedVersions(version -> true)
				.serverAcceptedVersions(version -> true)
				.networkProtocolVersion(() -> NETWORK_VERSION)
				.simpleChannel();

		channel.messageBuilder(SaveSurvivalCommandBlockMessage.class, 1)
				.decoder(SaveSurvivalCommandBlockMessage::decode)
				.encoder(SaveSurvivalCommandBlockMessage::encode)
				.consumer(SaveSurvivalCommandBlockMessage::handle)
				.add();

		channel.messageBuilder(FluidTankContentsMessage.class, 2)
				.decoder(FluidTankContentsMessage::decode)
				.encoder(FluidTankContentsMessage::encode)
				.consumer(FluidTankContentsMessage::handle)
				.add();

		channel.messageBuilder(SetLockCodeMessage.class, 3)
				.decoder(SetLockCodeMessage::decode)
				.encoder(SetLockCodeMessage::encode)
				.consumer(SetLockCodeMessage::handle)
				.add();

		channel.messageBuilder(UpdateChunkEnergyValueMessage.class, 4)
				.decoder(UpdateChunkEnergyValueMessage::decode)
				.encoder(UpdateChunkEnergyValueMessage::encode)
				.consumer(UpdateChunkEnergyValueMessage::handle)
				.add();


		channel.messageBuilder(BulkUpdateContainerFluidTanksMessage.class, 5)
				.decoder(BulkUpdateContainerFluidTanksMessage::decode)
				.encoder(BulkUpdateContainerFluidTanksMessage::encode)
				.consumer(BulkUpdateContainerFluidTanksMessage::handle)
				.add();

		channel.messageBuilder(UpdateContainerFluidTankMessage.class, 6)
				.decoder(UpdateContainerFluidTankMessage::decode)
				.encoder(UpdateContainerFluidTankMessage::encode)
				.consumer(UpdateContainerFluidTankMessage::handle)
				.add();


		channel.messageBuilder(BulkUpdateContainerHiddenBlockRevealersMessage.class, 7)
				.decoder(BulkUpdateContainerHiddenBlockRevealersMessage::decode)
				.encoder(BulkUpdateContainerHiddenBlockRevealersMessage::encode)
				.consumer(BulkUpdateContainerHiddenBlockRevealersMessage::handle)
				.add();

		channel.messageBuilder(UpdateContainerHiddenBlockRevealerMessage.class, 8)
				.decoder(UpdateContainerHiddenBlockRevealerMessage::decode)
				.encoder(UpdateContainerHiddenBlockRevealerMessage::encode)
				.consumer(UpdateContainerHiddenBlockRevealerMessage::handle)
				.add();


		channel.messageBuilder(BulkUpdateContainerLastUseTimesMessage.class, 9)
				.decoder(BulkUpdateContainerLastUseTimesMessage::decode)
				.encoder(BulkUpdateContainerLastUseTimesMessage::encode)
				.consumer(BulkUpdateContainerLastUseTimesMessage::handle)
				.add();

		channel.messageBuilder(UpdateContainerLastUseTimeMessage.class, 10)
				.decoder(UpdateContainerLastUseTimeMessage::decode)
				.encoder(UpdateContainerLastUseTimeMessage::encode)
				.consumer(UpdateContainerLastUseTimeMessage::handle)
				.add();


		channel.messageBuilder(BulkUpdateContainerPigSpawnerFinitesMessage.class, 11)
				.decoder(BulkUpdateContainerPigSpawnerFinitesMessage::decode)
				.encoder(BulkUpdateContainerPigSpawnerFinitesMessage::encode)
				.consumer(BulkUpdateContainerPigSpawnerFinitesMessage::handle)
				.add();

		channel.messageBuilder(UpdateContainerPigSpawnerFiniteMessage.class, 12)
				.decoder(UpdateContainerPigSpawnerFiniteMessage::decode)
				.encoder(UpdateContainerPigSpawnerFiniteMessage::encode)
				.consumer(UpdateContainerPigSpawnerFiniteMessage::handle)
				.add();

		channel.messageBuilder(OpenClientScreenMessage.class, 13)
				.decoder(OpenClientScreenMessage::decode)
				.encoder(OpenClientScreenMessage::encode)
				.consumer(OpenClientScreenMessage::handle)
				.add();

		return channel;
	}
}
