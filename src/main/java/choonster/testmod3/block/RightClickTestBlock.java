package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * A Block that changes state when right clicked with an Eye of Ender.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32685.0.html
 *
 * @author Choonster
 */
public class RightClickTestBlock extends GlassBlock {
	public static final Property<Boolean> HAS_ENDER_EYE = BooleanProperty.create("has_ender_eye");

	public RightClickTestBlock(final Block.Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(HAS_ENDER_EYE, false));
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HAS_ENDER_EYE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!heldItem.isEmpty() && heldItem.getItem() == Items.ENDER_EYE && !state.get(HAS_ENDER_EYE)) {
			if (!player.isCreative()) {
				heldItem.shrink(1);
			}

			world.setBlockState(pos, state.with(HAS_ENDER_EYE, true));
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.FAIL;
	}
}
