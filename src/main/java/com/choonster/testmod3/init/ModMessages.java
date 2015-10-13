package com.choonster.testmod3.init;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.network.MessageSurvivalCommandBlockSaveChanges;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public class ModMessages {
	private static int messageID = 0;

	public static void registerMessages() {
		registerMessage(MessageSurvivalCommandBlockSaveChanges.Handler.class, MessageSurvivalCommandBlockSaveChanges.class, Side.SERVER);
	}

	private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side receivingSide) {
		TestMod3.network.registerMessage(messageHandler, requestMessageType, messageID++, receivingSide);
	}
}
