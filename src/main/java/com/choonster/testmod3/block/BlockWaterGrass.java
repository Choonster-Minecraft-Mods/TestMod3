package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Tall grass that renders with water around it while in water.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32280.0.html
 *
 * @author Choonster
 */
public class BlockWaterGrass extends BlockBush {
	private static final AxisAlignedBB BOUNDING_BOX;

	static {
		final float size = 0.4F;
		BOUNDING_BOX = new AxisAlignedBB(0.5F - size, 0.0F, 0.5F - size, 0.5F + size, 0.8F, 0.5F + size);
	}

	public BlockWaterGrass() {
		super(Material.water);
		setCreativeTab(TestMod3.creativeTab);
		BlockTestMod3.setBlockName(this, "watergrass");

		setDefaultState(blockState.getBaseState().withProperty(BlockLiquid.LEVEL, 15));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_BOX;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockLiquid.LEVEL);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		return worldIn.getBlockState(pos.up()).getBlock() == Blocks.water && super.canBlockStay(worldIn, pos, state);
	}

	@Override
	public boolean canReplace(World worldIn, BlockPos pos, EnumFacing side, ItemStack stack) {
		return canBlockStay(worldIn, pos, this.getDefaultState());
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockState(pos, Blocks.water.getDefaultState());
	}

	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.water.getDefaultState());
		}
	}
}
