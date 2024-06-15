package choonster.testmod3.client.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModDataComponents;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * {@link ClampedItemPropertyFunction} to get whether hidden blocks are being revealed by an item's {@link ModDataComponents#REVEAL_HIDDEN_BLOCKS} component.
 */
public class RevealHiddenBlocksItemPropertyFunction {
	/**
	 * The ID of this function.
	 */
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, "reveal_hidden_blocks");

	/**
	 * The function.
	 */
	private static final ClampedItemPropertyFunction FUNCTION = (stack, level, entity, seed) ->
			stack.has(ModDataComponents.REVEAL_HIDDEN_BLOCKS.get()) ? 1 : 0;

	/**
	 * Add this function to an {@link Item}.
	 *
	 * @param item The item
	 */
	public static void registerForItem(final Item item) {
		ItemProperties.register(item, ID, FUNCTION);
	}
}
