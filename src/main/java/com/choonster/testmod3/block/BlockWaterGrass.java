package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Tall grass that renders with water around it while in water.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,32280.0.html
 */
public class BlockWaterGrass extends BlockBush {
	private static final double RENDER_TEMPERATURE = 0.5, RENDER_HUMIDITY = 1.0;

	public BlockWaterGrass() {
		super(Material.water);
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("watergrass");
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
		setDefaultState(blockState.getBaseState().withProperty(BlockLiquid.LEVEL, 15));
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, BlockLiquid.LEVEL);
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

	@SideOnly(Side.CLIENT)
	@Override
	public int getBlockColor() {
		return ColorizerGrass.getGrassColor(RENDER_TEMPERATURE, RENDER_HUMIDITY);
	}

	@Override
	public int getRenderColor(IBlockState state) {
		if (state.getBlock() != this) {
			return super.getRenderColor(state);
		} else {
			return ColorizerGrass.getGrassColor(RENDER_TEMPERATURE, RENDER_HUMIDITY);
		}
	}

	@Override
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return worldIn.getBiomeGenForCoords(pos).getGrassColorAtPos(pos);
	}
}
