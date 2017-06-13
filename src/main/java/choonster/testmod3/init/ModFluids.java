package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.fluid.BlockFluidNoFlow;
import choonster.testmod3.block.fluid.BlockFluidPortalDisplacement;
import choonster.testmod3.item.block.ItemFluidTank;
import choonster.testmod3.tileentity.TileEntityFluidTank;
import choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class ModFluids {
	/**
	 * The fluids registered by this mod. Includes fluids that were already registered by another mod.
	 */
	public static final Set<Fluid> FLUIDS = new HashSet<>();

	/**
	 * The fluid blocks from this mod only. Doesn't include blocks for fluids that were already registered by another mod.
	 */
	public static final Set<IFluidBlock> MOD_FLUID_BLOCKS = new HashSet<>();

	public static final Fluid STATIC = createFluid("static", false,
			fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
			fluid -> new BlockFluidNoFlow(fluid, new MaterialLiquid(MapColor.BROWN)));

	public static final Fluid STATIC_GAS = createFluid("static_gas", false,
			fluid -> fluid.setLuminosity(10).setDensity(-800).setViscosity(1500).setGaseous(true),
			fluid -> new BlockFluidNoFlow(fluid, new MaterialLiquid(MapColor.BROWN)));

	public static final Fluid NORMAL = createFluid("normal", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

	public static final Fluid NORMAL_GAS = createFluid("normal_gas", true,
			fluid -> fluid.setLuminosity(10).setDensity(-1600).setViscosity(100).setGaseous(true),
			fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

	public static final Fluid FINITE = createFluid("finite", false,
			fluid -> fluid.setLuminosity(10).setDensity(800).setViscosity(1500),
			fluid -> new BlockFluidFinite(fluid, new MaterialLiquid(MapColor.BLACK)));

	public static final Fluid PORTAL_DISPLACEMENT = createFluid("portal_displacement", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockFluidPortalDisplacement(fluid, new MaterialLiquid(MapColor.DIAMOND)));

	/**
	 * Create a {@link Fluid} and its {@link IFluidBlock}, or use the existing ones if a fluid has already been registered with the same name.
	 *
	 * @param name                 The name of the fluid
	 * @param hasFlowIcon          Does the fluid have a flow icon?
	 * @param fluidPropertyApplier A function that sets the properties of the {@link Fluid}
	 * @param blockFactory         A function that creates the {@link IFluidBlock}
	 * @return The fluid and block
	 */
	private static <T extends Block & IFluidBlock> Fluid createFluid(final String name, final boolean hasFlowIcon, final Consumer<Fluid> fluidPropertyApplier, final Function<Fluid, T> blockFactory) {
		final String texturePrefix = Constants.RESOURCE_PREFIX + "blocks/fluid_";

		final ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
		final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

		Fluid fluid = new Fluid(name, still, flowing);
		final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

		if (useOwnFluid) {
			fluidPropertyApplier.accept(fluid);
			MOD_FLUID_BLOCKS.add(blockFactory.apply(fluid));
		} else {
			fluid = FluidRegistry.getFluid(name);
		}

		FLUIDS.add(fluid);

		return fluid;
	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		/**
		 * Register this mod's fluid {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCKS) {
				final Block block = (Block) fluidBlock;
				block.setRegistryName(TestMod3.MODID, "fluid." + fluidBlock.getFluid().getName());
				block.setUnlocalizedName(Constants.RESOURCE_PREFIX + fluidBlock.getFluid().getUnlocalizedName());
				block.setCreativeTab(TestMod3.creativeTab);
				registry.register(block);
			}
		}

		/**
		 * Register this mod's fluid {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final IFluidBlock fluidBlock : MOD_FLUID_BLOCKS) {
				final Block block = (Block) fluidBlock;
				final ItemBlock itemBlock = new ItemBlock(block);
				itemBlock.setRegistryName(block.getRegistryName());
				registry.register(itemBlock);
			}
		}
	}

	public static void registerFluidContainers() {
		registerTank(FluidRegistry.WATER);
		registerTank(FluidRegistry.LAVA);

		for (final Fluid fluid : FLUIDS) {
			registerBucket(fluid);
			registerTank(fluid);
		}
	}

	private static void registerBucket(final Fluid fluid) {
		FluidRegistry.addBucketForFluid(fluid);
	}

	private static void registerTank(final Fluid fluid) {
		final FluidStack fluidStack = new FluidStack(fluid, TileEntityFluidTank.CAPACITY);

		final Item item = Item.getItemFromBlock(ModBlocks.FLUID_TANK);
		assert item instanceof ItemFluidTank;

		((ItemFluidTank) item).addFluid(fluidStack);
	}
}
