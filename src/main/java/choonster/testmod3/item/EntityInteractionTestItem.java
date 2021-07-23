package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
	public ActionResultType interactLivingEntity(final ItemStack stack, final PlayerEntity player, final LivingEntity target, final Hand hand) {
		if (!player.level.isClientSide) {
			final int count = getInteractCount(stack) + 1;
			stack.getTag().putInt("Count", count);

			player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_ENTITY_INTERACT_COUNT.getTranslationKey(), count), Util.NIL_UUID);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!player.level.isClientSide) {
			final int count = getInteractCount(heldItem);

			player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_ENTITY_INTERACT_COUNT.getTranslationKey(), count), Util.NIL_UUID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}
}
