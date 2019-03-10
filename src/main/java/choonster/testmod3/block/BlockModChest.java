package choonster.testmod3.block;

import choonster.testmod3.tileentity.TileEntityModChest;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A chest block.
 *
 * @author Choonster
 */
public class BlockModChest extends BlockTileEntity<TileEntityModChest> {
	/**
	 * The chest's shape.
	 */
	private static final VoxelShape SHAPE = makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

	public static final IProperty<EnumFacing> FACING = DirectionProperty.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockModChest(final Block.Properties properties) {
		super(true, properties);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final IBlockState state, final IBlockReader worldIn, final BlockPos pos) {
		return SHAPE;
	}

	@Override
	public TileEntity createTileEntity(final IBlockState state, final IBlockReader world) {
		return new TileEntityModChest();
	}

	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
		if (stack.hasDisplayName()) {
			final TileEntityModChest tileEntity = getTileEntity(worldIn, pos);
			if (tileEntity != null) {
				tileEntity.setDisplayName(stack.getDisplayName());
			}
		}
	}

	@Nullable
	@Override
	public IBlockState getStateForPlacement(final BlockItemUseContext context) {
		return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!world.isRemote && !isBlocked(world, pos)) {
			final TileEntityModChest tileEntity = getTileEntity(world, pos);
			if (tileEntity != null) {
				tileEntity.openGUI(world, player);
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	/**
	 * Is the chest at the specified position blocked?
	 *
	 * @param world The World
	 * @param pos   The position
	 * @return Is the chest blocked?
	 */
	private boolean isBlocked(final World world, final BlockPos pos) {
		return isBelowSolidBlock(world, pos) || isOcelotSittingOnChest(world, pos);
	}

	/**
	 * Is the chest at the specified position below a solid block?
	 *
	 * @param world The World
	 * @param pos   The position
	 * @return Is the chest below a solid block?
	 */
	private boolean isBelowSolidBlock(final World world, final BlockPos pos) {
		return world.getBlockState(pos.up()).doesSideBlockChestOpening(world, pos.up(), EnumFacing.DOWN);
	}

	/**
	 * Is an Ocelot sitting on the chest at the specified position?
	 *
	 * @param world The World
	 * @param pos   The position
	 * @return Is an Ocelot sitting on the chest?
	 */
	private boolean isOcelotSittingOnChest(final World world, final BlockPos pos) {
		for (final EntityOcelot entityOcelot : world.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
			if (entityOcelot.isSitting()) {
				return true;
			}
		}

		return false;
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

	@Override
	public void getDrops(final IBlockState state, final NonNullList<ItemStack> drops, final World world, final BlockPos pos, final int fortune) {
		super.getDrops(state, drops, world, pos, fortune);

		final TileEntityModChest tileEntity = getTileEntity(world, pos);
		if (tileEntity != null) {
			drops.addAll(tileEntity.getDrops());
		}
	}
}
