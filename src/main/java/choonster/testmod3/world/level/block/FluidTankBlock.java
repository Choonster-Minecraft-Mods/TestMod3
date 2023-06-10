package choonster.testmod3.world.level.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.network.FluidTankContentsMessage;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.world.level.block.entity.BaseFluidTankBlockEntity;
import choonster.testmod3.world.level.block.entity.FluidTankBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A Fluid Tank block.
 *
 * @author Choonster
 */
public class FluidTankBlock<TE extends BaseFluidTankBlockEntity> extends BaseEntityBlock<TE> {
	public static final ResourceLocation FLUID_TANK_CONTENTS = new ResourceLocation(TestMod3.MODID, "fluid_tank_contents");

	public FluidTankBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ItemStack> getDrops(final BlockState state, final LootParams.Builder builder) {
		final var blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

		if (blockEntity != null) {
			final var fluidHandler = blockEntity
					.getCapability(ForgeCapabilities.FLUID_HANDLER, null)
					.orElseThrow(CapabilityNotPresentException::new);

			builder.withDynamicDrop(FLUID_TANK_CONTENTS, (stackConsumer) ->
					Stream.of(FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler))
							.map(fluidTankSnapshot -> ModItems.FLUID_STACK_ITEM.get().withFluidStack(fluidTankSnapshot.contents()))
							.forEach(stackConsumer)
			);
		}

		return super.getDrops(state, builder);
	}

	/**
	 * Get the {@link IFluidHandler} from the {@link BlockEntity} at the specified position.
	 *
	 * @param level The level
	 * @param pos   The position
	 * @return A lazy optional containing the IFluidHandler, if it exists
	 */
	private LazyOptional<IFluidHandler> getFluidHandler(final BlockGetter level, final BlockPos pos) {
		return getBlockEntity(level, pos).getCapability(ForgeCapabilities.FLUID_HANDLER, null);
	}

	@Override
	public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
		final var fluidHandler = getFluidHandler(level, pos).orElseThrow(CapabilityNotPresentException::new);
		FluidUtil.tryEmptyContainer(stack, fluidHandler, Integer.MAX_VALUE, null, true);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return new FluidTankBlockEntity(pos, state);
	}

	public static List<Component> getFluidDataForDisplay(final FluidTankSnapshot[] fluidTankSnapshots) {
		final List<Component> data = new ArrayList<>();

		var hasFluid = false;

		for (final var snapshot : fluidTankSnapshots) {
			final var contents = snapshot.contents();
			if (!contents.isEmpty()) {
				hasFluid = true;
				data.add(Component.translatable(TestMod3Lang.BLOCK_DESC_FLUID_TANK_FLUID.getTranslationKey(), contents.getDisplayName(), contents.getAmount(), snapshot.capacity()));
			}
		}

		if (!hasFluid) {
			data.add(Component.translatable(TestMod3Lang.BLOCK_DESC_FLUID_TANK_EMPTY.getTranslationKey()));
		}

		return data;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		final var heldItem = player.getItemInHand(hand);
		return getFluidHandler(level, pos)
				.map(fluidHandler -> {
					// Try fill/empty the held fluid container from the tank
					final var success = FluidUtil.interactWithFluidHandler(player, hand, level, pos, rayTraceResult.getDirection());

					// If the contents changed or this is the off hand, send a chat message to the player
					if (!level.isClientSide && (success || hand == InteractionHand.OFF_HAND)) {
						final var fluidTankSnapshots = FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler);
						TestMod3.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), new FluidTankContentsMessage(fluidTankSnapshots));
					}

					// If the held item is a fluid container, stop processing here so it doesn't try to place its contents
					return heldItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent() ? InteractionResult.SUCCESS : InteractionResult.PASS;
				})
				.orElse(InteractionResult.PASS);
	}
}
