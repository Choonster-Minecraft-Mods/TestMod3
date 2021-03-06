package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.network.FluidTankContentsMessage;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.tileentity.BaseFluidTankTileEntity;
import choonster.testmod3.tileentity.FluidTankTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * A Fluid Tank block.
 *
 * @author Choonster
 */
public class FluidTankBlock<TE extends BaseFluidTankTileEntity> extends TileEntityBlock<TE> {
	public static final ResourceLocation FLUID_TANK_CONTENTS = new ResourceLocation(TestMod3.MODID, "fluid_tank_contents");

	public FluidTankBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ItemStack> getDrops(final BlockState state, final LootContext.Builder builder) {
		final TileEntity tileentity = builder.get(LootParameters.BLOCK_ENTITY);

		if (tileentity != null) {
			tileentity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)
					.ifPresent(fluidHandler ->
							builder.withDynamicDrop(FLUID_TANK_CONTENTS, (context, stackConsumer) ->
									Stream.of(FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler))
											.map(fluidTankSnapshot -> ModItems.FLUID_STACK_ITEM.get().withFluidStack(fluidTankSnapshot.getContents()))
											.forEach(stackConsumer)
							)
					);
		}

		return super.getDrops(state, builder);
	}

	/**
	 * Get the {@link IFluidHandler} from the {@link TileEntity} at the specified position.
	 *
	 * @param world The world
	 * @param pos   The position
	 * @return A lazy optional containing the IFluidHandler, if it exists
	 */
	private LazyOptional<IFluidHandler> getFluidHandler(final IBlockReader world, final BlockPos pos) {
		return getTileEntity(world, pos).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
	}

	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
		getFluidHandler(worldIn, pos).ifPresent(fluidHandler ->
				FluidUtil.tryEmptyContainer(stack, fluidHandler, Integer.MAX_VALUE, null, true)
		);
	}

	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		return new FluidTankTileEntity();
	}

	public static List<ITextComponent> getFluidDataForDisplay(final FluidTankSnapshot[] fluidTankSnapshots) {
		final List<ITextComponent> data = new ArrayList<>();

		boolean hasFluid = false;

		for (final FluidTankSnapshot snapshot : fluidTankSnapshots) {
			final FluidStack contents = snapshot.getContents();
			if (!contents.isEmpty()) {
				hasFluid = true;
				data.add(new TranslationTextComponent(TestMod3Lang.BLOCK_DESC_FLUID_TANK_FLUID.getTranslationKey(), contents.getDisplayName(), contents.getAmount(), snapshot.getCapacity()));
			}
		}

		if (!hasFluid) {
			data.add(new TranslationTextComponent(TestMod3Lang.BLOCK_DESC_FLUID_TANK_EMPTY.getTranslationKey()));
		}

		return data;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final ItemStack heldItem = player.getHeldItem(hand);
		return getFluidHandler(world, pos)
				.map(fluidHandler -> {
					// Try fill/empty the held fluid container from the tank
					final boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, rayTraceResult.getFace());

					// If the contents changed or this is the off hand, send a chat message to the player
					if (!world.isRemote && (success || hand == Hand.OFF_HAND)) {
						final FluidTankSnapshot[] fluidTankSnapshots = FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler);
						TestMod3.network.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new FluidTankContentsMessage(fluidTankSnapshots));
					}

					// If the held item is a fluid container, stop processing here so it doesn't try to place its contents
					return heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() ? ActionResultType.SUCCESS : ActionResultType.PASS;
				})
				.orElse(ActionResultType.PASS);
	}
}
