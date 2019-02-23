package choonster.testmod3.block;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
	public static final IProperty<Boolean> HAS_ENDER_EYE = PropertyBool.create("has_ender_eye");

	public BlockRightClickTest() {
		super(Material.GLASS, false);
		setDefaultState(blockState.getBaseState().withProperty(HAS_ENDER_EYE, false));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HAS_ENDER_EYE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(HAS_ENDER_EYE, meta > 0);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(HAS_ENDER_EYE) ? 1 : 0;
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);

		if (!heldItem.isEmpty() && heldItem.getItem() == Items.ENDER_EYE && !state.getValue(HAS_ENDER_EYE)) {
			if (!playerIn.capabilities.isCreativeMode) {
				heldItem.shrink(1);
			}

			worldIn.setBlockState(pos, state.withProperty(HAS_ENDER_EYE, true));
			return true;
		}

		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public int quantityDropped(final Random random) {
		return 1;
	}

	@Override
	public int damageDropped(final IBlockState state) {
		return 0;
	}

	@Override
	public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		super.getDrops(drops, world, pos, state, fortune);

		if (state.getValue(HAS_ENDER_EYE)) {
			drops.add(new ItemStack(Items.ENDER_EYE));
		}
	}
}
