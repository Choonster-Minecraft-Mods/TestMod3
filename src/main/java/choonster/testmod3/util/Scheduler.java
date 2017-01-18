package choonster.testmod3.util;

import choonster.testmod3.Logger;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Allows tasks to be scheduled to run at the start of the next server tick. Must not be used from the logical client.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber
public class Scheduler {
	private static final Queue<Runnable> tasks = new ArrayDeque<>();

	/**
	 * Schedule a task to be run at the start of the next server tick. Must not be called from the logical client.
	 *
	 * @param task The task
	 */
	public static void scheduleTask(Runnable task) {
		tasks.add(task);
	}

	/**
	 * Run all scheduled tasks at the start of the server tick.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void serverTick(TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.START) return;

		while (!tasks.isEmpty()) {
			try {
				tasks.poll().run();
			} catch (Throwable throwable) {
				Logger.error(throwable, "Error running scheduled task");
			}
		}
	}
}
