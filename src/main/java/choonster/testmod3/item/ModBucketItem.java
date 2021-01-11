package choonster.testmod3.item;

import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.fluid.UniversalBucketFluidHandler;
import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A bucket that uses a single {@link Item} for all possible fluids (including the empty fluid).
 * <p>
 * The contained fluid is stored as a {@link FluidStack} in-memory rather than being written to the {@link ItemStack}'s
 * NBT.
 * <p>
 * Test for this thread:
 * https://forums.minecraftforge.net/topic/60374-re-skinned-universal-bucket/
 *
 * @author Choonster
 */
public class ModBucketItem extends Item {
	private final ItemStack empty = new ItemStack(this);
	private final int capacity;

	public ModBucketItem(final Item.Properties properties) {
		this(FluidAttributes.BUCKET_VOLUME, properties);
	}

	public ModBucketItem(final int capacity, final Item.Properties properties) {
		super(properties);
		this.capacity = capacity;
	}

	public ItemStack getFilledBucket(final FluidStack fluidStack) {
		final ItemStack stack = new ItemStack(this);
		final FluidActionResult fillResult = fill(stack, fluidStack);
		return fillResult.isSuccess() ? fillResult.getResult() : ItemStack.EMPTY;
	}

	@Override
	public int getItemStackLimit(final ItemStack stack) {
		return getFluid(stack).isEmpty() ? super.getItemStackLimit(stack) : 1;
	}

	@Override
	public void fillItemGroup(final ItemGroup group, final NonNullList<ItemStack> items) {
		if (!isInGroup(group)) return;

		items.add(empty);

		if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY == null) {
			return;
		}

