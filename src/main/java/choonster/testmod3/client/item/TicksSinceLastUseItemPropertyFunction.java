package choonster.testmod3.client.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * {@link ClampedItemPropertyFunction} to get the ticks since the last use of the item, as recorded by the item's {@link ILastUseTime} capability.
 * <p>
 * Returns {@link Float#MAX_VALUE} if the required information isn't available.
 */
public class TicksSinceLastUseItemPropertyFunction {
	/**
	 * The ID of this function.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "ticks_since_last_use");

	/**
	 * The function.
	 */
	private static final ClampedItemPropertyFunction GETTER = (stack, level, entity, seed) -> // TODO: This may be clamped
	{
		final Level world = level != null ? level : entity != null ? entity.getCommandSenderWorld() : null;

		if (world == null) {
			return Float.MAX_VALUE;
		}

		return LastUseTimeCapability.getLastUseTime(stack)
				.map(lastUseTime -> (float) world.getGameTime() - lastUseTime.get())
				.orElse(Float.MAX_VALUE);
	};

	/**
	 * Add this getter to an {@link Item}.
	 *
	 * @param item The item
	 */
	public static void registerForItem(final Item item) {
		ItemProperties.register(item, ID, GETTER);
	}
}
