package choonster.testmod3.world.item;

import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.fluid.UniversalBucketFluidHandler;
import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.util.RegistryUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

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
	private final int capacity;

	public ModBucketItem(final Item.Properties properties) {
		this(FluidType.BUCKET_VOLUME, properties);
	}

	public ModBucketItem(final int capacity, final Item.Properties properties) {
		super(properties);
		this.capacity = capacity;
	}

	public ItemStack getFilledBucket(final FluidStack fluidStack) {
		final var stack = new ItemStack(this);
		final var fillResult = ModFluidUtil.fillContainer(stack, fluidStack);
		return fillResult.isSuccess() ? fillResult.getResult() : ItemStack.EMPTY;
	}

	@Override
	public int getMaxStackSize(final ItemStack stack) {
		return getFluid(stack).isEmpty() ? super.getMaxStackSize(stack) : 1;
	}

	public void fillCreativeModeTab(final MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries) {
		final var empty = empty();

		// Add all fluids that the bucket can be filled with
		RegistryUtil.stream(ForgeRegistries.FLUIDS)
				.map(fluid -> new FluidStack(fluid, capacity))
				.filter(ModFluidUtil::hasBucket)
				.map(this::getFilledBucket)
				.filter(stack -> !stack.isEmpty())
				.forEach(stack -> entries.putAfter(empty, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
	}

	@Override
	public Component getName(final ItemStack stack) {
		final var fluidStack = getFluid(stack);
		final var translationKey = getDescriptionId(stack);

		// If the bucket is empty, translate the translation key directly
		if (fluidStack.isEmpty()) {
			return Component.translatable(translationKey);
		}

		// If there's a fluid-specific translation, use it
		final var fluidTranslationKey = translationKey + ".filled." + fluidStack.getTranslationKey();

		if (Language.getInstance().has(fluidTranslationKey)) {
			return Component.translatable(fluidTranslationKey);
		}

		// Else translate the filled name directly, formatting it with the fluid name
		return Component.translatable(translationKey + ".filled", fluidStack.getDisplayName());
	}

	@Override
	public boolean hasCraftingRemainingItem(final ItemStack stack) {
		return !getFluid(stack).isEmpty();
	}

	@Override
	public ItemStack getCraftingRemainingItem(final ItemStack itemStack) {
		return empty();
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);
		final var fluidStack = getFluid(heldItem);
		final var isEmpty = fluidStack.isEmpty();

		final var rayTrace = getPlayerPOVHitResult(level, player, isEmpty ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

		final var eventResult = ForgeEventFactory.onBucketUse(player, level, heldItem, rayTrace);
		if (eventResult != null) {
			return eventResult;
		}

		if (rayTrace.getType() != HitResult.Type.BLOCK) {
			return InteractionResultHolder.pass(heldItem);
		}

		final var pos = rayTrace.getBlockPos();
		final var direction = rayTrace.getDirection();
		final var adjacentPos = pos.relative(direction);

		if (!level.mayInteract(player, pos) || !player.mayUseItemAt(adjacentPos, direction, heldItem)) {
			return InteractionResultHolder.fail(heldItem);
		}

		final ItemStack result;

		if (isEmpty) {
			final var pickUpResult = FluidUtil.tryPickUpFluid(heldItem, player, level, pos, direction);

			if (!pickUpResult.isSuccess()) {
				return InteractionResultHolder.fail(heldItem);
			}

			final var filledBucket = pickUpResult.getResult();

			if (!level.isClientSide()) {
				CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, filledBucket);
			}

			// Add the filled bucket to the player's inventory, or replace this stack if this was the last empty bucket
			result = ItemUtils.createFilledResult(heldItem, player, filledBucket);
		} else {
			final var destState = level.getBlockState(pos);
			final var destPos = canBlockContainFluid(level, pos, direction, destState, fluidStack) ? pos : adjacentPos;

			final var placeResultPair = tryPlaceContainedFluid(
					player, hand, heldItem, fluidStack,
					level, destPos, pos, direction, true
			);

			final var placeResult = placeResultPair.getFirst();
			final var placePos = placeResultPair.getSecond();

			if (!placeResult.isSuccess()) {
				return InteractionResultHolder.fail(heldItem);
			}

			if (!level.isClientSide()) {
				CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, placePos, heldItem);
			}

			// Return an empty bucket, or the original held item if the player is in Creative Mode
			result = !player.getAbilities().instabuild ? empty() : heldItem;
		}

		player.awardStat(Stats.ITEM_USED.get(this));

		return InteractionResultHolder.sidedSuccess(result, level.isClientSide());
	}

	private Pair<FluidActionResult, BlockPos> tryPlaceContainedFluid(
			@Nullable final Player player, final InteractionHand hand, final ItemStack container, final FluidStack fluidStack,
			final Level level, final BlockPos pos, final BlockPos originalPos, final Direction direction, final boolean tryAdjacentBlock
	) {
		final var fluid = fluidStack.getFluid();

		if (level.dimensionType().ultraWarm() && fluid.getFluidType().isVaporizedOnPlacement(level, pos, fluidStack)) {

			fluid.getFluidType().onVaporize(player, level, pos, fluidStack);

			return Pair.of(new FluidActionResult(empty()), pos);
		}

		// If the fluid is a flowing fluid,
		if (fluid instanceof FlowingFluid) {
			final var destState = level.getBlockState(pos);
			final var destBlock = destState.getBlock();

			// Try to place the fluid in a Vanilla ILiquidContainer block
			if (destBlock instanceof LiquidBlockContainer && ((LiquidBlockContainer) destBlock).canPlaceLiquid(level, pos, destState, fluid)) {
				((LiquidBlockContainer) destBlock).placeLiquid(level, pos, destState, ((FlowingFluid) fluid).getSource(false));

				final var soundEvent = fluid.getFluidType().getSound(fluidStack, SoundActions.BUCKET_EMPTY);
				if (soundEvent != null) {
					level.playSound(player, pos, soundEvent, SoundSource.BLOCKS, 1, 1);
				}

				return Pair.of(new FluidActionResult(empty()), pos);
			}
		}

		// Try place the fluid in level or in an IFluidHandler block
		final var placeResult = FluidUtil.tryPlaceFluid(player, level, hand, pos, container, fluidStack);

		if (placeResult.isSuccess()) {
			return Pair.of(placeResult, pos);
		}

		// If this is the first attempt, try to place the fluid in the adjacent block space
		if (tryAdjacentBlock) {
			return tryPlaceContainedFluid(
					player, hand, container, fluidStack,
					level, originalPos.relative(direction), originalPos, direction, false
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
		if (ForgeCapabilities.FLUID_HANDLER_ITEM == null) {
			return null;
		}

		return new SerializableCapabilityProvider<>(
				ForgeCapabilities.FLUID_HANDLER_ITEM,
				null,
				new UniversalBucketFluidHandler(stack, capacity)
		);
	}

	private ItemStack empty() {
		return new ItemStack(this);
	}
}
