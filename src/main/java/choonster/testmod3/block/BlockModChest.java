package choonster.testmod3.block;

import choonster.testmod3.tileentity.TileEntityModChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A chest block.
 *
 * @author Choonster
 */
public class BlockModChest extends BlockTileEntity<TileEntityModChest> {
	/**
	 * The chest's bounding box.
	 */
	private static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);

	public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public BlockModChest() {
		super(Material.WOOD, true);
		setHardness(2.5F);
		setSoundType(SoundType.WOOD);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	public TileEntity createTileEntity(final World world, final IBlockState state) {
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

	@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!worldIn.isRemote && !isBlocked(worldIn, pos)) {
			final TileEntityModChest tileEntity = getTileEntity(worldIn, pos);
			if (tileEntity != null) {
				tileEntity.openGUI(worldIn, playerIn);
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	/**
	 * Is the chest at the specified position blocked?
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return Is the chest blocked?
	 */
	private boolean isBlocked(final World worldIn, final BlockPos pos) {
		return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
	}

	/**
	 * Is the chest at the specified position below a solid block?
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return Is the chest below a solid block?
	 */
	private boolean isBelowSolidBlock(final World worldIn, final BlockPos pos) {
		return worldIn.getBlockState(pos.up()).doesSideBlockChestOpening(worldIn, pos.up(), EnumFacing.DOWN);
	}

	/**
	 * Is an Ocelot sitting on the chest at the specified position?
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return Is an Ocelot sitting on the chest?
	 */
	private boolean isOcelotSittingOnChest(final World worldIn, final BlockPos pos) {
		for (final EntityOcelot entityOcelot : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
			if (entityOcelot.isSitting()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState withRotation(final IBlockState state, final Rotation rotation) {
		return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}

	@Override
	public boolean removedByPlayer(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final boolean willHarvest) {
		//If it will harvest, delay deletion of the block until after getDrops
		return willHarvest || super.removedByPlayer(state, world, pos, player, false);
	}

	@Override
	public void harvestBlock(final World world, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}

	@Override
	public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
		super.getDrops(drops, world, pos, state, fortune);

		final TileEntityModChest tileEntity = getTileEntity(world, pos);
		if (tileEntity != null) {
			drops.addAll(tileEntity.getDrops());
		}
	}
}
