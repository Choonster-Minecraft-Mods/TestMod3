package choonster.testmod3.world.level.block;

import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

/**
 * A block with 16 colours and 6 facings.
 *
 * @author Choonster
 */
public class ColoredRotatableBlock extends Block {
	public static final Property<Direction> FACING = BlockStateProperties.FACING;

	private final BlockVariantGroup<DyeColor, ? extends ColoredRotatableBlock> variantGroup;
	private final DyeColor color;

	public ColoredRotatableBlock(final DyeColor color, final BlockVariantGroup<DyeColor, ? extends ColoredRotatableBlock> variantGroup, final Block.Properties properties) {
		super(properties);
		this.color = color;
		this.variantGroup = variantGroup;
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public DyeColor getColor() {
		return color;
	}

	public BlockVariantGroup<DyeColor, ? extends ColoredRotatableBlock> getVariantGroup() {
		return variantGroup;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
	}

	private boolean recolorBlock(final BlockState currentState, final LevelAccessor world, final BlockPos pos, final DyeColor color) {
		final BlockState newState = copyState(currentState, getVariantGroup().getBlock(color).get().defaultBlockState());

		world.setBlock(pos, newState, Constants.BlockFlags.DEFAULT);

		return true;
	}

	protected BlockState copyState(final BlockState currentState, final BlockState newState) {
		return newState.setValue(FACING, currentState.getValue(FACING));
	}

	@Override
	public BlockState rotate(final BlockState state, final LevelAccessor world, final BlockPos pos, final Rotation direction) {
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!heldItem.isEmpty()) { // If the player is holding dye, change the colour
			final DyeColor dyeColour = DyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final boolean success = recolorBlock(state, world, pos, dyeColour);
				if (success) {
					heldItem.shrink(1);
					return InteractionResult.SUCCESS;
				}
			}

			return InteractionResult.FAIL;
		} else { // Else rotate the block
			world.setBlockAndUpdate(pos, rotate(state, world, pos, Rotation.CLOCKWISE_90));

			return InteractionResult.SUCCESS;
		}
	}
}
