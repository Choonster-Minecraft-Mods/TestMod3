package com.choonster.testmod3.client.util;

import com.choonster.testmod3.block.fluid.FluidUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector3f;
import java.util.*;


/**
 * The fluid rendering setup associated with this class was originally created by kirderf1 for www.github.com/mraof/minestuck
 * When copying this code, please keep this comment or refer back to the original source in another way, if possible.
 */
@SideOnly(Side.CLIENT)
public class FluidBlockModel implements ISmartBlockModel {

	static final float RENDER_OFFSET = 0.001F;

	@Override
	public List getFaceQuads(EnumFacing facing) {
		return null;
	}

	@Override
	public List getGeneralQuads() {
		return null;
	}

	@Override
	public boolean isAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean isBuiltInRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getTexture() {
		return null;
	}

	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return null;
	}

	private final Map<List<Float>, SimpleBakedModel> models = new HashMap<>();

	@Override
	public IBakedModel handleBlockState(IBlockState state) {
		IExtendedBlockState extState = (IExtendedBlockState) state;
		float heightNW = extState.getValue(FluidUtils.HEIGHT_NW);
		float heightSW = extState.getValue(FluidUtils.HEIGHT_SW);
		float heightSE = extState.getValue(FluidUtils.HEIGHT_SE);
		float heightNE = extState.getValue(FluidUtils.HEIGHT_NE);
		float flow = extState.getValue(FluidUtils.FLOW_DIRECTION);

		return getModel((IFluidBlock) state.getBlock(), Arrays.asList(heightSE, heightSW, heightNW, heightNE, flow));
	}

	private SimpleBakedModel getModel(IFluidBlock block, List<Float> param) {
		SimpleBakedModel model = models.get(param);
		if (model == null) {
			model = this.buildModel(block, param);
			models.put(param, model);
		}
		return model;
	}

	private SimpleBakedModel buildModel(IFluidBlock block, List<Float> param) {
		List<List<BakedQuad>> faceQuads = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			if (i == EnumFacing.DOWN.ordinal())
				faceQuads.add(Arrays.asList(createQuad(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), new Vector3f(1, 0, 1), new Vector3f(0, 0, 1), block.getFluid().getStillIcon(), EnumFacing.DOWN)));
			else if (i == EnumFacing.UP.ordinal()) {
				TextureAtlasSprite sprite = param.get(4) < -999F ? block.getFluid().getStillIcon() : block.getFluid().getFlowingIcon();
				BakedQuad quad1 = createQuad(new Vector3f(0, param.get(2), 0), new Vector3f(0, param.get(1), 1),
						new Vector3f(1, param.get(0), 1), new Vector3f(1, param.get(3), 0), sprite, EnumFacing.UP, param.get(4));
				BakedQuad quad2 = createQuad(new Vector3f(0, param.get(2) - RENDER_OFFSET, 0), new Vector3f(1, param.get(3) - RENDER_OFFSET, 0),
						new Vector3f(1, param.get(0) - RENDER_OFFSET, 1), new Vector3f(0, param.get(1) - RENDER_OFFSET, 1), sprite, EnumFacing.DOWN, param.get(4));
				faceQuads.add(Arrays.asList(quad1, quad2));
			} else {
				EnumFacing facing = EnumFacing.values()[i];
				float height1 = param.get(facing.getHorizontalIndex());
				float height2 = param.get((facing.getHorizontalIndex() + 1) % 4);
				int p = facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 1 : 0;
				float offset = facing.getAxisDirection().getOffset() * RENDER_OFFSET;

				if (facing.getAxis() == EnumFacing.Axis.X) {
					BakedQuad quad1 = createQuad(new Vector3f(p, 0, p), new Vector3f(p, 0, 1 - p), new Vector3f(p, height1, 1 - p), new Vector3f(p, height2, p), block.getFluid().getFlowingIcon(), facing);
					BakedQuad quad2 = createQuad(new Vector3f(p - offset, 0, p), new Vector3f(p - offset, height2, p), new Vector3f(p - offset, height1, 1 - p), new Vector3f(p - offset, 0, 1 - p), block.getFluid().getFlowingIcon(), facing.getOpposite());
					faceQuads.add(Arrays.asList(quad1, quad2));
				} else {
					BakedQuad quad1 = createQuad(new Vector3f(1 - p, 0, p), new Vector3f(p, 0, p), new Vector3f(p, height1, p), new Vector3f(1 - p, height2, p), block.getFluid().getFlowingIcon(), facing);
					BakedQuad quad2 = createQuad(new Vector3f(1 - p, 0, p - offset), new Vector3f(1 - p, height2, p - offset), new Vector3f(p, height1, p - offset), new Vector3f(p, 0, p - offset), block.getFluid().getFlowingIcon(), facing.getOpposite());
					faceQuads.add(Arrays.asList(quad1, quad2));
				}
			}
		}

		return new SimpleBakedModel(Collections.emptyList(), faceQuads, false, false, block.getFluid().getStillIcon(), null);
	}

	private BakedQuad createQuad(Vector3f vec1, Vector3f vec2, Vector3f vec3, Vector3f vec4, TextureAtlasSprite sprite, EnumFacing facing) {
		return createQuad(vec1, vec2, vec3, vec4, sprite, facing, -1000F);
	}

	private BakedQuad createQuad(Vector3f vec1, Vector3f vec2, Vector3f vec3, Vector3f vec4, TextureAtlasSprite sprite, EnumFacing facing, float flowDir) {
		int[] data = new int[28];
		int shade = getShade(facing);

		float xFlow = -1, zFlow = -1;
		if (flowDir > -999F) {
			xFlow = MathHelper.sin(flowDir) * 0.25F;
			zFlow = MathHelper.cos(flowDir) * 0.25F;
		}

		Vector3f[] vectors = {vec1, vec2, vec3, vec4};
		for (int i = 0; i < 4; i++) {
			Vector3f vec = vectors[i];
			int index = i * 7;
			data[index] = Float.floatToRawIntBits(vec.x);
			data[index + 1] = Float.floatToRawIntBits(vec.y);
			data[index + 2] = Float.floatToRawIntBits(vec.z);
			data[index + 3] = shade;
			float u = sprite.getMaxU() - sprite.getMinU();
			float v = sprite.getMaxV() - sprite.getMinV();
			if (facing.getAxis() == EnumFacing.Axis.X) {
				data[index + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(vec.z * 16F));
				data[index + 5] = Float.floatToRawIntBits(sprite.getInterpolatedV(16F - vec.y * 16F));
			} else if (facing.getAxis() == EnumFacing.Axis.Y) {
				if (flowDir < -999F) {
					data[index + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(vec.x * 16F));
					data[index + 5] = Float.floatToRawIntBits(sprite.getInterpolatedV(vec.z * 16F));
				} else {
					data[index + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(8F + ((vec.z * 2 - 1) * xFlow + (vec.x * 2 - 1) * zFlow) * 16F));    //The x and z values here are only expected to be 0 or 1.
					data[index + 5] = Float.floatToRawIntBits(sprite.getInterpolatedV(8F + ((1 - 2 * vec.x) * xFlow + (vec.z * 2 - 1) * zFlow) * 16F));
				}
			} else {
				data[index + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU(vec.x * 16F));
				data[index + 5] = Float.floatToRawIntBits(sprite.getInterpolatedV(16F - vec.y * 16F));
			}

		}
		return new BakedQuad(data, -1, facing);
	}

	private int getShade(EnumFacing facing)    //Copied from FaceBakery (Only the end result, and not the process)
	{
		if (facing == EnumFacing.DOWN)
			return -8421505;
		if (facing == EnumFacing.SOUTH || facing == EnumFacing.NORTH)
			return -3355444;
		if (facing == EnumFacing.WEST || facing == EnumFacing.EAST)
			return -6710887;
		else return -1;
	}
}