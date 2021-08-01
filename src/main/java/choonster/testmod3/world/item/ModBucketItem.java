package choonster.testmod3.world.item;

import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.fluid.UniversalBucketFluidHandler;
import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.util.RegistryUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

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
		final FluidActionResult fillResult = ModFluidUtil.fillContainer(stack, fluidStack);
		return fillResult.isSuccess() ? fillResult.getResult() : ItemStack.EMPTY;
	}

	@Override
	public int getItemStackLimit(final ItemStack stack) {
		return getFluid(stack).isEmpty() ? super.getItemStackLimit(stack) : 1;
	}

	@Override
	public void fillItemCategory(final CreativeModeTab group, final NonNullList<ItemStack> items) {
		if (!allowdedIn(group)) return;

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
	public Component getName(final ItemStack stack) {
		final FluidStack fluidStack = getFluid(stack);
		final String translationKey = getDescriptionId(stack);

		// If the bucket is empty, translate the translation key directly
		if (fluidStack.isEmpty()) {
			return new TranslatableComponent(translationKey);
		}

		// If there's a fluid-specific translation, use it
		final String fluidTranslationKey = translationKey + ".filled." + fluidStack.getTranslationKey();

		if (Language.getInstance().has(fluidTranslationKey)) {
			return new TranslatableComponent(fluidTranslationKey);
		}

		// Else translate the filled name directly, formatting it with the fluid name
		return new TranslatableComponent(translationKey + ".filled", fluidStack.getDisplayName());
	}

	@Override
	public boolean hasContainerItem(final ItemStack stack) {
		return !getFluid(stack).isEmpty();
	}

	@Override
	public ItemStack getContainerItem(final ItemStack stack) {
		return empty.copy();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);
		final FluidStack fluidStack = getFluid(heldItem);
		final boolean isEmpty = fluidStack.isEmpty();

		final BlockHitResult rayTrace = getPlayerPOVHitResult(world, player, isEmpty ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

		final InteractionResultHolder<ItemStack> eventResult = ForgeEventFactory.onBucketUse(player, world, heldItem, rayTrace);
		if (eventResult != null) {
			return eventResult;
		}

		if (rayTrace.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(heldItem);
		}

		final BlockPos pos = rayTrace.getBlockPos();
		final Direction direction = rayTrace.getDirection();
		final BlockPos adjacentPos = pos.relative(direction);

		if (!world.mayInteract(player, pos) || !player.mayUseItemAt(adjacentPos, direction, heldItem)) {
			return InteractionResultHolder.fail(heldItem);
		}

		final ItemStack result;

		if (isEmpty) {
			final FluidActionResult pickUpResult = FluidUtil.tryPickUpFluid(heldItem, player, world, pos, direction);

			if (!pickUpResult.isSuccess()) {
				return InteractionResultHolder.fail(heldItem);
			}

			final ItemStack filledBucket = pickUpResult.getResult();

			if (!world.isClientSide()) {
				CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, filledBucket);
			}

			// Add the filled bucket to the player's inventory, or replace this stack if this was the last empty bucket
			result = ItemUtils.createFilledResult(heldItem, player, filledBucket);
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
				return InteractionResultHolder.fail(heldItem);
			}

			if (!world.isClientSide()) {
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, placePos, heldItem);
			}

			// Return an empty bucket, or the original held item if the player is in Creative Mode
			result = !player.getAbilities().instabuild ? empty.copy() : heldItem;
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		return InteractionResultHolder.sidedSuccess(result, world.isClientSide());
	}

	private Pair<FluidActionResult, BlockPos> tryPlaceContainedFluid(
			@Nullable final Player player, final InteractionHand hand, final ItemStack container, final FluidStack fluidStack,
			final Level world, final BlockPos pos, final BlockPos originalPos, final Direction direction, final boolean tryAdjacentBlock
	) {
		final Fluid fluid = fluidStack.getFluid();

		if (world.dimensionType().ultraWarm() && fluid.getAttributes().doesVaporize(world, pos, fluidStack)) {

			fluid.getAttributes().vaporize(player, world, pos, fluidStack);

			return Pair.of(new FluidActionResult(empty.copy()), pos);
		}

		// If the fluid is a flowing fluid,
		if (fluid instanceof FlowingFluid) {
			final BlockState destState = world.getBlockState(pos);
			final Block destBlock = destState.getBlock();

			// Try to place the fluid in a Vanilla ILiquidContainer block
			if (destBlock instanceof LiquidBlockContainer && ((LiquidBlockContainer) destBlock).canPlaceLiquid(world, pos, destState, fluid)) {
				((LiquidBlockContainer) destBlock).placeLiquid(world, pos, destState, ((FlowingFluid) fluid).getSource(false));

				final SoundEvent soundEvent = fluid.getAttributes().getEmptySound(fluidStack);
				world.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1, 1);

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
					world, originalPos.relative(direction), originalPos, direction, false
			);
		}

		return Pair.of(FluidActionResult.FAILURE, pos);
	}

	protected FluidStack getFluid(final ItemStack container) {
		return FluidUtil.getFluidContained(container).orElse(FluidStack.EMPTY);
	}

	private boolean canBlockContainFluid(
			final Level level, final BlockPos posIn, final Direction direction,
			final BlockState blockState, final FluidStack fluidStack
	) {
		// If the block implements ILiquidContainer, check if it can contain the fluid
		if (blockState.getBlock() instanceof LiquidBlockContainer) {
			return ((LiquidBlockContainer) blockState.getBlock()).canPlaceLiquid(level, posIn, blockState, fluidStack.getFluid());
		}

		// Otherwise, check if there's an IFluidHandler that can be filled with the entire FluidStack
		return FluidUtil.getFluidHandler(level, posIn, direction)
				.map(fluidHandler -> fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) == fluidStack.getAmount())
				.orElse(false);
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
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
