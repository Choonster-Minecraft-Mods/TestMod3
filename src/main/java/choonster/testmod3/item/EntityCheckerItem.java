package choonster.testmod3.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
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
public class EntityCheckerItem extends Item {
	private static final Logger LOGGER = LogManager.getLogger();

	public EntityCheckerItem(final Item.Properties properties) {
		super(properties);
	}

	/**
	 * Get the search radius from the {@link ItemStack}.
	 *
	 * @param stack The ItemStack
	 * @return The search radius
	 */
	private int getRadius(final ItemStack stack) {
		return stack.getOrCreateTag().getInt("Radius");
	}

	/**
	 * Increment the search radius for the {@link ItemStack} by the specified amount and return the new radius.
	 *
	 * @param stack The ItemStack
	 * @return The new radius
	 */
	private int incrementRadius(final ItemStack stack, final int amount) {
		final CompoundNBT tag = stack.getOrCreateTag();

		final int newRadius = Math.max(tag.getInt("Radius") + amount, 0); // Don't allow negative values
		tag.putInt("Radius", newRadius);

		return newRadius;
	}

	/**
	 * Is corner mode enabled for the {@link ItemStack}?
	 *
	 * @param stack The ItemStack
	 * @return Is corner mode enabled?
	 */
	private boolean isCornerModeEnabled(final ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("CornerMode");
	}

	/**
	 * Toggle whether corner mode is enabled for the {@link ItemStack} and return the new setting.
	 *
	 * @param stack The ItemStack
	 * @return The new corner mode setting
	 */
	private boolean toggleCornerModeEnabled(final ItemStack stack) {
		final CompoundNBT tag = stack.getOrCreateTag();

		final boolean cornerModeEnabled = !tag.getBoolean("CornerMode");
		tag.putBoolean("CornerMode", cornerModeEnabled);

		return cornerModeEnabled;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!world.isRemote) {
			final int newRadius = incrementRadius(heldItem, player.isSneaking() ? -1 : 1);
			player.sendMessage(new TranslationTextComponent("message.testmod3.entity_checker.radius", newRadius));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final PlayerEntity player, final Entity entity) {
		if (!player.getEntityWorld().isRemote) {
			final boolean cornerModeEnabled = toggleCornerModeEnabled(stack);
			final String message = cornerModeEnabled ? "message.testmod3.entity_checker.mode.corner" : "message.testmod3.entity_checker.mode.edge";
			player.sendMessage(new TranslationTextComponent(message, cornerModeEnabled));
		}

		return true;
	}

	@Override
	public ActionResultType onItemUse(final ItemUseContext context) {
		if (!context.getWorld().isRemote) {
			final PlayerEntity player = context.getPlayer();
			final ItemStack heldItem = context.getItem();
			final BlockPos pos = context.getPos();

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

			final List<Entity> entities = context.getWorld().getEntitiesWithinAABBExcludingEntity(player, boundingBox);

			LOGGER.info("Bounding box: {}", boundingBox);
			if (player != null) {
				player.sendMessage(new TranslationTextComponent("message.testmod3.entity_checker.results", entities.size()));
				entities.forEach(entity -> player.sendMessage(new StringTextComponent(entity.toString())));
			}
		}

		return ActionResultType.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("item.testmod3.entity_checker.radius.desc", getRadius(stack)));

		final String cornerModeTranslationKey = isCornerModeEnabled(stack) ? "item.testmod3.entity_checker.mode.corner.desc" : "item.testmod3.entity_checker.mode.edge.desc";
		tooltip.add(new TranslationTextComponent(cornerModeTranslationKey));
	}
}
