package choonster.testmod3.item;

import choonster.testmod3.Logger;
import choonster.testmod3.util.ItemStackUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.List;

/**
 * An item that lists the entities within the specified horizontal square radius of either the block's northwest corner
 * or its edges (depending on the current mode) when right clicked on a block.
 * <p>
 * Right click air to increase the radius, sneak-right click in air to decrease the radius.
 * <p>
 * Left click an entity to toggle corner mode on or off.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/57877-1112-unsolved-accessing-an-entitys-gui-container/
 *
 * @author Choonster
 */
public class ItemEntityChecker extends ItemTestMod3 {
	public ItemEntityChecker() {
		super("entity_checker");
	}

	/**
	 * Get the search radius from the {@link ItemStack}.
	 *
	 * @param stack The ItemStack
	 * @return The search radius
	 */
	private int getRadius(final ItemStack stack) {
		final NBTTagCompound tagCompound = ItemStackUtils.getOrCreateTagCompound(stack);

		return tagCompound.getInteger("Radius");
	}

	/**
	 * Increment the search radius for the {@link ItemStack} by the specified amount and return the new radius.
	 *
	 * @param stack The ItemStack
	 * @return The new radius
	 */
	private int incrementRadius(final ItemStack stack, final int amount) {
		final NBTTagCompound tagCompound = ItemStackUtils.getOrCreateTagCompound(stack);

		final int newRadius = Math.max(tagCompound.getInteger("Radius") + amount, 0); // Don't allow negative values
		tagCompound.setInteger("Radius", newRadius);

		return newRadius;
	}

	/**
	 * Is corner mode enabled for the {@link ItemStack}?
	 *
	 * @param stack The ItemStack
	 * @return Is corner mode enabled?
	 */
	private boolean isCornerModeEnabled(final ItemStack stack) {
		final NBTTagCompound tagCompound = ItemStackUtils.getOrCreateTagCompound(stack);

		return tagCompound.getBoolean("CornerMode");
	}

	/**
	 * Toggle whether corner mode is enabled for the {@link ItemStack} and return the new setting.
	 *
	 * @param stack The ItemStack
	 * @return The new corner mode setting
	 */
	private boolean toggleCornerModeEnabled(final ItemStack stack) {
		final NBTTagCompound tagCompound = ItemStackUtils.getOrCreateTagCompound(stack);

		final boolean cornerModeEnabled = !tagCompound.getBoolean("CornerMode");
		tagCompound.setBoolean("CornerMode", cornerModeEnabled);

		return cornerModeEnabled;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!world.isRemote) {
			final int newRadius = incrementRadius(heldItem, player.isSneaking() ? -1 : 1);
			player.sendMessage(new TextComponentTranslation("message.testmod3:entity_checker.radius", newRadius));
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity) {
		if (!player.getEntityWorld().isRemote) {
			final boolean cornerModeEnabled = toggleCornerModeEnabled(stack);
			final String message = cornerModeEnabled ? "message.testmod3:entity_checker.mode.corner" : "message.testmod3:entity_checker.mode.edge";
			player.sendMessage(new TextComponentTranslation(message, cornerModeEnabled));
		}

		return true;
	}

	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		if (!worldIn.isRemote) {
			final ItemStack heldItem = player.getHeldItem(hand);

			final int radius = getRadius(heldItem);
			final AxisAlignedBB boundingBox;

			// Create the AABB based on whether or not corner mode is enabled.
			// The AABB will always have the block's y coordinate minus 1 as the minimum coordinate and the block's y coordinate plus 2 as the maximum coordinate.
			if (isCornerModeEnabled(heldItem)) {
				// In corner mode, use the block's x and z coordinates as both the minimum and maximum coordinates of the AABB.
				boundingBox = new AxisAlignedBB(pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX(), pos.getY() + 2, pos.getZ()).expand(radius, 0, radius);
			} else {
				// In edge mode, use the block's x and z coordinates as the minimum coordinates of the AABB and the block's x and z coordinates plus 1 as the maximum coordinates.
				boundingBox = new AxisAlignedBB(pos).expand(radius, 1, radius);
			}

			final List<Entity> entities = worldIn.getEntitiesWithinAABBExcludingEntity(player, boundingBox);

			Logger.info("Bounding box: %s", boundingBox);
			player.sendMessage(new TextComponentTranslation("message.testmod3:entity_checker.results", entities.size()));
			entities.forEach(entity -> player.sendMessage(new TextComponentString(entity.toString())));
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
		tooltip.add(I18n.format("item.testmod3:entity_checker.radius.desc", getRadius(stack)));

		final String cornerModeTranslationKey = isCornerModeEnabled(stack) ? "item.testmod3:entity_checker.mode.corner.desc" : "item.testmod3:entity_checker.mode.edge.desc";
		tooltip.add(I18n.format(cornerModeTranslationKey));
	}
}