		// Add all fluids that the bucket can be filled with
		RegistryUtil.stream(ForgeRegistries.FLUIDS)
				.map(fluid -> new FluidStack(fluid, capacity))
				.filter(ModFluidUtil::hasBucket)
				.map(this::getFilledBucket)
				.filter(stack -> !stack.isEmpty())
				.forEach(items::add);
	}

	@Override
	public ITextComponent getDisplayName(final ItemStack stack) {
		final FluidStack fluidStack = getFluid(stack);
		final String translationKey = getTranslationKey(stack);

		// If the bucket is empty, translate the translation key directly
		if (fluidStack.isEmpty()) {
			return new TranslationTextComponent(translationKey);
		}

		// If there's a fluid-specific translation, use it
		final String fluidTranslationKey = translationKey + ".filled." + fluidStack.getTranslationKey();

		if (LanguageMap.getInstance()./* exists */func_230506_b_(fluidTranslationKey)) {
			return new TranslationTextComponent(fluidTranslationKey);
		}

		// Else translate the filled name directly, formatting it with the fluid name
		return new TranslationTextComponent(translationKey + ".filled", fluidStack.getDisplayName());
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);
		final FluidStack fluidStack = getFluid(heldItem);
		final boolean isEmpty = fluidStack.isEmpty();

		final BlockRayTraceResult rayTrace = rayTrace(world, player, isEmpty ? RayTraceContext.FluidMode.SOURCE_ONLY : RayTraceContext.FluidMode.NONE);

		final ActionResult<ItemStack> eventResult = ForgeEventFactory.onBucketUse(player, world, heldItem, rayTrace);
		if (eventResult != null) {
			return eventResult;
		}

		if (rayTrace.getType() != RayTraceResult.Type.BLOCK) {
			return ActionResult.resultPass(heldItem);
		}

		final BlockPos pos = rayTrace.getPos();
		final Direction direction = rayTrace.getFace();
		final BlockPos adjacentPos = pos.offset(direction);

		if (!world.isBlockModifiable(player, pos) || !player.canPlayerEdit(adjacentPos, direction, heldItem)) {
			return ActionResult.resultFail(heldItem);
		}

		final ItemStack result;

		if (isEmpty) {
			final FluidActionResult pickUpResult = tryPickUpFluid(player, heldItem, world, pos, direction);

			if (!pickUpResult.isSuccess()) {
				return ActionResult.resultFail(heldItem);
			}

			final ItemStack filledBucket = pickUpResult.getResult();

			if (!world.isRemote()) {
				CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) player, filledBucket);
			}

			// Add the filled bucket to the player's inventory, or replace this stack if this was the last empty bucket
			result = DrinkHelper.fill(heldItem, player, filledBucket);
		} else {
			final BlockState destState = world.getBlockState(pos);
			final BlockPos destPos = canBlockContainFluid(world, pos, direction, destState, fluidStack) ? pos : adjacentPos;

			final Pair<FluidActionResult, BlockPos> placeResultPair = tryPlaceContainedFluid(
					player, hand, heldItem, fluidStack,
					world, destPos, pos, direction, true
			);

			final FluidActionResult placeResult = placeResultPair.getFirst();
			final BlockPos placePos = placeResultPair.getSecond();

			if (!placeResult.isSuccess()) {
				return ActionResult.resultFail(heldItem);
			}

			if (!world.isRemote()) {
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, placePos, heldItem);
			}

			// Return an empty bucket, or the original held item if the player is in Creative Mode
			result = !player.abilities.isCreativeMode ? empty.copy() : heldItem;
		}

		player.addStat(Stats.ITEM_USED.get(this));

		return ActionResult.func_233538_a_(result, world.isRemote());
	}

	private FluidActionResult tryPickUpFluid(
			@Nullable final PlayerEntity player, final ItemStack container,
			final World world, final BlockPos pos, final Direction direction
	) {
		final BlockState destState = world.getBlockState(pos);
		final Block destBlock = destState.getBlock();

		// Try to pick up fluid from an IFluidBlock (not many blocks are likely to implement this in current versions)
		final FluidActionResult pickUpResult = FluidUtil.tryPickUpFluid(container, player, world, pos, direction);

		if (pickUpResult.isSuccess()) {
			return pickUpResult;
		}

		// Try to pick up fluid from an IBucketPickupHandler block.
		// This includes FlowingFluidBlock/ForgeFlowingFluidBlock used by Vanilla and likely most modded fluids.
		if (destBlock instanceof IBucketPickupHandler) {
			world.captureBlockSnapshots = true;
			final Fluid fluid = ((IBucketPickupHandler) destBlock).pickupFluid(world, pos, destState);
			world.captureBlockSnapshots = false;

			@SuppressWarnings("unchecked")
			final List<BlockSnapshot> blockSnapshots = (List<BlockSnapshot>) world.capturedBlockSnapshots.clone();
			world.capturedBlockSnapshots.clear();

			if (fluid != Fluids.EMPTY) {
				final FluidStack fluidStack = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);

				final FluidActionResult fillResult = fill(container.copy(), fluidStack);
				if (fillResult.isSuccess()) {
					final SoundEvent soundEvent = fluid.getAttributes().getFillSound(fluidStack);
					world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1, 1);

					return fillResult;
				}

				// If the bucket wasn't filled, restore the original state
				for (final BlockSnapshot blockSnapshot : Lists.reverse(blockSnapshots)) {
					world.restoringBlockSnapshots = true;
					blockSnapshot.restore(true, false);
					world.restoringBlockSnapshots = false;
				}
			}
		}

		return FluidActionResult.FAILURE;
	}

	private Pair<FluidActionResult, BlockPos> tryPlaceContainedFluid(
			@Nullable final PlayerEntity player, final Hand hand, final ItemStack container, final FluidStack fluidStack,
			final World world, final BlockPos pos, final BlockPos originalPos, final Direction direction, final boolean tryAdjacentBlock
	) {
		final Fluid fluid = fluidStack.getFluid();

		if (world.getDimensionType().isUltrawarm() && fluid.getAttributes().doesVaporize(world, pos, fluidStack)) {

			fluid.getAttributes().vaporize(player, world, pos, fluidStack);

			return Pair.of(new FluidActionResult(empty.copy()), pos);
		}

		// If the fluid is a flowing fluid,
		if (fluid instanceof FlowingFluid) {
			final BlockState destState = world.getBlockState(pos);
			final Block destBlock = destState.getBlock();

			// Try to place the fluid in a Vanilla ILiquidContainer block
			if (destBlock instanceof ILiquidContainer && ((ILiquidContainer) destBlock).canContainFluid(world, pos, destState, fluid)) {
				((ILiquidContainer) destBlock).receiveFluid(world, pos, destState, ((FlowingFluid) fluid).getStillFluidState(false));

				final SoundEvent soundEvent = fluid.getAttributes().getEmptySound(fluidStack);
				world.playSound(player, pos, soundEvent, SoundCategory.BLOCKS, 1, 1);

				return Pair.of(new FluidActionResult(empty.copy()), pos);
			}
		}

		// Try place the fluid in world or in an IFluidHandler block
		final FluidActionResult placeResult = FluidUtil.tryPlaceFluid(player, world, hand, pos, container, fluidStack);

		if (placeResult.isSuccess()) {
			return Pair.of(placeResult, pos);
		}

		// If this is the first attempt, try to place the fluid in the adjacent block space
		if (tryAdjacentBlock) {
			return tryPlaceContainedFluid(
					player, hand, container, fluidStack,
					world, originalPos.offset(direction), originalPos, direction, false
			);
		}

		return Pair.of(FluidActionResult.FAILURE, pos);
	}

	protected FluidStack getFluid(final ItemStack container) {
		return FluidUtil.getFluidContained(container).orElse(FluidStack.EMPTY);
	}

	private FluidActionResult fill(final ItemStack container, final FluidStack fluidStack) {
		final IFluidHandlerItem fluidHandler = FluidUtil
				.getFluidHandler(container)
				.orElseThrow(CapabilityNotPresentException::new);

		final int originalAmount = fluidStack.getAmount();

		final int amountFilled = fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

		if (amountFilled != originalAmount) {
			return FluidActionResult.FAILURE;
		}

		return new FluidActionResult(fluidHandler.getContainer());
	}

	private boolean canBlockContainFluid(
			final World worldIn, final BlockPos posIn, final Direction direction,
			final BlockState blockState, final FluidStack fluidStack
	) {
		// If the block implements ILiquidContainer, check if it can contain the fluid
		if (blockState.getBlock() instanceof ILiquidContainer) {
			return ((ILiquidContainer) blockState.getBlock()).canContainFluid(worldIn, posIn, blockState, fluidStack.getFluid());
		}

		// Otherwise check if there's an IFluidHandler that can be filled with the entire FluidStack
		return FluidUtil.getFluidHandler(worldIn, posIn, direction)
				.map(fluidHandler -> fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) == fluidStack.getAmount())
				.orElse(false);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
		if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY == null) {
			return null;
		}

		return new SerializableCapabilityProvider<>(
				CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY,
				null,
				new UniversalBucketFluidHandler(stack, capacity)
		);
	}
}
