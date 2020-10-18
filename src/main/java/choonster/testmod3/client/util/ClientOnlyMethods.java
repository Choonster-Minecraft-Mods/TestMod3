package choonster.testmod3.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;

/**
 * Contains implementations of client-only methods designed to be called through {@link DistExecutor}.
 *
 * @author Choonster
 */
class ClientOnlyMethods {
	@Nullable
	public static PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}
}
