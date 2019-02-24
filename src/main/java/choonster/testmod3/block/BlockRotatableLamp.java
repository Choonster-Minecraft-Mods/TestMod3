package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
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
public class BlockRotatableLamp extends Block {
	public static final IProperty<EnumFacing> FACING = DirectionProperty.create("facing");
	public static final IProperty<Boolean> LIT = BooleanProperty.create("lit");

	public BlockRotatableLamp(final Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(FACING, EnumFacing.NORTH).with(LIT, false));
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING, LIT);
	}

	@Nullable
	@Override
	public IBlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getNearestLookingDirection());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World worldIn, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final IBlockState newState;

		if (player.isSneaking()) {
			newState = state.cycle(FACING); // Cycle the facing (down -> up -> north -> south -> west -> east -> down)
		} else {
			newState = state.cycle(LIT); // Cycle the lit state (true -> false -> true)
		}

		worldIn.setBlockState(pos, newState);

		return true;
	}

	@Override
	public int getLightValue(final IBlockState state, final IWorldReader world, final BlockPos pos) {
		return state.get(LIT) ? 15 : 0;
	}
}
