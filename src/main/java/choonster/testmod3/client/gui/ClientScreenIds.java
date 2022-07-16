package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import net.minecraft.resources.ResourceLocation;

public class ClientScreenIds {
	public static final ResourceLocation SURVIVAL_COMMAND_BLOCK = id("survival_command_block");
	public static final ResourceLocation SURVIVAL_COMMAND_BLOCK_MINECART = id("survival_command_block_minecart");
	public static final ResourceLocation LOCK = id("lock");

	private static ResourceLocation id(final String id) {
		return new ResourceLocation(TestMod3.MODID, id);
	}
}
