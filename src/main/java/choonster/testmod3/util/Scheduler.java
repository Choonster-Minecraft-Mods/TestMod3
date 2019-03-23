package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Allows tasks to be scheduled to run at the start of the next server tick. Must not be used from the logical client.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class Scheduler {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final Queue<Runnable> tasks = new ArrayDeque<>();

	/**
	 * Schedule a task to be run at the start of the next server tick. Must not be called from the logical client.
	 *
	 * @param task The task
	 */
	public static void scheduleTask(final Runnable task) {
		tasks.add(task);
	}

	/**
	 * Run all scheduled tasks at the start of the server tick.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void serverTick(final TickEvent.ServerTickEvent event) {
		if (event.phase != TickEvent.Phase.START) return;

		while (!tasks.isEmpty()) {
			try {
				tasks.poll().run();
			} catch (final Throwable throwable) {
				LOGGER.error("Error running scheduled task", throwable);
			}
		}
	}
}
