package choonster.testmod3.world.level.block;

import choonster.testmod3.world.level.block.variantgroup.IBlockVariantGroup;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Base class for coloured rotatable blocks.
 *
 * @author Choonster
 */
public abstract class BaseColoredRotatableBlock<T extends BaseColoredRotatableBlock<T>> extends Block {
	public static final Property<Direction> FACING = BlockStateProperties.FACING;

	protected final DyeColor color;
	protected final Supplier<IBlockVariantGroup<DyeColor, T>> variantGroup;
	protected final MapCodec<IBlockVariantGroup<DyeColor, T>> variantGroupMapCodec;

	public BaseColoredRotatableBlock(
			final DyeColor color,
			final Supplier<IBlockVariantGroup<DyeColor, T>> variantGroup,
			final MapCodec<IBlockVariantGroup<DyeColor, T>> variantGroupMapCodec,
			final Properties properties
	) {
		super(properties);
		this.color = color;
		this.variantGroup = variantGroup;
		this.variantGroupMapCodec = variantGroupMapCodec;
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public DyeColor getColor() {
		return color;
	}

	public IBlockVariantGroup<DyeColor, T> getVariantGroup() {
		return variantGroup.get();
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
	}

	private boolean recolorBlock(final BlockState currentState, final LevelAccessor world, final BlockPos pos, final DyeColor color) {
		final var newBlock = Objects.requireNonNull(getVariantGroup().getBlock(color));
		final var newState = copyState(currentState, newBlock.get().defaultBlockState());

		world.setBlock(pos, newState, Block.UPDATE_ALL);

		return true;
	}

	protected BlockState copyState(final BlockState currentState, final BlockState newState) {
		return newState.setValue(FACING, currentState.getValue(FACING));
	}

	@Override
	public BlockState rotate(final BlockState state, final LevelAccessor world, final BlockPos pos, final Rotation direction) {
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@Override
	protected ItemInteractionResult useItemOn(
			final ItemStack heldItem,
			final BlockState state,
			final Level level,
			final BlockPos pos,
			final Player player,
			final InteractionHand hand,
			final BlockHitResult blockHitResult
	) {
		if (!heldItem.isEmpty()) { // If the player is holding dye, change the colour
			final var dyeColour = DyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final var success = recolorBlock(state, level, pos, dyeColour);
				if (success) {
					heldItem.shrink(1);
					return ItemInteractionResult.SUCCESS;
				}
			}

			return ItemInteractionResult.FAIL;
		} else { // Else rotate the block
			level.setBlockAndUpdate(pos, rotate(state, level, pos, Rotation.CLOCKWISE_90));

			return ItemInteractionResult.SUCCESS;
		}
	}
}
