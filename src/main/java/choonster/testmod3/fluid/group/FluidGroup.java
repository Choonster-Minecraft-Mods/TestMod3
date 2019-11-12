package choonster.testmod3.fluid.group;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * A group consisting of a still and flowing fluid, a fluid block and a bucket item.
 *
 * @author Choonster
 */
public class FluidGroup<STILL extends Fluid, FLOWING extends Fluid, BLOCK extends FlowingFluidBlock, BUCKET extends Item> {
	private final RegistryObject<STILL> still;
	private final RegistryObject<FLOWING> flowing;
	private final RegistryObject<BLOCK> block;
	private final RegistryObject<BUCKET> bucket;

	protected FluidGroup(final RegistryObject<STILL> still, final RegistryObject<FLOWING> flowing, final RegistryObject<BLOCK> block, final RegistryObject<BUCKET> bucket) {
		this.still = still;
		this.flowing = flowing;
		this.block = block;
		this.bucket = bucket;
	}

	/**
	 * Gets the still fluid.
	 *
	 * @return The still fluid.
	 */
	public RegistryObject<STILL> getStill() {
		return still;
	}

	/**
	 * Gets the flowing fluid.
	 *
	 * @return The flowing fluid.
	 */
	public RegistryObject<FLOWING> getFlowing() {
		return flowing;
	}

	/**
	 * Gets the fluid block.
	 *
	 * @return The fluid block.
	 */
	public RegistryObject<BLOCK> getBlock() {
		return block;
	}

	/**
	 * Gets the bucket item.
	 *
	 * @return The bucket item.
	 */
	public RegistryObject<BUCKET> getBucket() {
		return bucket;
	}

	public static class Builder<STILL extends Fluid, FLOWING extends Fluid, BLOCK extends FlowingFluidBlock, BUCKET extends Item> {
		protected final DeferredRegister<Fluid> fluids;
		protected final DeferredRegister<Block> blocks;
		protected final DeferredRegister<Item> items;

		protected final String name;

		protected IFluidFactory<STILL> stillFactory;
		protected IFluidFactory<FLOWING> flowingFactory;
		protected IBlockFactory<STILL, BLOCK> blockFactory;
		protected IBucketFactory<STILL, BUCKET> bucketFactory;

		protected FluidAttributes.Builder attributes;

		protected ForgeFlowingFluid.Properties properties;

		public Builder(final String name, final DeferredRegister<Fluid> fluids, final DeferredRegister<Block> blocks, final DeferredRegister<Item> items) {
			this.name = name;
			this.fluids = fluids;
			this.blocks = blocks;
			this.items = items;
		}

		public Builder<STILL, FLOWING, BLOCK, BUCKET> stillFactory(final IFluidFactory<STILL> stillFactory) {
			Preconditions.checkNotNull(stillFactory, "stillFactory");
			this.stillFactory = stillFactory;
			return this;
		}

		public Builder<STILL, FLOWING, BLOCK, BUCKET> flowingFactory(final IFluidFactory<FLOWING> flowingFactory) {
			Preconditions.checkNotNull(flowingFactory, "flowingFactory");
			this.flowingFactory = flowingFactory;
			return this;
		}

		public Builder<STILL, FLOWING, BLOCK, BUCKET> blockFactory(final IBlockFactory<STILL, BLOCK> blockFactory) {
			Preconditions.checkNotNull(blockFactory, "blockFactory");
			this.blockFactory = blockFactory;
			return this;
		}

		public Builder<STILL, FLOWING, BLOCK, BUCKET> bucketFactory(final IBucketFactory<STILL, BUCKET> bucketFactory) {
			Preconditions.checkNotNull(bucketFactory, "bucketFactory");
			this.bucketFactory = bucketFactory;
			return this;
		}

		public Builder<STILL, FLOWING, BLOCK, BUCKET> attributes(final FluidAttributes.Builder attributes) {
			this.attributes = attributes;
			return this;
		}

		public FluidGroup<STILL, FLOWING, BLOCK, BUCKET> build() {
			return buildImpl(FluidGroup::new);
		}

		protected <GROUP extends FluidGroup<STILL, FLOWING, BLOCK, BUCKET>> GROUP buildImpl(final IFluidGroupFactory<GROUP, STILL, FLOWING, BLOCK, BUCKET> factory) {
			Preconditions.checkState(stillFactory != null, "Still Factory not provided");
			Preconditions.checkState(flowingFactory != null, "Flowing factory not provided");
			Preconditions.checkState(blockFactory != null, "Block Factory not provided");
			Preconditions.checkState(bucketFactory != null, "Bucket Factory not provided");
			Preconditions.checkState(attributes != null, "Attributes not provided");

			final RegistryObject<STILL> still = fluids.register(name, () -> stillFactory.create(properties));
			final RegistryObject<FLOWING> flowing = fluids.register("flowing_" + name, () -> flowingFactory.create(properties));

			final RegistryObject<BLOCK> block = blocks.register(name, () -> blockFactory.create(still));
			final RegistryObject<BUCKET> bucket = items.register(name + "_bucket", () -> bucketFactory.create(still));

			properties = new ForgeFlowingFluid.Properties(still, flowing, attributes)
					.block(block)
					.bucket(bucket);

			return factory.create(still, flowing, block, bucket);
		}

		@FunctionalInterface
		protected interface IFluidGroupFactory<
				GROUP extends FluidGroup<STILL, FLOWING, BLOCK, BUCKET>,
				STILL extends Fluid, FLOWING extends Fluid, BLOCK extends FlowingFluidBlock, BUCKET extends Item
				> {
			GROUP create(RegistryObject<STILL> still, RegistryObject<FLOWING> flowing, RegistryObject<BLOCK> block, RegistryObject<BUCKET> bucket);
		}
	}

	public static Block.Properties defaultBlockProperties(final Material material) {
		return Block.Properties.create(material)
				.doesNotBlockMovement()
				.hardnessAndResistance(100)
				.noDrops();
	}

	public static Item.Properties defaultBucketProperties() {
		return new Item.Properties()
				.containerItem(Items.BUCKET)
				.maxStackSize(1)
				.group(ItemGroup.MISC);
	}

	public interface IFluidFactory<FLUID extends Fluid> {
		FLUID create(ForgeFlowingFluid.Properties properties);
	}

	public interface IBlockFactory<STILL extends Fluid, BLOCK extends Block> {
		BLOCK create(Supplier<? extends STILL> fluid);
	}

	public interface IBucketFactory<STILL extends Fluid, BUCKET extends Item> {
		BUCKET create(Supplier<? extends STILL> fluid);
	}
}
