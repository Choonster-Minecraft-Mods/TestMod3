package choonster.testmod3.client.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;

/**
 * {@link IItemPropertyGetter} to get whether hidden blocks are being revealed by an item's {@link IHiddenBlockRevealer} capability.
 */
public class RevealHiddenBlocksItemPropertyGetter {
	/**
	 * The ID of this getter.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "reveal_hidden_blocks");

	/**
	 * The getter.
	 */
	private static final IItemPropertyGetter GETTER = (stack, worldIn, entityIn) ->
			HiddenBlockRevealerCapability.getHiddenBlockRevealer(stack)
					.map(hiddenBlockRevealer -> hiddenBlockRevealer.revealHiddenBlocks() ? 1 : 0)
					.orElse(0);

	/**
	 * Add this getter to an {@link Item}.
	 *
	 * @param item The item
	 */
	public static void registerForItem(final Item item) {
		ItemModelsProperties.register(item, ID, GETTER);
	}
}
