package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.slf4j.Logger;

import java.util.List;

/**
 * An item that lists the entities within the specified horizontal square radius of either the block's northwest corner
 * or its edges (depending on the current mode) when right-clicked on a block.
 * <p>
 * Right-click air to increase the radius, sneak-right click in air to decrease the radius.
 * <p>
 * Left-click an entity to toggle corner mode on or off.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/57877-1112-unsolved-accessing-an-entitys-gui-container/
 *
 * @author Choonster
 */
public class EntityCheckerItem extends Item {
	private static final Logger LOGGER = LogUtils.getLogger();

	public EntityCheckerItem(final Item.Properties properties) {
		super(properties);
	}

	private static EntityCheckerProperties getProperties(final ItemStack stack) {
		return stack.getOrDefault(
				ModDataComponents.ENTITY_CHECKER_PROPERTIES.get(),
				EntityCheckerProperties.DEFAULT
		);
	}

	private static void setProperties(final ItemStack stack, final EntityCheckerProperties properties) {
		stack.set(ModDataComponents.ENTITY_CHECKER_PROPERTIES.get(), properties);
	}

	/**
	 * Increment the search radius for the {@link ItemStack} by the specified amount and return the new radius.
	 *
	 * @param stack The ItemStack
	 * @return The new radius
	 */
	private static int incrementRadius(final ItemStack stack, final int amount) {
		final var properties = getProperties(stack);

		final var newRadius = Math.max(properties.radius + amount, 0); // Don't allow negative values
		final var newProperties = new EntityCheckerProperties(newRadius, properties.cornerModeEnabled);

		setProperties(stack, newProperties);

		return newRadius;
	}

	/**
	 * Toggle whether corner mode is enabled for the {@link ItemStack} and return the new setting.
	 *
	 * @param stack The ItemStack
	 * @return The new corner mode setting
	 */
	private static boolean toggleCornerModeEnabled(final ItemStack stack) {
		final var properties = getProperties(stack);

		final var cornerModeEnabled = !properties.cornerModeEnabled;
		final var newProperties = new EntityCheckerProperties(properties.radius, cornerModeEnabled);

		setProperties(stack, newProperties);

		return cornerModeEnabled;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);

		if (!world.isClientSide) {
			final var newRadius = incrementRadius(heldItem, player.isShiftKeyDown() ? -1 : 1);
			player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_ENTITY_CHECKER_RADIUS.getTranslationKey(), newRadius));
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final Player player, final Entity entity) {
		if (!player.getCommandSenderWorld().isClientSide) {
			final var cornerModeEnabled = toggleCornerModeEnabled(stack);
			final var message = cornerModeEnabled ? TestMod3Lang.MESSAGE_ENTITY_CHECKER_MODE_CORNER : TestMod3Lang.MESSAGE_ENTITY_CHECKER_MODE_EDGE;
			player.sendSystemMessage(Component.translatable(message.getTranslationKey(), cornerModeEnabled));
		}

		return true;
	}

	@Override
	public InteractionResult useOn(final UseOnContext context) {
		if (!context.getLevel().isClientSide) {
			final var player = context.getPlayer();
			final var heldItem = context.getItemInHand();
			final var clickedPos = context.getClickedPos();
			final var properties = getProperties(heldItem);

			final var radius = properties.radius;
			final AABB boundingBox;

			// Create the AABB based on whether corner mode is enabled.
			// The AABB will always have the block's y coordinate minus 1 as the minimum coordinate and the block's y coordinate plus 2 as the maximum coordinate.
			if (properties.cornerModeEnabled) {
				// In corner mode, use the block's x and z coordinates as both the minimum and maximum coordinates of the AABB.
				boundingBox = new AABB(clickedPos.getX(), clickedPos.getY() - 1, clickedPos.getZ(), clickedPos.getX(), clickedPos.getY() + 2, clickedPos.getZ()).expandTowards(radius, 0, radius);
			} else {
				// In edge mode, use the block's x and z coordinates as the minimum coordinates of the AABB and the block's x and z coordinates plus 1 as the maximum coordinates.
				boundingBox = new AABB(clickedPos).expandTowards(radius, 1, radius);
			}

			final var entities = context.getLevel().getEntities(player, boundingBox);

			LOGGER.info("Bounding box: {}", boundingBox);
			if (player != null) {
				player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_ENTITY_CHECKER_RESULTS.getTranslationKey(), entities.size()));
				entities.forEach(entity -> player.sendSystemMessage(Component.literal(entity.toString())));
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final List<Component> tooltip, final TooltipFlag flagIn) {
		final var properties = getProperties(stack);

		tooltip.add(Component.translatable(TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_RADIUS.getTranslationKey(), properties.radius));

		final var cornerMode = properties.cornerModeEnabled ? TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_MODE_CORNER : TestMod3Lang.ITEM_DESC_ENTITY_CHECKER_MODE_EDGE;
		tooltip.add(Component.translatable(cornerMode.getTranslationKey()));
	}

	public record EntityCheckerProperties(int radius, boolean cornerModeEnabled) {
		public static final Codec<EntityCheckerProperties> CODEC = RecordCodecBuilder.create(builder ->
				builder.group(

						Codec.INT
								.fieldOf("radius")
								.forGetter(EntityCheckerProperties::radius),

						Codec.BOOL
								.fieldOf("corner_mode_enabled")
								.forGetter(EntityCheckerProperties::cornerModeEnabled)

				).apply(builder, EntityCheckerProperties::new)
		);

		public static final StreamCodec<RegistryFriendlyByteBuf, EntityCheckerProperties> NETWORK_CODEC = StreamCodec.composite(
				ByteBufCodecs.VAR_INT,
				EntityCheckerProperties::radius,
				ByteBufCodecs.BOOL,
				EntityCheckerProperties::cornerModeEnabled,
				EntityCheckerProperties::new
		);

		public static final EntityCheckerProperties DEFAULT = new EntityCheckerProperties(0, false);
	}
}
