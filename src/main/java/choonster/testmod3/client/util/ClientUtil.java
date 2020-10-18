package choonster.testmod3.client.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;

/**
 * Client-specific utility methods designed to be called from common code.
 *
 * @author Choonster
 */
public class ClientUtil {
	@Nullable
	public static PlayerEntity getClientPlayer() {
		return DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientOnlyMethods::getClientPlayer);
	}
}
