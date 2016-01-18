package com.choonster.testmod3.init;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.block.fluid.BlockFluidNoFlow;
import com.choonster.testmod3.item.block.ItemFluidTank;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModFluids {
	public static Fluid static_;
	public static Fluid staticGas;
	public static Fluid normal;
	public static Fluid normalGas;
	public static Fluid finite;

	/**
	 * The fluids registered by this mod. Includes fluids that were already registered by another mod.
	 */
	public static final Set<Fluid> fluids = new HashSet<>();

	/**
	 * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
	 */
	public static final Set<IFluidBlock> modFluidBlocks = new HashSet<>();

	public static void registerFluids() {

		static_ = createFluid("static", false,
				fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
				fluid -> new BlockFluidNoFlow(fluid, new MaterialLiquid(MapColor.brownColor)));

		staticGas = createFluid("staticGas", false,
				fluid -> fluid.setLuminosity(10).setDensity(-800).setViscosity(1500).setGaseous(true),
				fluid -> new BlockFluidNoFlow(fluid, new MaterialLiquid(MapColor.brownColor)));

		normal = createFluid("normal", true,
				fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
				fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.adobeColor)));

		normalGas = createFluid("normalGas", true,
				fluid -> fluid.setLuminosity(10).setDensity(-1600).setViscosity(100).setGaseous(true),
				fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.adobeColor)));

		finite = createFluid("finite", false,
				fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
				fluid -> new BlockFluidFinite(fluid, new MaterialLiquid(MapColor.blackColor)));
	}

	public static void registerFluidContainers() {
		registerTank(FluidRegistry.WATER);
		registerTank(FluidRegistry.LAVA);

		for (Fluid fluid : fluids) {
			registerBucket(fluid);
			registerTank(fluid);
		}
	}

	/**
	 * Create a {@link Fluid} and its {@link IFluidBlock}, or use the existing ones if a fluid has already been registered with the same name.
	 *
	 * @param name         The name of the fluid
	 * @param hasFlowIcon  Does the fluid have a flow icon?
	 * @param fluidPropertyApplier A function that sets the properties of the {@link Fluid}
	 * @param blockFactory A function that creates the {@link IFluidBlock}
	 * @return The fluid and block
	 */
	private static <T extends Block & IFluidBlock> Fluid createFluid(String name, boolean hasFlowIcon, Consumer<Fluid> fluidPropertyApplier, Function<Fluid, T> blockFactory) {
		final String texturePrefix = Constants.RESOURCE_PREFIX + "blocks/fluid_";

		ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
		ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

		Fluid fluid = new Fluid(name, still, flowing);
		boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

		if (useOwnFluid) {
			fluidPropertyApplier.accept(fluid);
			registerFluidBlock(blockFactory.apply(fluid));
		} else {
			fluid = FluidRegistry.getFluid(name);
		}

		fluids.add(fluid);

		return fluid;
	}

	private static <T extends Block & IFluidBlock> T registerFluidBlock(T block) {
		block.setRegistryName("fluid." + block.getFluid().getName());
		block.setUnlocalizedName(Constants.RESOURCE_PREFIX + block.getFluid().getUnlocalizedName());
		block.setCreativeTab(TestMod3.creativeTab);
		GameRegistry.registerBlock(block);

		modFluidBlocks.add(block);

		return block;
	}

	private static void registerBucket(Fluid fluid) {
		ItemStack filledBucket = ModItems.bucket.registerBucketForFluid(fluid);

		if (!FluidContainerRegistry.registerFluidContainer(fluid, filledBucket, FluidContainerRegistry.EMPTY_BUCKET)) {
			Logger.error("Unable to register bucket of %s as fluid container", fluid.getName());
		}
	}

	private static void registerTank(Fluid fluid) {
		FluidStack fluidStack = new FluidStack(fluid, 10 * FluidContainerRegistry.BUCKET_VOLUME);
		((ItemFluidTank) Item.getItemFromBlock(ModBlocks.fluidTank)).addFluid(fluidStack);
	}
}
