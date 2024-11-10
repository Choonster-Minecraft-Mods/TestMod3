package choonster.testmod3.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;

/**
 * An Item with a different model depending on how long ago it was last used.
 * <p>
 * Doesn't use {@link ItemCooldowns} because that would prevent using the item again before the cooldown has expired.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2470478-how-to-do-a-custom-bow-animation
 *
 * @author Choonster
 */
public class LastUseTimeModelItem extends Item {
	public LastUseTimeModelItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player playerIn, final InteractionHand hand) {
		return InteractionResult.SUCCESS;
	}
}
