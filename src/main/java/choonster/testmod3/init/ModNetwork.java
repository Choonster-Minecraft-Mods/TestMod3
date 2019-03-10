package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.MessageFluidTankContents;
import choonster.testmod3.network.MessageLockSetLockCode;
import choonster.testmod3.network.MessageSurvivalCommandBlockSaveChanges;
import choonster.testmod3.network.MessageUpdateChunkEnergyValue;
import choonster.testmod3.network.capability.fluidhandler.MessageBulkUpdateContainerFluidTanks;
import choonster.testmod3.network.capability.fluidhandler.MessageUpdateContainerFluidTank;
import choonster.testmod3.network.capability.hiddenblock.MessageBulkUpdateContainerHiddenBlockRevealers;
import choonster.testmod3.network.capability.hiddenblock.MessageUpdateContainerHiddenBlockRevealer;
import choonster.testmod3.network.capability.lastusetime.MessageBulkUpdateContainerLastUseTimes;
import choonster.testmod3.network.capability.lastusetime.MessageUpdateContainerLastUseTime;
import choonster.testmod3.network.capability.pigspawner.MessageBulkUpdateContainerPigSpawnerFinites;
import choonster.testmod3.network.capability.pigspawner.MessageUpdateContainerPigSpawnerFinite;
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

		channel.messageBuilder(MessageSurvivalCommandBlockSaveChanges.class, 1)
				.decoder(MessageSurvivalCommandBlockSaveChanges::decode)
				.encoder(MessageSurvivalCommandBlockSaveChanges::encode)
				.consumer(MessageSurvivalCommandBlockSaveChanges::handle)
				.add();

		channel.messageBuilder(MessageFluidTankContents.class, 2)
				.decoder(MessageFluidTankContents::decode)
				.encoder(MessageFluidTankContents::encode)
				.consumer(MessageFluidTankContents::handle)
				.add();

		channel.messageBuilder(MessageLockSetLockCode.class, 3)
				.decoder(MessageLockSetLockCode::decode)
				.encoder(MessageLockSetLockCode::encode)
				.consumer(MessageLockSetLockCode::handle)
				.add();

		channel.messageBuilder(MessageUpdateChunkEnergyValue.class, 4)
				.decoder(MessageUpdateChunkEnergyValue::decode)
				.encoder(MessageUpdateChunkEnergyValue::encode)
				.consumer(MessageUpdateChunkEnergyValue::handle)
				.add();


		channel.messageBuilder(MessageBulkUpdateContainerFluidTanks.class, 5)
				.decoder(MessageBulkUpdateContainerFluidTanks::decode)
				.encoder(MessageBulkUpdateContainerFluidTanks::encode)
				.consumer(MessageBulkUpdateContainerFluidTanks::handle)
				.add();

		channel.messageBuilder(MessageUpdateContainerFluidTank.class, 6)
				.decoder(MessageUpdateContainerFluidTank::decode)
				.encoder(MessageUpdateContainerFluidTank::encode)
				.consumer(MessageUpdateContainerFluidTank::handle)
				.add();


		channel.messageBuilder(MessageBulkUpdateContainerHiddenBlockRevealers.class, 7)
				.decoder(MessageBulkUpdateContainerHiddenBlockRevealers::decode)
				.encoder(MessageBulkUpdateContainerHiddenBlockRevealers::encode)
				.consumer(MessageBulkUpdateContainerHiddenBlockRevealers::handle)
				.add();

		channel.messageBuilder(MessageUpdateContainerHiddenBlockRevealer.class, 8)
				.decoder(MessageUpdateContainerHiddenBlockRevealer::decode)
				.encoder(MessageUpdateContainerHiddenBlockRevealer::encode)
				.consumer(MessageUpdateContainerHiddenBlockRevealer::handle)
				.add();


		channel.messageBuilder(MessageBulkUpdateContainerLastUseTimes.class, 9)
				.decoder(MessageBulkUpdateContainerLastUseTimes::decode)
				.encoder(MessageBulkUpdateContainerLastUseTimes::encode)
				.consumer(MessageBulkUpdateContainerLastUseTimes::handle)
				.add();

		channel.messageBuilder(MessageUpdateContainerLastUseTime.class, 10)
				.decoder(MessageUpdateContainerLastUseTime::decode)
				.encoder(MessageUpdateContainerLastUseTime::encode)
				.consumer(MessageUpdateContainerLastUseTime::handle)
				.add();


		channel.messageBuilder(MessageBulkUpdateContainerPigSpawnerFinites.class, 11)
				.decoder(MessageBulkUpdateContainerPigSpawnerFinites::decode)
				.encoder(MessageBulkUpdateContainerPigSpawnerFinites::encode)
				.consumer(MessageBulkUpdateContainerPigSpawnerFinites::handle)
				.add();

		channel.messageBuilder(MessageUpdateContainerPigSpawnerFinite.class, 12)
				.decoder(MessageUpdateContainerPigSpawnerFinite::decode)
				.encoder(MessageUpdateContainerPigSpawnerFinite::encode)
				.consumer(MessageUpdateContainerPigSpawnerFinite::handle)
				.add();

		return channel;
	}
}
