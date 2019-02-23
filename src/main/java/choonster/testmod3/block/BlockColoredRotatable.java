package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;

import java.util.Optional;

/**
 * A block with 16 colours (stored in the metadata) and 6 facings (stored in the TileEntity).
 *
 * @author Choonster
 */
public class BlockColoredRotatable extends Block {
	public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");

	private final BlockVariantGroup<EnumDyeColor, ? extends BlockColoredRotatable> variantGroup;
	private final EnumDyeColor color;

	public BlockColoredRotatable(final EnumDyeColor color, final Material materialIn, final BlockVariantGroup<EnumDyeColor, ? extends BlockColoredRotatable> variantGroup) {
		super(materialIn, MapColor.getBlockColor(color));
		this.color = color;
		this.variantGroup = variantGroup;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	public EnumDyeColor getColor() {
		return color;
	}

	public BlockVariantGroup<EnumDyeColor, ? extends BlockColoredRotatable> getVariantGroup() {
		return variantGroup;
	}

	public EnumFacing getFacing(final IBlockAccess world, final BlockPos pos) {
		return world.getBlockState(pos).getValue(FACING);
	}

	public void setFacing(final World world, final BlockPos pos, final EnumFacing facing) {
		world.setBlockState(pos, getDefaultState().withProperty(FACING, facing));
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
		setFacing(worldIn, pos, EnumFacing.getDirectionFromEntityLiving(pos, placer));
	}

	@Override
	public boolean recolorBlock(final World world, final BlockPos pos, final EnumFacing side, final EnumDyeColor color) {
		final IBlockState currentState = world.getBlockState(pos);
		final Block newBlock = getVariantGroup().getBlock(color);

		world.setBlockState(pos, newBlock.getDefaultState().withProperty(FACING, currentState.getValue(FACING)));

		return true;
	}

	@Override
	public boolean rotateBlock(final World world, final BlockPos pos, final EnumFacing axis) {
		final EnumFacing facing = getFacing(world, pos);
		setFacing(world, pos, facing.rotateAround(axis.getAxis()));

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState withRotation(final IBlockState state, final Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final ItemStack heldItem = playerIn.getHeldItem(hand);

		if (!heldItem.isEmpty()) { // If the player is holding dye, change the colour
			final Optional<EnumDyeColor> dyeColour = DyeUtils.colorFromStack(heldItem);
			if (dyeColour.isPresent()) {
				final boolean success = recolorBlock(worldIn, pos, side, dyeColour.get());
				if (success) {
					heldItem.shrink(1);
					return true;
				}
			}

			return false;
		} else { // Else rotate the block
			return rotateBlock(worldIn, pos, side);
		}
	}
}
