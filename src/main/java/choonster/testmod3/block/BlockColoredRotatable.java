package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A block with 16 colours and 6 facings.
 *
 * @author Choonster
 */
public class BlockColoredRotatable extends Block {
	public static final IProperty<EnumFacing> FACING = BlockStateProperties.FACING;

	private final BlockVariantGroup<EnumDyeColor, ? extends BlockColoredRotatable> variantGroup;
	private final EnumDyeColor color;

	public BlockColoredRotatable(final EnumDyeColor color, final BlockVariantGroup<EnumDyeColor, ? extends BlockColoredRotatable> variantGroup, final Block.Properties properties) {
		super(properties);
		this.color = color;
		this.variantGroup = variantGroup;
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING);
	}

	public EnumDyeColor getColor() {
		return color;
	}

	public BlockVariantGroup<EnumDyeColor, ? extends BlockColoredRotatable> getVariantGroup() {
		return variantGroup;
	}

	@Nullable
	@Override
	public IBlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getNearestLookingDirection());
	}

	@Override
	public final boolean recolorBlock(final IBlockState currentState, final IWorld world, final BlockPos pos, final EnumFacing facing, final EnumDyeColor color) {
		final IBlockState newState = copyState(currentState, getVariantGroup().getBlock(color).getDefaultState());

		world.setBlockState(pos, newState, Constants.BlockFlags.DEFAULT_FLAGS);

		return true;
	}

	protected IBlockState copyState(final IBlockState currentState, final IBlockState newState) {
		return newState.with(FACING, currentState.get(FACING));
	}

	public boolean rotateBlock(final World world, final BlockPos pos, final EnumFacing axis) {
		final IBlockState currentState = world.getBlockState(pos);
		final EnumFacing facing = currentState.get(FACING);
		final IBlockState newState = currentState.with(FACING, facing.rotateAround(axis.getAxis()));

		world.setBlockState(pos, newState);

		return true;
	}

	@Override
	public IBlockState rotate(final IBlockState state, final IWorld world, final BlockPos pos, final Rotation direction) {
		return state.with(FACING, direction.rotate(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState mirror(final IBlockState state, final Mirror mirror) {
		return state.with(FACING, mirror.mirror(state.get(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!heldItem.isEmpty()) { // If the player is holding dye, change the colour
			final EnumDyeColor dyeColour = EnumDyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final boolean success = recolorBlock(state, world, pos, side, dyeColour);
				if (success) {
					heldItem.shrink(1);
					return true;
				}
			}

			return false;
		} else { // Else rotate the block
			return rotateBlock(world, pos, side);
		}
	}
}
