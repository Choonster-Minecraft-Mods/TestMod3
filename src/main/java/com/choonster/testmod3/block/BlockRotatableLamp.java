package com.choonster.testmod3.block;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A rotatable lamp block.
 * <p>
 * Example for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,33716.0.html
 *
 * @author Choonster
 */
public class BlockRotatableLamp extends BlockTestMod3 {
	public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
	public static final IProperty<Boolean> LIT = PropertyBool.create("lit");

	public BlockRotatableLamp() {
		super(Material.REDSTONE_LIGHT, "rotatableLamp");
		setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LIT, false));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, LIT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		final int facingIndex = state.getValue(FACING).getIndex(); // Convert the EnumFacing to its index
		final int lit = state.getValue(LIT) ? 1 : 0; // Convert the lit state boolean to 1 or 0

		// Shift lit left three bits so it occupies the highest bit then OR it with the facing index (which occupies the lowest three bits)
		return lit << 3 | facingIndex;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta) {
		final int facingIndex = meta & 7; // Extract the facing index (lowest three bits)
		final EnumFacing facing = EnumFacing.getFront(facingIndex); // Convert it to the corresponding EnumFacing

		final boolean lit = (meta & 8) != 0; // Extract the lit state (highest bit)

		return getDefaultState().withProperty(FACING, facing).withProperty(LIT, lit);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		final EnumFacing newFacing = BlockPistonBase.getFacingFromEntity(pos, placer);

		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, newFacing);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		final IBlockState newState;

		if (playerIn.isSneaking()) {
			newState = state.cycleProperty(FACING); // Cycle the facing (down -> up -> north -> south -> west -> east -> down)
		} else {
			newState = state.cycleProperty(LIT); // Cycle the lit state (true -> false -> true)
		}

		worldIn.setBlockState(pos, newState);

		return true;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(LIT) ? 15 : 0;
	}
}
