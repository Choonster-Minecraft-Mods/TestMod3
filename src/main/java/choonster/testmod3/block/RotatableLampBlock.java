package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;

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
	public static final IProperty<Direction> FACING = DirectionProperty.create("facing", facing -> true);
	public static final IProperty<Boolean> LIT = BooleanProperty.create("lit");

	public RotatableLampBlock(final Block.Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(LIT, false));
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getNearestLookingDirection());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final BlockState newState;

		if (player.isSneaking()) {
			newState = state.cycle(FACING); // Cycle the facing (down -> up -> north -> south -> west -> east -> down)
		} else {
			newState = state.cycle(LIT); // Cycle the lit state (true -> false -> true)
		}

		world.setBlockState(pos, newState);

		return true;
	}

	@Override
	public int getLightValue(final BlockState state, final IEnviromentBlockReader world, final BlockPos pos) {
		return state.get(LIT) ? 15 : 0;
	}
}
