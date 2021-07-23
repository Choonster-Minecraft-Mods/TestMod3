package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

/**
 * An item that records how many times it's used to right click an entity.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2451199-1-8-iteminteractionforentity-with-nbt-bug
 *
 * @author Choonster
 */
public class EntityInteractionTestItem extends Item {
	public EntityInteractionTestItem(final Item.Properties properties) {
		super(properties);
	}

	private int getInteractCount(final ItemStack stack) {
		return stack.getOrCreateTag().getInt("Count");
	}

	@Override
	public InteractionResult interactLivingEntity(final ItemStack stack, final Player player, final LivingEntity target, final InteractionHand hand) {
		if (!player.level.isClientSide) {
			final int count = getInteractCount(stack) + 1;
			stack.getTag().putInt("Count", count);

			player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_ENTITY_INTERACT_COUNT.getTranslationKey(), count), Util.NIL_UUID);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!player.level.isClientSide) {
			final int count = getInteractCount(heldItem);

			player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_ENTITY_INTERACT_COUNT.getTranslationKey(), count), Util.NIL_UUID);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}
}
