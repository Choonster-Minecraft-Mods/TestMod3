package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
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
import net.minecraft.util.Util;
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
	private static int getRadius(final ItemStack stack) {
		return stack.getOrCreateTag().getInt("Radius");
	}

	/**
	 * Increment the search radius for the {@link ItemStack} by the specified amount and return the new radius.
	 *
	 * @param stack The ItemStack
	 * @return The new radius
	 */
	private static int incrementRadius(final ItemStack stack, final int amount) {
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
	private static boolean isCornerModeEnabled(final ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("CornerMode");
	}

	/**
	 * Toggle whether corner mode is enabled for the {@link ItemStack} and return the new setting.
	 *
	 * @param stack The ItemStack
	 * @return The new corner mode setting
	 */
	private static boolean toggleCornerModeEnabled(final ItemStack stack) {
		final CompoundNBT tag = stack.getOrCreateTag();

		final boolean cornerModeEnabled = !tag.getBoolean("CornerMode");
		tag.putBoolean("CornerMode", cornerModeEnabled);

		return cornerModeEnabled;
	}

	@Override
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!world.isClientSide) {
			final int newRadius = incrementRadius(heldItem, player.isShiftKeyDown() ? -1 : 1);
			player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_ENTITY_CHECKER_RADIUS.getTranslationKey(), newRadius), Util.NIL_UUID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final PlayerEntity player, final Entity entity) {
		if (!player.getCommandSenderWorld().isClientSide) {
			final boolean cornerModeEnabled = toggleCornerModeEnabled(stack);
			final TestMod3Lang message = cornerModeEnabled ? TestMod3Lang.MESSAGE_ENTITY_CHECKER_MODE_CORNER : TestMod3Lang.MESSAGE_ENTITY_CHECKER_MODE_EDGE;
			player.sendMessage(new TranslationTextComponent(message.getTranslationKey(), cornerModeEnabled), Util.NIL_UUID);
		}

		return true;
	}

	@Override
	public ActionResultType useOn(final ItemUseContext context) {
		if (!context.getLevel().isClientSide) {
			final PlayerEntity player = context.getPlayer();
			final ItemStack heldItem = context.getItemInHand();
			final BlockPos clickedPos = context.getClickedPos();

			final int radius = getRadius(heldItem);
			final AxisAlignedBB boundingBox;

			// Create the AABB based on whether or not corner mode is enabled.
			// The AABB will always have the block's y coordinate minus 1 as the minimum coordinate and the block's y coordinate plus 2 as the maximum coordinate.
			if (isCornerModeEnabled(heldItem)) {
				// In corner mode, use the block's x and z coordinates as both the minimum and maximum coordinates of the AABB.
				boundingBox = new AxisAlignedBB(clickedPos.getX(), clickedPos.getY() - 1, clickedPos.getZ(), clickedPos.getX(), clickedPos.getY() + 2, clickedPos.getZ()).expandTowards(radius, 0, radius);
			} else {
				// In edge mode, use the block's x and z coordinates as the minimum coordinates of the AABB and the block's x and z coordinates plus 1 as the maximum coordinates.
				boundingBox = new AxisAlignedBB(clickedPos).expandTowards(radius, 1, radius);
			}

			final List<Entity> entities = context.getLevel().getEntities(player, boundingBox);

			LOGGER.info("Bounding box: {}", boundingBox);
			if (player != null) {
				player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_ENTITY_CHECKER_RESULTS.getTranslationKey(), entities.size()), Util.NIL_UUID);
				entities.forEach(entity -> player.sendMessage(new StringTextComponent(entity.toString()), Util.NIL_UUID));
			}
		}

		return ActionResultType.SUCCESS;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(final ItemStack stack, @Nullable final World worldIn, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent(TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_RADIUS.getTranslationKey(), getRadius(stack)));

		final TestMod3Lang cornerMode = isCornerModeEnabled(stack) ? TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_MODE_CORNER : TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_MODE_EDGE;
		tooltip.add(new TranslationTextComponent(cornerMode.getTranslationKey()));
	}
}
