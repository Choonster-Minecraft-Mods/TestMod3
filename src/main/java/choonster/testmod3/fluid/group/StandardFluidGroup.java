package choonster.testmod3.fluid.group;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A group consisting of a fluid type, a still and flowing fluid, a fluid block and a bucket item.
 * <p>
 * This class restricts the still and flowing fluids to subclasses of {@link FlowingFluid} and provides default
 * still, flowing, block and bucket factories.
 * <p>
 * The only required input is the {@link FluidType} factory ({@link StandardFluidGroup.Builder#typeFactory(Supplier)})}).
 *
 * @author Choonster
 */
public class StandardFluidGroup extends FluidGroup<FluidType, FlowingFluid, FlowingFluid, LiquidBlock, Item> {
	private StandardFluidGroup(final RegistryObject<FluidType> type, final RegistryObject<FlowingFluid> still, final RegistryObject<FlowingFluid> flowing, final RegistryObject<LiquidBlock> block, final RegistryObject<Item> bucket) {
		super(type, still, flowing, block, bucket);
	}

	public static class Builder extends FluidGroup.Builder<FluidType, FlowingFluid, FlowingFluid, LiquidBlock, Item> {
		public Builder(final String name, final DeferredRegister<FluidType> fluidTypes, final DeferredRegister<Fluid> fluids, final DeferredRegister<Block> blocks, final DeferredRegister<Item> items) {
			super(name, fluidTypes, fluids, blocks, items);

			stillFactory = ForgeFlowingFluid.Source::new;
			flowingFactory = ForgeFlowingFluid.Flowing::new;

			blockFactory = LiquidBlock::new;

			bucketFactory = fluid -> new BucketItem(fluid, FluidGroup.defaultBucketProperties());
		}

		@Override
		public Builder typeFactory(final Supplier<FluidType> typeFactory) {
			return (Builder) super.typeFactory(typeFactory);
		}

		@Override
		public Builder stillFactory(final IFluidFactory<FlowingFluid> stillFactory) {
			return (Builder) super.stillFactory(stillFactory);
		}

		@Override
		public Builder flowingFactory(final IFluidFactory<FlowingFluid> flowingFactory) {
			return (Builder) super.flowingFactory(flowingFactory);
		}

		@Override
		public Builder blockFactory(final IBlockFactory<FlowingFluid, LiquidBlock> blockFactory) {
			return (Builder) super.blockFactory(blockFactory);
		}

		@Override
		public Builder bucketFactory(final IBucketFactory<FlowingFluid, Item> bucketFactory) {
			return (Builder) super.bucketFactory(bucketFactory);
		}

		@Override
		public Builder propertiesCustomiser(final Consumer<ForgeFlowingFluid.Properties> propertiesCustomiser) {
			return (Builder) super.propertiesCustomiser(propertiesCustomiser);
		}

		@Override
		public Builder blockPropertiesCustomiser(final Consumer<BlockBehaviour.Properties> blockPropertiesCustomiser) {
			return (Builder) super.blockPropertiesCustomiser(blockPropertiesCustomiser);
		}

		@Override
		public StandardFluidGroup build() {
			return buildImpl(StandardFluidGroup::new);
		}
	}
}
