package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An item that records how many times it's used to right-click an entity.
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
		return stack.getOrDefault(ModDataComponents.ENTITY_INTERACTION_COUNT.get(), 0);
	}

	@Override
	public InteractionResult interactLivingEntity(final ItemStack stack, final Player player, final LivingEntity target, final InteractionHand hand) {
		if (!player.level().isClientSide() && player instanceof final ServerPlayer serverPlayer) {
			final var count = getInteractCount(stack) + 1;
			stack.set(ModDataComponents.ENTITY_INTERACTION_COUNT.get(), count);

			serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_ENTITY_INTERACT_COUNT.getTranslationKey(), count));
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public InteractionResult use(final Level world, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);

		if (!player.level().isClientSide() && player instanceof final ServerPlayer serverPlayer) {
			final var count = getInteractCount(heldItem);

			serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_ENTITY_INTERACT_COUNT.getTranslationKey(), count));
		}

		return InteractionResult.SUCCESS;
	}
}
