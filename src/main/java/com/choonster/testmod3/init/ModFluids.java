package com.choonster.testmod3.init;

import com.choonster.testmod3.block.fluid.BlockFluidClassicWithModel;
import com.choonster.testmod3.block.fluid.BlockFluidFiniteWithModel;
import com.choonster.testmod3.block.fluid.BlockFluidNoFlow;
import com.choonster.testmod3.fluid.FluidTestMod3;
import com.choonster.testmod3.item.ItemBucketTestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

public class ModFluids {
	public static FluidTestMod3 fluidStatic;
	public static BlockFluidFiniteWithModel blockStatic;

	public static FluidTestMod3 fluidStaticGas;
	public static BlockFluidNoFlow blockStaticGas;

	public static FluidTestMod3 fluidNormal;
	public static BlockFluidClassicWithModel blockNormal;

	public static FluidTestMod3 fluidNormalGas;
	public static BlockFluidClassicWithModel blockNormalGas;

	public static Map<Fluid, ItemBucketTestMod3> buckets = new HashMap<>();

	public static void registerFluids() {
		fluidStatic = (FluidTestMod3) registerFluid(new FluidTestMod3("static", false).setLuminosity(10).setDensity(800).setViscosity(1500));
		blockStatic = registerFluidBlock(new BlockFluidNoFlow(fluidStatic, new MaterialLiquid(MapColor.brownColor)));

		fluidStaticGas = (FluidTestMod3) registerFluid(new FluidTestMod3("staticGas", false).setLuminosity(10).setDensity(-800).setViscosity(1500));
		blockStaticGas = registerFluidBlock(new BlockFluidNoFlow(fluidStaticGas, new MaterialLiquid(MapColor.brownColor)));

		fluidNormal = (FluidTestMod3) registerFluid(new FluidTestMod3("normal").setLuminosity(10).setDensity(1600).setViscosity(100));
		blockNormal = registerFluidBlock(new BlockFluidClassicWithModel(fluidNormal, new MaterialLiquid(MapColor.adobeColor)));

		fluidNormalGas = (FluidTestMod3) registerFluid(new FluidTestMod3("normalGas").setLuminosity(10).setDensity(-1600).setViscosity(100));
		blockNormalGas = registerFluidBlock(new BlockFluidClassicWithModel(fluidNormalGas, new MaterialLiquid(MapColor.adobeColor)));
	}

	public static void registerBuckets() {
		registerBucket(fluidStatic);
		registerBucket(fluidStaticGas);
		registerBucket(fluidNormal);
		registerBucket(fluidNormalGas);
	}

	private static <T extends Fluid> T registerFluid(T fluid) {
		if (!FluidRegistry.registerFluid(fluid)) {
			throw new IllegalStateException(String.format("Unable to register fluid %s", fluid.getID()));
		}

		return fluid;
	}

	private static <T extends Block & IFluidBlock> T registerFluidBlock(T block) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));

		return block;
	}

	private static void registerBucket(Fluid fluid) {
		ItemStack filledBucket = ModItems.bucket.addFluid(fluid);

		FluidContainerRegistry.registerFluidContainer(fluid, filledBucket);
	}
}
