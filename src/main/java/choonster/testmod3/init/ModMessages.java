package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.*;
import choonster.testmod3.network.capability.fluidhandler.MessageBulkUpdateContainerFluidTanks;
import choonster.testmod3.network.capability.fluidhandler.MessageUpdateContainerFluidTank;
import choonster.testmod3.network.capability.hiddenblock.MessageBulkUpdateContainerHiddenBlockRevealers;
import choonster.testmod3.network.capability.hiddenblock.MessageUpdateContainerHiddenBlockRevealer;
import choonster.testmod3.network.capability.lastusetime.MessageBulkUpdateContainerLastUseTimes;
import choonster.testmod3.network.capability.lastusetime.MessageUpdateContainerLastUseTime;
import choonster.testmod3.network.capability.pigspawner.MessageBulkUpdateContainerPigSpawnerFinites;
import choonster.testmod3.network.capability.pigspawner.MessageUpdateContainerPigSpawnerFinite;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ModMessages {
	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static void registerMessages() {
		registerMessage(MessageSurvivalCommandBlockSaveChanges.Handler.class, MessageSurvivalCommandBlockSaveChanges.class, Side.SERVER);
		registerMessage(MessagePlayerReceivedLoot.Handler.class, MessagePlayerReceivedLoot.class, Side.CLIENT);
		registerMessage(MessageFluidTankContents.Handler.class, MessageFluidTankContents.class, Side.CLIENT);
		registerMessage(MessageLockSetLockCode.Handler.class, MessageLockSetLockCode.class, Side.SERVER);
		registerMessage(MessageOpenLockGui.Handler.class, MessageOpenLockGui.class, Side.CLIENT);
		registerMessage(MessageUpdateChunkEnergyValue.Handler.class, MessageUpdateChunkEnergyValue.class, Side.CLIENT);

		registerMessage(MessageBulkUpdateContainerFluidTanks.Handler.class, MessageBulkUpdateContainerFluidTanks.class, Side.CLIENT);
		registerMessage(MessageUpdateContainerFluidTank.Handler.class, MessageUpdateContainerFluidTank.class, Side.CLIENT);

		registerMessage(MessageBulkUpdateContainerHiddenBlockRevealers.Handler.class, MessageBulkUpdateContainerHiddenBlockRevealers.class, Side.CLIENT);
		registerMessage(MessageUpdateContainerHiddenBlockRevealer.Handler.class, MessageUpdateContainerHiddenBlockRevealer.class, Side.CLIENT);

		registerMessage(MessageBulkUpdateContainerLastUseTimes.Handler.class, MessageBulkUpdateContainerLastUseTimes.class, Side.CLIENT);
		registerMessage(MessageUpdateContainerLastUseTime.Handler.class, MessageUpdateContainerLastUseTime.class, Side.CLIENT);

		registerMessage(MessageBulkUpdateContainerPigSpawnerFinites.Handler.class, MessageBulkUpdateContainerPigSpawnerFinites.class, Side.CLIENT);
		registerMessage(MessageUpdateContainerPigSpawnerFinite.Handler.class, MessageUpdateContainerPigSpawnerFinite.class, Side.CLIENT);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(final Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, final Class<REQ> requestMessageType, final Side receivingSide) {
		TestMod3.network.registerMessage(messageHandler, requestMessageType, messageID++, receivingSide);
	}
}
