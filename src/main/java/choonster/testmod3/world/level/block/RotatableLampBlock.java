package choonster.testmod3.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

/**
 * A rotatable lamp block.
 * <p>
 * Example for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,33716.0.html
 *
 * @author Choonster
 */
public class RotatableLampBlock extends Block {
	public static final Property<Direction> FACING = DirectionProperty.create("facing", facing -> true);
	public static final Property<Boolean> LIT = BooleanProperty.create("lit");

	public RotatableLampBlock(final Block.Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		final BlockState newState;

		if (player.isShiftKeyDown()) {
			newState = state.cycle(FACING); // Cycle the facing (down -> up -> north -> south -> west -> east -> down)
		} else {
			newState = state.cycle(LIT); // Cycle the lit state (true -> false -> true)
		}

		world.setBlockAndUpdate(pos, newState);

		return InteractionResult.SUCCESS;
	}

	@Override
	public int getLightEmission(final BlockState state, final BlockGetter world, final BlockPos pos) {
		return state.getValue(LIT) ? 15 : 0;
	}
}
