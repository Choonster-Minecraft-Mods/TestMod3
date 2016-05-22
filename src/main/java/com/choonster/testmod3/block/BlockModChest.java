package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.client.gui.GuiIDs;
import com.choonster.testmod3.tileentity.TileEntityModChest;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

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
		super(Material.WOOD, "chest");
		setHardness(2.5F);
		setSoundType(SoundType.WOOD);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityModChest();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (stack.hasDisplayName()) {
			getTileEntity(worldIn, pos).setDisplayName(stack.getDisplayName());
		}
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && !isBlocked(worldIn, pos)) {
			playerIn.openGui(TestMod3.instance, GuiIDs.MOD_CHEST, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}

	/**
	 * Is the chest at the specified position blocked?
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return Is the chest blocked?
	 */
	private boolean isBlocked(World worldIn, BlockPos pos) {
		return this.isBelowSolidBlock(worldIn, pos) || this.isOcelotSittingOnChest(worldIn, pos);
	}

	/**
	 * Is the chest at the specified position below a solid block?
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return Is the chest below a solid block?
	 */
	private boolean isBelowSolidBlock(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.up()).isSideSolid(worldIn, pos.up(), EnumFacing.DOWN);
	}

	/**
	 * Is an Ocelot sitting on the chest at the specified position?
	 *
	 * @param worldIn The World
	 * @param pos     The position
	 * @return Is an Ocelot sitting on the chest?
	 */
	private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos) {
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
	public IBlockState withRotation(IBlockState state, Rotation rotation) {
		return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return state.withRotation(mirror.toRotation(state.getValue(FACING)));
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		//If it will harvest, delay deletion of the block until after getDrops
		return willHarvest || super.removedByPlayer(state, world, pos, player, false);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack tool) {
		super.harvestBlock(world, player, pos, state, te, tool);
		world.setBlockToAir(pos);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		final List<ItemStack> drops = super.getDrops(world, pos, state, fortune);

		drops.addAll(getTileEntity(world, pos).getDrops());

		return drops;
	}
}
