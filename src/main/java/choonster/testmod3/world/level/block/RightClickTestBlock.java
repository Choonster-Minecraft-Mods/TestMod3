package choonster.testmod3.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

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
		registerDefaultState(getStateDefinition().any().setValue(HAS_ENDER_EYE, false));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HAS_ENDER_EYE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!heldItem.isEmpty() && heldItem.getItem() == Items.ENDER_EYE && !state.getValue(HAS_ENDER_EYE)) {
			if (!player.isCreative()) {
				heldItem.shrink(1);
			}

			world.setBlockAndUpdate(pos, state.setValue(HAS_ENDER_EYE, true));
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.FAIL;
	}
}
