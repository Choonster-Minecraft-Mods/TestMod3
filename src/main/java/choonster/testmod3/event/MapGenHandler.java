package choonster.testmod3.event;

import choonster.testmod3.world.gen.structure.MapGenScatteredFeatureModBiomes;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapGenHandler {

	@SubscribeEvent
	public void initMapGen(InitMapGenEvent event) {
		if (event.getType() == InitMapGenEvent.EventType.SCATTERED_FEATURE) {
			event.setNewGen(new MapGenScatteredFeatureModBiomes());
		}
	}
}
