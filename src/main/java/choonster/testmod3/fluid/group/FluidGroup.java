package choonster.testmod3.fluid.group;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A group consisting of a fluid type, a still and flowing fluid, a fluid block and a bucket item.
 *
 * @author Choonster
 */
public class FluidGroup<TYPE extends FluidType, STILL extends Fluid, FLOWING extends Fluid, BLOCK extends LiquidBlock, BUCKET extends Item> {
	private final RegistryObject<TYPE> type;
	private final RegistryObject<STILL> still;
	private final RegistryObject<FLOWING> flowing;
	private final RegistryObject<BLOCK> block;
	private final RegistryObject<BUCKET> bucket;

	protected FluidGroup(final RegistryObject<TYPE> type, final RegistryObject<STILL> still, final RegistryObject<FLOWING> flowing, final RegistryObject<BLOCK> block, final RegistryObject<BUCKET> bucket) {
		this.type = type;
		this.still = still;
		this.flowing = flowing;
		this.block = block;
		this.bucket = bucket;
	}

	/**
	 * Gets the fluid type.
	 *
	 * @return THe fluid type.
	 */
	public RegistryObject<TYPE> getType() {
		return type;
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

	public static class Builder<TYPE extends FluidType, STILL extends Fluid, FLOWING extends Fluid, BLOCK extends LiquidBlock, BUCKET extends Item> {
		protected final DeferredRegister<FluidType> fluidTypes;
		protected final DeferredRegister<Fluid> fluids;
		protected final DeferredRegister<Block> blocks;
		protected final DeferredRegister<Item> items;

		protected final String name;

		@Nullable
		private Supplier<TYPE> typeFactory;
		@Nullable
		protected IFluidFactory<STILL> stillFactory;
		@Nullable
		protected IFluidFactory<FLOWING> flowingFactory;
		@Nullable
		protected IBlockFactory<STILL, BLOCK> blockFactory;
		@Nullable
		protected IBucketFactory<STILL, BUCKET> bucketFactory;
		@Nullable
		protected Consumer<ForgeFlowingFluid.Properties> propertiesCustomiser;

		@Nullable
		protected ForgeFlowingFluid.Properties properties;

		public Builder(final String name, final DeferredRegister<FluidType> fluidTypes, final DeferredRegister<Fluid> fluids, final DeferredRegister<Block> blocks, final DeferredRegister<Item> items) {
			this.name = name;
			this.fluidTypes = fluidTypes;
			this.fluids = fluids;
			this.blocks = blocks;
			this.items = items;
		}

		public Builder<TYPE, STILL, FLOWING, BLOCK, BUCKET> typeFactory(final Supplier<TYPE> typeFactory) {
			Preconditions.checkNotNull(typeFactory, "typeFactory");
			this.typeFactory = typeFactory;
			return this;
		}

		public Builder<TYPE, STILL, FLOWING, BLOCK, BUCKET> stillFactory(final IFluidFactory<STILL> stillFactory) {
			Preconditions.checkNotNull(stillFactory, "stillFactory");
			this.stillFactory = stillFactory;
			return this;
		}

		public Builder<TYPE, STILL, FLOWING, BLOCK, BUCKET> flowingFactory(final IFluidFactory<FLOWING> flowingFactory) {
			Preconditions.checkNotNull(flowingFactory, "flowingFactory");
			this.flowingFactory = flowingFactory;
			return this;
		}

		public Builder<TYPE, STILL, FLOWING, BLOCK, BUCKET> blockFactory(final IBlockFactory<STILL, BLOCK> blockFactory) {
			Preconditions.checkNotNull(blockFactory, "blockFactory");
			this.blockFactory = blockFactory;
			return this;
		}

		public Builder<TYPE, STILL, FLOWING, BLOCK, BUCKET> bucketFactory(final IBucketFactory<STILL, BUCKET> bucketFactory) {
			Preconditions.checkNotNull(bucketFactory, "bucketFactory");
			this.bucketFactory = bucketFactory;
			return this;
		}

		public Builder<TYPE, STILL, FLOWING, BLOCK, BUCKET> propertiesCustomiser(final Consumer<ForgeFlowingFluid.Properties> propertiesCustomiser) {
			Preconditions.checkNotNull(propertiesCustomiser, "propertiesCustomiser");
			this.propertiesCustomiser = propertiesCustomiser;
			return this;
		}

		public FluidGroup<TYPE, STILL, FLOWING, BLOCK, BUCKET> build() {
			return buildImpl(FluidGroup::new);
		}

		protected <GROUP extends FluidGroup<TYPE, STILL, FLOWING, BLOCK, BUCKET>> GROUP buildImpl(final IFluidGroupFactory<GROUP, TYPE, STILL, FLOWING, BLOCK, BUCKET> factory) {
			Preconditions.checkState(typeFactory != null, "Type Factory not provided");
			Preconditions.checkState(stillFactory != null, "Still Factory not provided");
			Preconditions.checkState(flowingFactory != null, "Flowing factory not provided");
			Preconditions.checkState(blockFactory != null, "Block Factory not provided");
			Preconditions.checkState(bucketFactory != null, "Bucket Factory not provided");

			final RegistryObject<TYPE> type = fluidTypes.register(name, typeFactory);

			final RegistryObject<STILL> still = fluids.register(name, () -> stillFactory.create(Objects.requireNonNull(properties)));
			final RegistryObject<FLOWING> flowing = fluids.register("flowing_" + name, () -> flowingFactory.create(Objects.requireNonNull(properties)));

			final RegistryObject<BLOCK> block = blocks.register(name, () -> blockFactory.create(still));
			final RegistryObject<BUCKET> bucket = items.register(name + "_bucket", () -> bucketFactory.create(still));

			properties = new ForgeFlowingFluid.Properties(type, still, flowing)
					.block(block)
					.bucket(bucket);

			if (propertiesCustomiser != null) {
				propertiesCustomiser.accept(properties);
			}

			return factory.create(type, still, flowing, block, bucket);
		}

		@FunctionalInterface
		protected interface IFluidGroupFactory<
				GROUP extends FluidGroup<TYPE, STILL, FLOWING, BLOCK, BUCKET>,
				TYPE extends FluidType, STILL extends Fluid, FLOWING extends Fluid, BLOCK extends LiquidBlock, BUCKET extends Item
				> {
			GROUP create(RegistryObject<TYPE> type, RegistryObject<STILL> still, RegistryObject<FLOWING> flowing, RegistryObject<BLOCK> block, RegistryObject<BUCKET> bucket);
		}
	}

	public static Block.Properties defaultBlockProperties(final Material material) {
		return Block.Properties.of(material)
				.noCollission()
				.strength(100)
				.noLootTable();
	}

	public static Item.Properties defaultBucketProperties() {
		return new Item.Properties()
				.craftRemainder(Items.BUCKET)
				.stacksTo(1);
	}

	@FunctionalInterface
	public interface IFluidFactory<FLUID extends Fluid> {
		FLUID create(ForgeFlowingFluid.Properties properties);
	}

	@FunctionalInterface
	public interface IBlockFactory<STILL extends Fluid, BLOCK extends Block> {
		BLOCK create(Supplier<? extends STILL> fluid);
	}

	@FunctionalInterface
	public interface IBucketFactory<STILL extends Fluid, BUCKET extends Item> {
		BUCKET create(Supplier<? extends STILL> fluid);
	}
}
