package com.choonster.testmod3.block.fluid;

import com.choonster.testmod3.block.properties.PropertyFloat;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * The fluid rendering setup associated with this class was originally created by kirderf1 for www.github.com/mraof/minestuck
 * When copying this code, please keep this comment or refer back to the original source in another way, if possible.
 */
public class FluidUtils {
	private FluidUtils() {
	}

	public static final PropertyFloat HEIGHT_NW = new PropertyFloat("height_nw", 0F, 1F), HEIGHT_SW = new PropertyFloat("height_sw", 0F, 1F),
			HEIGHT_SE = new PropertyFloat("height_se", 0F, 1F), HEIGHT_NE = new PropertyFloat("height_ne", 0F, 1F);
	public static final PropertyFloat FLOW_DIRECTION = new PropertyFloat("flow_direction");

	public static <T extends BlockFluidBase & IFluidBlockWithModel> IBlockState getExtendedState(T fluidBlock, IBlockState state, IBlockAccess world, BlockPos pos) {

		float heightNW, heightSW, heightSE, heightNE;
		float flow11 = getFluidHeightForRender(fluidBlock, world, pos);

		if (flow11 != 1) {
			float flow00 = getFluidHeightForRender(fluidBlock, world, pos.add(-1, 0, -1));
			float flow01 = getFluidHeightForRender(fluidBlock, world, pos.add(-1, 0, 0));
			float flow02 = getFluidHeightForRender(fluidBlock, world, pos.add(-1, 0, 1));
			float flow10 = getFluidHeightForRender(fluidBlock, world, pos.add(0, 0, -1));
			float flow12 = getFluidHeightForRender(fluidBlock, world, pos.add(0, 0, 1));
			float flow20 = getFluidHeightForRender(fluidBlock, world, pos.add(1, 0, -1));
			float flow21 = getFluidHeightForRender(fluidBlock, world, pos.add(1, 0, 0));
			float flow22 = getFluidHeightForRender(fluidBlock, world, pos.add(1, 0, 1));

			heightNW = getFluidHeightAverage(new float[]{flow00, flow01, flow10, flow11});
			heightSW = getFluidHeightAverage(new float[]{flow01, flow02, flow12, flow11});
			heightSE = getFluidHeightAverage(new float[]{flow12, flow21, flow22, flow11});
			heightNE = getFluidHeightAverage(new float[]{flow10, flow20, flow21, flow11});
		} else {
			heightNW = flow11;
			heightSW = flow11;
			heightSE = flow11;
			heightNE = flow11;
		}

		IExtendedBlockState extState = (IExtendedBlockState) state;
		extState = extState.withProperty(HEIGHT_NW, heightNW).withProperty(HEIGHT_SW, heightSW);
		extState = extState.withProperty(HEIGHT_SE, heightSE).withProperty(HEIGHT_NE, heightNE);
		extState = extState.withProperty(FLOW_DIRECTION, (float) BlockFluidBase.getFlowDirection(world, pos));

		return extState;
	}

	private static <T extends BlockFluidBase & IFluidBlockWithModel> float getFluidHeightForRender(T fluidBlock, IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block verticalOrigin = world.getBlockState(pos.down(fluidBlock.getDensityDir())).getBlock();
		if (state.getBlock() == fluidBlock) {
			if (verticalOrigin.getMaterial().isLiquid() || verticalOrigin instanceof IFluidBlock) {
				return 1;
			}

			if ((Integer) state.getValue(BlockFluidBase.LEVEL) == fluidBlock.getMaxRenderHeightMeta()) {
				return 0.875F;
			}
		}
		return !state.getBlock().getMaterial().isSolid() && verticalOrigin == fluidBlock ? 1 : fluidBlock.getQuantaPercentage(world, pos) * 0.875F;
	}

	private static float getFluidHeightAverage(float[] height) {
		float total = 0;
		int count = 0;

		float end = 0;

		for (int i = 0; i < height.length; i++) {
			if (height[i] >= 0.875F && end != 1F) {
				end = height[i];
			}

			if (height[i] >= 0) {
				total += height[i];
				count++;
			}
		}

		if (end == 0)
			end = total / count;

		return end;
	}
}
