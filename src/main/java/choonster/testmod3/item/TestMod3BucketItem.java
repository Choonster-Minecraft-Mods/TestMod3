package choonster.testmod3.item;

import net.minecraft.item.Item;

/**
 * A copy of Forge's Universal Bucket that uses a single {@link Item} for both the full and empty buckets.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/60374-re-skinned-universal-bucket/
 *
 * @author Choonster
 */
/*
public class TestMod3BucketItem extends UniversalBucket {
	private final ItemStack empty = new ItemStack(this);

	public TestMod3BucketItem(final Item.Properties properties) {
		this(Fluid.BUCKET_VOLUME, properties);
	}

	public TestMod3BucketItem(final int capacity, final Properties properties) {
		super(properties, capacity, ItemStack.EMPTY, true);
	}

	@Override
	public void fillItemGroup(@Nullable final ItemGroup group, @Nonnull final NonNullList<ItemStack> subItems) {
		if (!isInGroup(group)) return;

		subItems.add(empty);

		*/
/*
		// TODO: Uncomment when fluids are reimplemented in 1.14
		for (final Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			// Add all fluids that the bucket can be filled with
			final FluidStack fs = new FluidStack(fluid, getCapacity());
			final ItemStack stack = new ItemStack(this);
			final IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			if (fluidHandler != null && fluidHandler.fill(fs, true) == fs.amount) {
				final ItemStack filled = fluidHandler.getContainer();
				subItems.add(filled);
			}
		}
		*//*

	}

	@Override
	public ITextComponent getDisplayName(@Nonnull final ItemStack stack) {
		final FluidStack fluidStack = getFluid(stack);
		final String translationKey = getTranslationKey(stack);

		// If the bucket is empty, translate the translation key directly
		if (fluidStack == null) {
			return new TranslationTextComponent(translationKey);
		}

		// If there's a fluid-specific translation, use it
		final String fluidTranslationKey = translationKey + ".filled." + fluidStack.getFluid().getName();

		// TODO: This is not reliable on the server. Watch for updates to the super method in Forge.
		if (LanguageMap.getInstance().exists(fluidTranslationKey)) {
			return new TranslationTextComponent(fluidTranslationKey);
		}

		// Else translate the filled name directly, formatting it with the fluid name
		return new TranslationTextComponent(translationKey + ".filled", fluidStack.getLocalizedName());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);
		final FluidStack fluidStack = getFluid(heldItem);

		// If the bucket is full, call the super method to try and empty it
		if (fluidStack != null) return super.onItemRightClick(world, player, hand);

		// If the bucket is empty, try and fill it
		final RayTraceResult rayTraceResult = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

		if (rayTraceResult.getType() != RayTraceResult.Type.BLOCK) {
			return new ActionResult<>(ActionResultType.PASS, heldItem);
		}

		final BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;

		final BlockPos pos = blockRayTraceResult.getPos();

		final ItemStack singleBucket = heldItem.copy();
		singleBucket.setCount(1);

		final FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, player, world, pos, blockRayTraceResult.getFace());
		if (filledResult.isSuccess()) {
			final ItemStack filledBucket = filledResult.result;

			if (player.abilities.isCreativeMode)
				return new ActionResult<>(ActionResultType.SUCCESS, heldItem);

			heldItem.shrink(1);
			if (heldItem.isEmpty())
				return new ActionResult<>(ActionResultType.SUCCESS, filledBucket);

			ItemHandlerHelper.giveItemToPlayer(player, filledBucket);

			return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
		}

		return new ActionResult<>(ActionResultType.PASS, heldItem);
	}


	@Override
	public ItemStack getEmpty() {
		return empty;
	}

	@Nullable
	@Override
	public FluidStack getFluid(final ItemStack container) {
		return FluidUtil.getFluidContained(container).orElse(null);
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, final CompoundNBT nbt) {
		if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY == null) return null;

		// FluidBucketWrapper only works with Forge's UniversalBucket instance, use a different IFluidHandlerItem implementation instead
		return new UniversalBucketFluidHandler(stack, getCapacity());
	}
}
*/
