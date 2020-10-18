package choonster.testmod3.client.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.DistExecutor;

/**
 * Client-only methods used by various blocks.
 *
 * @author Choonster
 */
public class ClientOnlyBlockMethods {
	public static void pressUseItemKeyBinding() {
		KeyBinding.onTick(Minecraft.getInstance().gameSettings.keyBindUseItem.getKey());
	}

	public static DistExecutor.SafeRunnable rotateEntityTowards(final Entity entity, final float rotationYaw, final float rotationPitch) {
		return () -> entity.rotateTowards(rotationYaw, rotationPitch);
	}
}
