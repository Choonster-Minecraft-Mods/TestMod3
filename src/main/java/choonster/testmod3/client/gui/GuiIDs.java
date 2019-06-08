package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.MessageOpenClientGui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class GuiIDs {
	/**
	 * IDs for {@link GuiContainer} classes opened with {@link NetworkHooks#openGui}/{@link FMLPlayMessages.OpenContainer}.
	 */
	public static class Container {
		public static final ResourceLocation MOD_CHEST = id("chest");
	}

	/**
	 * IDs for {@link GuiScreen} classes opened with {@link MessageOpenClientGui}.
	 */
	public static class Client {
		public static final ResourceLocation SURVIVAL_COMMAND_BLOCK = id("survival_command_block");
		public static final ResourceLocation SURVIVAL_COMMAND_BLOCK_MINECART = id("survival_command_block_minecart");
		public static final ResourceLocation LOCK = id("lock");
	}

	private static ResourceLocation id(final String id) {
		return new ResourceLocation(TestMod3.MODID, id);
	}
}
