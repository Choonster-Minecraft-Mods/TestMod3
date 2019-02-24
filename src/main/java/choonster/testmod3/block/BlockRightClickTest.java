package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * A Block that changes state when right clicked with an Eye of Ender.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32685.0.html
 *
 * @author Choonster
 */
public class BlockRightClickTest extends BlockGlass {
	public static final IProperty<Boolean> HAS_ENDER_EYE = BooleanProperty.create("has_ender_eye");

	public BlockRightClickTest(final Block.Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(HAS_ENDER_EYE, false));
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(HAS_ENDER_EYE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!heldItem.isEmpty() && heldItem.getItem() == Items.ENDER_EYE && !state.get(HAS_ENDER_EYE)) {
			if (!player.isCreative()) {
				heldItem.shrink(1);
			}

			world.setBlockState(pos, state.with(HAS_ENDER_EYE, true));
			return true;
		}

		return super.onBlockActivated(state, world, pos, player, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public int quantityDropped(final IBlockState state, final Random random) {
		return 1;
	}

	@Override
	public void getDrops(final IBlockState state, final NonNullList<ItemStack> drops, final World world, final BlockPos pos, final int fortune) {
		super.getDrops(state, drops, world, pos, fortune);

		if (state.get(HAS_ENDER_EYE)) {
			drops.add(new ItemStack(Items.ENDER_EYE));
		}
	}
}
