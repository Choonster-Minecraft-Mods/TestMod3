package choonster.testmod3.client.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

/**
 * Client-only methods used by various blocks.
 *
 * @author Choonster
 */
public class ClientOnlyBlockMethods {
	public static void pressUseItemKeyBinding() {
		KeyMapping.click(Minecraft.getInstance().options.keyUse.getKey());
	}
}
