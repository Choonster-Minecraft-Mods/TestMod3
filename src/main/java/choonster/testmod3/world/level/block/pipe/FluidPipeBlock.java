package choonster.testmod3.world.level.block.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * A fluid pipe.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34448.0.html
 *
 * @author Choonster
 */
public class FluidPipeBlock extends BasePipeBlock {
	public FluidPipeBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected boolean isValidConnection(final BlockState ownState, final BlockState neighbourState, final LevelReader world, final BlockPos ownPos, final Direction neighbourDirection) {
		// Connect if the neighbouring block is another pipe
		if (super.isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection)) {
			return true;
		}

		final BlockPos neighbourPos = ownPos.relative(neighbourDirection);
		final Block neighbourBlock = neighbourState.getBlock();

		// Connect if the neighbouring block has a BlockEntity with an IFluidHandler for the adjacent face
		if (neighbourBlock instanceof EntityBlock) {
			final BlockEntity blockEntity = world.getBlockEntity(neighbourPos);
			return blockEntity != null && blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, neighbourDirection.getOpposite()).isPresent();
		}

		// Connect if the neighbouring block is a fluid, FluidUtil.getFluidHandler will provide an IFluidHandler wrapper to drain from it
		return neighbourBlock instanceof IFluidBlock || neighbourBlock instanceof LiquidBlock;
	}
}
