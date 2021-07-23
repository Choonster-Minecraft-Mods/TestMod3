package choonster.testmod3.client.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * {@link IItemPropertyGetter} to get the ticks since the last use of the item, as recorded by the item's {@link ILastUseTime} capability.
 * <p>
 * Returns {@link Float#MAX_VALUE} if the required information isn't available.
 */
public class TicksSinceLastUseItemPropertyGetter {
	/**
	 * The ID of this getter.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "ticks_since_last_use");

	/**
	 * The getter.
	 */
	private static final IItemPropertyGetter GETTER = (stack, worldIn, entityIn) ->
	{
		final World world = worldIn != null ? worldIn : entityIn != null ? entityIn.getCommandSenderWorld() : null;

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
		ItemModelsProperties.register(item, ID, GETTER);
	}
}
