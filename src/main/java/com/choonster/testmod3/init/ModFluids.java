package com.choonster.testmod3.init;

import com.choonster.testmod3.block.fluid.BlockFluidNoFlow;
import com.choonster.testmod3.item.block.ItemFluidTank;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

public class ModFluids {
	public static Fluid fluidStatic;
	public static BlockFluidFinite blockStatic;

	public static Fluid fluidStaticGas;
	public static BlockFluidNoFlow blockStaticGas;

	public static Fluid fluidNormal;
	public static BlockFluidClassic blockNormal;

	public static Fluid fluidNormalGas;
	public static BlockFluidClassic blockNormalGas;

	public static Set<IFluidBlock> fluidBlocks = new HashSet<>();

	public static void registerFluids() {
		fluidStatic = createFluid("static", "testmod3:blocks/fluid_static", false).setLuminosity(10).setDensity(800).setViscosity(1500);
		blockStatic = registerFluidBlock(new BlockFluidNoFlow(fluidStatic, new MaterialLiquid(MapColor.brownColor)));

		fluidStaticGas = createFluid("staticgas", "testmod3:blocks/fluid_staticGas", false).setLuminosity(10).setDensity(-800).setViscosity(1500).setGaseous(true);
		blockStaticGas = registerFluidBlock(new BlockFluidNoFlow(fluidStaticGas, new MaterialLiquid(MapColor.brownColor)));

		fluidNormal = createFluid("normal", "testmod3:blocks/fluid_normal", true).setLuminosity(10).setDensity(1600).setViscosity(100);
		blockNormal = registerFluidBlock(new BlockFluidClassic(fluidNormal, new MaterialLiquid(MapColor.adobeColor)));

		fluidNormalGas = createFluid("normalgas", "testmod3:blocks/fluid_normalGas", true).setLuminosity(10).setDensity(-1600).setViscosity(100).setGaseous(true);
		blockNormalGas = registerFluidBlock(new BlockFluidClassic(fluidNormalGas, new MaterialLiquid(MapColor.adobeColor)));
	}

	public static void registerFluidContainers() {
		registerTank(FluidRegistry.WATER);
		registerTank(FluidRegistry.LAVA);

		for (IFluidBlock fluidBlock : fluidBlocks) {
			registerBucket(fluidBlock.getFluid());
			registerTank(fluidBlock.getFluid());
		}
	}

	private static Fluid createFluid(String name, String textureName, boolean hasFlowIcon) {
		ResourceLocation still = new ResourceLocation(textureName + "_still");
		ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(textureName + "_flow") : still;

		Fluid fluid = new Fluid(name, still, flowing);
		if (!FluidRegistry.registerFluid(fluid)) {
			throw new IllegalStateException(String.format("Unable to register fluid %s", fluid.getID()));
		}

		return fluid;
	}

	private static <T extends Block & IFluidBlock> T registerFluidBlock(T block) {
		String fluidName = block.getFluid().getUnlocalizedName();
		block.setUnlocalizedName(fluidName);
		GameRegistry.registerBlock(block, fluidName);

		fluidBlocks.add(block);

		return block;
	}

	private static void registerBucket(Fluid fluid) {
		ItemStack filledBucket = ModItems.bucket.addFluid(fluid);

		FluidContainerRegistry.registerFluidContainer(fluid, filledBucket, FluidContainerRegistry.EMPTY_BUCKET);
	}

	private static void registerTank(Fluid fluid) {
		FluidStack fluidStack = new FluidStack(fluid, 10 * FluidContainerRegistry.BUCKET_VOLUME);
		ItemStack filledTank = ((ItemFluidTank) Item.getItemFromBlock(ModBlocks.fluidTank)).addFluid(fluidStack);

		FluidContainerRegistry.registerFluidContainer(fluidStack, filledTank, new ItemStack(ModBlocks.fluidTank));
	}
}
