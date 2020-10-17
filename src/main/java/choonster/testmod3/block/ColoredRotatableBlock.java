package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
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
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getNearestLookingDirection());
	}

	private boolean recolorBlock(final BlockState currentState, final IWorld world, final BlockPos pos, final Direction facing, final DyeColor color) {
		final BlockState newState = copyState(currentState, getVariantGroup().getBlock(color).get().getDefaultState());

		world.setBlockState(pos, newState, Constants.BlockFlags.DEFAULT_FLAGS);

		return true;
	}

	protected BlockState copyState(final BlockState currentState, final BlockState newState) {
		return newState.with(FACING, currentState.get(FACING));
	}

	@Override
	public BlockState rotate(final BlockState state, final IWorld world, final BlockPos pos, final Rotation direction) {
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.with(FACING, mirror.mirror(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!heldItem.isEmpty()) { // If the player is holding dye, change the colour
			final DyeColor dyeColour = DyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final boolean success = recolorBlock(state, world, pos, rayTraceResult.getFace(), dyeColour);
				if (success) {
					heldItem.shrink(1);
					return ActionResultType.SUCCESS;
				}
			}

			return ActionResultType.FAIL;
		} else { // Else rotate the block
			world.setBlockState(pos, rotate(state, world, pos, Rotation.CLOCKWISE_90));

			return ActionResultType.SUCCESS;
		}
	}
}
