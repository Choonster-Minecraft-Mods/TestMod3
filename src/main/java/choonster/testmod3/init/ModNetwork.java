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
import net.minecraftforge.fml.relauncher.Side;

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

		registerMessage(MessageUpdateContainerFluidTank.Handler.class, MessageUpdateContainerFluidTank.class, Side.CLIENT);

		channel.messageBuilder(MessageBulkUpdateContainerHiddenBlockRevealers.class, 7)
				.decoder(MessageBulkUpdateContainerHiddenBlockRevealers::decode)
				.encoder(MessageBulkUpdateContainerHiddenBlockRevealers::encode)
				.consumer(MessageBulkUpdateContainerHiddenBlockRevealers::handle)
				.add();

		registerMessage(MessageUpdateContainerHiddenBlockRevealer.Handler.class, MessageUpdateContainerHiddenBlockRevealer.class, Side.CLIENT);

		registerMessage(MessageBulkUpdateContainerLastUseTimes.Handler.class, MessageBulkUpdateContainerLastUseTimes.class, Side.CLIENT);
		registerMessage(MessageUpdateContainerLastUseTime.Handler.class, MessageUpdateContainerLastUseTime.class, Side.CLIENT);

		registerMessage(MessageBulkUpdateContainerPigSpawnerFinites.Handler.class, MessageBulkUpdateContainerPigSpawnerFinites.class, Side.CLIENT);
		registerMessage(MessageUpdateContainerPigSpawnerFinite.Handler.class, MessageUpdateContainerPigSpawnerFinite.class, Side.CLIENT);

		return channel;
	}
}
