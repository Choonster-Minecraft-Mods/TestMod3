package choonster.testmod3.client.cape;

import choonster.testmod3.TestMod3;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = TestMod3.MODID)
public class CapeEventHandler {
	@SubscribeEvent
	public static void entityJoinWorld(final EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AbstractClientPlayer && CapeUtils.doesPlayerHaveCape((AbstractClientPlayer) event.getEntity())) {
			// EntityJoinWorldEvent fires before the player's NetworkPlayerInfo is populated,
			// so we delay replacing the cape by at least 100 milliseconds.
			CapeUtils.queuePlayerCapeReplacement((AbstractClientPlayer) event.getEntity());
		}
	}
}
