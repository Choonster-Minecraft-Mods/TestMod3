package com.choonster.testmod3.client.event;

import com.choonster.testmod3.item.ItemModBow;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent event) {
		if (event.entity.isUsingItem() && event.entity.getItemInUse().getItem() instanceof ItemModBow) {
			float fovModifier = event.entity.getItemInUseDuration() / 20.0f;

			if (fovModifier > 1.0f) {
				fovModifier = 1.0f;
			} else {
				fovModifier *= fovModifier;
			}

			event.newfov = event.fov * (1.0f - fovModifier * 0.15f);
		}
	}
}
