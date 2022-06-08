package choonster.testmod3.world.level.block;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.entity.RestrictedFluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * A Fluid Tank block that can have access enabled and disabled for each facing when right-clicked with a stick.
 * <p>
 * When left-clicked, it will tell the player which sides are enabled.
 *
 * @author Choonster
 */
public class RestrictedFluidTankBlock extends FluidTankBlock<RestrictedFluidTankBlockEntity> {
	public RestrictedFluidTankBlock(final Block.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return new RestrictedFluidTankBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		final ItemStack heldItem = player.getItemInHand(hand);
		final Direction direction = rayTraceResult.getDirection();

		if (heldItem.getItem() == Items.STICK) {
			final RestrictedFluidTankBlockEntity blockEntity = getBlockEntity(level, pos);
			if (blockEntity != null) {
				final boolean enabled = blockEntity.toggleFacing(direction);

				if (!level.isClientSide) {
					final TestMod3Lang message = enabled ? TestMod3Lang.MESSAGE_FLUID_TANK_RESTRICTED_FACING_ENABLED : TestMod3Lang.MESSAGE_FLUID_TANK_RESTRICTED_FACING_DISABLED;
					player.sendSystemMessage(Component.translatable(message.getTranslationKey(), direction));
				}

				return InteractionResult.SUCCESS;
			}
		}

		return super.use(state, level, pos, player, hand, rayTraceResult);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void attack(final BlockState state, final Level level, final BlockPos pos, final Player player) {
		if (!level.isClientSide) {
			final String enabledFacingsString = getEnabledFacingsString(level, pos);
			player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_FLUID_TANK_RESTRICTED_ENABLED_FACINGS.getTranslationKey(), enabledFacingsString));
		}
	}

	/**
	 * Get the enabled facings for the tank at the specified position as a string.
	 *
	 * @param level The level
	 * @param pos   The position
	 * @return The enabled facings string
	 */
	public String getEnabledFacingsString(final LevelReader level, final BlockPos pos) {
		final RestrictedFluidTankBlockEntity blockEntity = getBlockEntity(level, pos);

		if (blockEntity != null) {
			final Set<Direction> enabledFacings = blockEntity.getEnabledFacings();
			return enabledFacings.stream()
					.sorted()
					.map(Direction::getSerializedName)
					.collect(Collectors.joining(", "));
		}

		return "";
	}
}
