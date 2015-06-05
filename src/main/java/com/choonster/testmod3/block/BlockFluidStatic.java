package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

// A fluid that doesn't flow and has no animation
// Test for this thread:
// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2441743-fluid-id
public class BlockFluidStatic extends BlockFluidFinite {

	public BlockFluidStatic(Fluid fluid) {
		super(fluid, Material.water);
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName(fluid.getUnlocalizedName());
	}

	@Override
	public Vec3 getFlowVector(IBlockAccess world, BlockPos pos) {
		return new Vec3(0, 0, 0);
	}
}
