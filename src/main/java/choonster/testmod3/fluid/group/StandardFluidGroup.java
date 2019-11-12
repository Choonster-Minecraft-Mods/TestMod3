package choonster.testmod3.fluid.group;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * A group consisting of a still and flowing fluid, a fluid block and a bucket item.
 * <p>
 * This class restricts the still and flowing fluids to subclasses of {@link FlowingFluid} and provides default
 * still, flowing, block and bucket factories.
 * <p>
 * The only required inputs are the {@link FluidAttributes} ({@link StandardFluidGroup.Builder#attributes(FluidAttributes.Builder)})
 * and the block's {@link Material} ({@link StandardFluidGroup.Builder#blockMaterial(Material)}).
 *
 * @author Choonster
 */
public class StandardFluidGroup extends FluidGroup<FlowingFluid, FlowingFluid, FlowingFluidBlock, Item> {
	private StandardFluidGroup(final RegistryObject<FlowingFluid> still, final RegistryObject<FlowingFluid> flowing, final RegistryObject<FlowingFluidBlock> block, final RegistryObject<Item> bucket) {
		super(still, flowing, block, bucket);
	}

	public static class Builder extends FluidGroup.Builder<FlowingFluid, FlowingFluid, FlowingFluidBlock, Item> {
		private Material blockMaterial;

		public Builder(final String name, final DeferredRegister<Fluid> fluids, final DeferredRegister<Block> blocks, final DeferredRegister<Item> items) {
			super(name, fluids, blocks, items);

			stillFactory = ForgeFlowingFluid.Source::new;
			flowingFactory = ForgeFlowingFluid.Flowing::new;

			blockFactory = fluid -> {
				Preconditions.checkState(blockMaterial != null, "Block Material not provided");

				return new FlowingFluidBlock(fluid, FluidGroup.defaultBlockProperties(blockMaterial));
			};

			bucketFactory = fluid -> new BucketItem(fluid, FluidGroup.defaultBucketProperties());
		}

		public Builder blockMaterial(final Material blockMaterial) {
			this.blockMaterial = blockMaterial;
			return this;
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
		public Builder blockFactory(final IBlockFactory<FlowingFluid, FlowingFluidBlock> blockFactory) {
			return (Builder) super.blockFactory(blockFactory);
		}

		@Override
		public Builder bucketFactory(final IBucketFactory<FlowingFluid, Item> bucketFactory) {
			return (Builder) super.bucketFactory(bucketFactory);
		}

		@Override
		public Builder attributes(final FluidAttributes.Builder attributes) {
			return (Builder) super.attributes(attributes);
		}

		@Override
		public StandardFluidGroup build() {
			return buildImpl(StandardFluidGroup::new);
		}
	}
}
