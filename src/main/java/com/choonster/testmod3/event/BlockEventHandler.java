package com.choonster.testmod3.event;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockEventHandler {

	/**
	 * Drop 2 sticks from leaves.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,32525.0.html
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public void harvestDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.state.getBlock().isLeaves(event.world, event.pos)) {
			event.drops.add(new ItemStack(Items.stick, 2));
		}
	}
}
