package choonster.testmod3.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * An item that records how many times it's used to right click an entity.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2451199-1-8-iteminteractionforentity-with-nbt-bug
 *
 * @author Choonster
 */
public class ItemEntityInteractionTest extends Item {
	public ItemEntityInteractionTest(final Item.Properties properties) {
		super(properties);
	}

	private int getInteractCount(final ItemStack stack) {
		return stack.getOrCreateTag().getInt("Count");
	}

	@Override
	public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer player, final EntityLivingBase target, final EnumHand hand) {
		if (!player.world.isRemote) {
			final int count = getInteractCount(stack) + 1;
			stack.getTag().putInt("Count", count);

			player.sendMessage(new TextComponentTranslation("message.testmod3:entity_interact_count", count));
		}

		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!player.world.isRemote) {
			final int count = getInteractCount(heldItem);

			player.sendMessage(new TextComponentTranslation("message.testmod3:entity_interact_count", count));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}
}
