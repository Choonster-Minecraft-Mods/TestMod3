package choonster.testmod3.item;

import choonster.testmod3.util.ItemStackUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
public class ItemEntityInteractionTest extends ItemTestMod3 {
	public ItemEntityInteractionTest() {
		super("entity_interaction_test");
	}

	private int getInteractCount(final ItemStack stack) {
		return ItemStackUtils.getOrCreateTagCompound(stack).getInteger("Count");
	}

	@Override
	public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target, final EnumHand hand) {
		if (!playerIn.world.isRemote) {
			final int count = getInteractCount(stack) + 1;
			stack.getTagCompound().setInteger("Count", count);

			playerIn.sendMessage(new TextComponentTranslation("message.testmod3:entity_interact_count", count));
		}

		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);

		if (!playerIn.world.isRemote) {
			final int count = getInteractCount(heldItem);

			playerIn.sendMessage(new TextComponentTranslation("message.testmod3:entity_interact_count", count));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}
}
