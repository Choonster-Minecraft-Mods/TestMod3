package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An item that reveals hidden blocks.
 *
 * @author Choonster
 */
public class HiddenBlockRevealerItem extends Item {
	public HiddenBlockRevealerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
		final var revealHiddenBlocksComponent = ModDataComponents.REVEAL_HIDDEN_BLOCKS.get();
		final var heldItem = player.getItemInHand(hand);

		final boolean revealHiddenBlocks;
		if (heldItem.has(revealHiddenBlocksComponent)) {
			heldItem.remove(revealHiddenBlocksComponent);
			revealHiddenBlocks = false;
		} else {
			heldItem.set(revealHiddenBlocksComponent, Unit.INSTANCE);
			revealHiddenBlocks = true;
		}

		final var message = revealHiddenBlocks ? TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_REVEAL : TestMod3Lang.MESSAGE_HIDDEN_BLOCK_REVEALER_HIDE;
		player.sendSystemMessage(Component.translatable(message.getTranslationKey()));

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}
}
