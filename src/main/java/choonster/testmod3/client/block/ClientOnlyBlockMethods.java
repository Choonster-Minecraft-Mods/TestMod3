package choonster.testmod3.client.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

/**
 * Client-only methods used by various blocks.
 *
 * @author Choonster
 */
public class ClientOnlyBlockMethods {
	public static void pressUseItemKeyBinding() {
		KeyBinding.onTick(Minecraft.getInstance().gameSettings.keyBindUseItem.getKey());
	}
}
