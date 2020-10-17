package choonster.testmod3.item;

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
	public ActionResultType itemInteractionForEntity(final ItemStack stack, final PlayerEntity player, final LivingEntity target, final Hand hand) {
		if (!player.world.isRemote) {
			final int count = getInteractCount(stack) + 1;
			stack.getTag().putInt("Count", count);

			player.sendMessage(new TranslationTextComponent("message.testmod3.entity_interact_count", count), Util.DUMMY_UUID);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!player.world.isRemote) {
			final int count = getInteractCount(heldItem);

			player.sendMessage(new TranslationTextComponent("message.testmod3.entity_interact_count", count), Util.DUMMY_UUID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}
}
