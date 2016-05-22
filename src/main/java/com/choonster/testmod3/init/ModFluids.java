package com.choonster.testmod3.init;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.block.fluid.BlockFluidNoFlow;
import com.choonster.testmod3.item.block.ItemFluidTank;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class ModFluids {
	public static final Fluid STATIC;
	public static final Fluid STATIC_GAS;
	public static final Fluid NORMAL;
	public static final Fluid NORMAL_GAS;
	public static final Fluid FINITE;

	/**
	 * The fluids registered by this mod. Includes fluids that were already registered by another mod.
	 */
	public static final Set<Fluid> FLUIDS = new HashSet<>();

	/**
	 * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
	 */
	public static final Set<IFluidBlock> MOD_FLUID_BLOCKS = new HashSet<>();

	static {
		STATIC = createFluid("static", false,
				fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
				fluid -> new BlockFluidNoFlow(fluid, new MaterialLiquid(MapColor.BROWN)));

		STATIC_GAS = createFluid("staticGas", false,
				fluid -> fluid.setLuminosity(10).setDensity(-800).setViscosity(1500).setGaseous(true),
				fluid -> new BlockFluidNoFlow(fluid, new MaterialLiquid(MapColor.BROWN)));

		NORMAL = createFluid("normal", true,
				fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
				fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

		NORMAL_GAS = createFluid("normalGas", true,
				fluid -> fluid.setLuminosity(10).setDensity(-1600).setViscosity(100).setGaseous(true),
				fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

		FINITE = createFluid("finite", false,
				fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
				fluid -> new BlockFluidFinite(fluid, new MaterialLiquid(MapColor.BLACK)));
	}

	public static void registerFluids() {
		// Dummy method to make sure the static initialiser runs
	}

	public static void registerFluidContainers() {
		registerTank(FluidRegistry.WATER);
		registerTank(FluidRegistry.LAVA);

		for (final Fluid fluid : FLUIDS) {
			registerBucket(fluid);
			registerTank(fluid);
		}
	}

	/**
	 * Create a {@link Fluid} and its {@link IFluidBlock}, or use the existing ones if a fluid has already been registered with the same name.
	 *
	 * @param name                 The name of the fluid
	 * @param hasFlowIcon          Does the fluid have a flow icon?
	 * @param fluidPropertyApplier A function that sets the properties of the {@link Fluid}
	 * @param blockFactory         A function that creates the {@link IFluidBlock}
	 * @return The fluid and block
	 */
	private static <T extends Block & IFluidBlock> Fluid createFluid(String name, boolean hasFlowIcon, Consumer<Fluid> fluidPropertyApplier, Function<Fluid, T> blockFactory) {
		final String texturePrefix = Constants.RESOURCE_PREFIX + "blocks/fluid_";

		final ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
		final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

		Fluid fluid = new Fluid(name, still, flowing);
		final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

		if (useOwnFluid) {
			fluidPropertyApplier.accept(fluid);
			registerFluidBlock(blockFactory.apply(fluid));
		} else {
			fluid = FluidRegistry.getFluid(name);
		}

		FLUIDS.add(fluid);

		return fluid;
	}

	private static <T extends Block & IFluidBlock> T registerFluidBlock(T block) {
		block.setRegistryName("fluid." + block.getFluid().getName());
		block.setUnlocalizedName(Constants.RESOURCE_PREFIX + block.getFluid().getUnlocalizedName());
		block.setCreativeTab(TestMod3.creativeTab);

		ModBlocks.registerBlock(block);

		MOD_FLUID_BLOCKS.add(block);

		return block;
	}

	private static void registerBucket(Fluid fluid) {
		FluidRegistry.addBucketForFluid(fluid);
	}

	private static void registerTank(Fluid fluid) {
		@SuppressWarnings("deprecation")
		final FluidStack fluidStack = new FluidStack(fluid, 10 * FluidContainerRegistry.BUCKET_VOLUME);
		((ItemFluidTank) Item.getItemFromBlock(ModBlocks.FLUID_TANK)).addFluid(fluidStack);
	}
}
