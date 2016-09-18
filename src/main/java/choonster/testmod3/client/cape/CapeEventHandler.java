package choonster.testmod3.client.cape;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(Side.CLIENT)
public class CapeEventHandler {
	@SubscribeEvent
	public static void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AbstractClientPlayer && CapeUtils.doesPlayerHaveCape((AbstractClientPlayer) event.getEntity())) {
			// EntityJoinWorldEvent fires before the player's NetworkPlayerInfo is populated,
			// so we delay replacing the cape by at least 100 milliseconds.
			CapeUtils.queuePlayerCapeReplacement((AbstractClientPlayer) event.getEntity());
		}
	}
}
