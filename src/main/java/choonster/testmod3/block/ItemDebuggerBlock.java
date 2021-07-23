package choonster.testmod3.block;

import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

/**
 * A Block that prints the current state of the player's held {@link ItemStack}s on the client and server when left or right clicked.
 *
 * @author Choonster
 */
public class ItemDebuggerBlock extends Block {
	private static final Logger LOGGER = LogManager.getLogger();

	public ItemDebuggerBlock(final Block.Properties properties) {
		super(properties);
	}

	private void logItem(final ItemStack stack) {
		if (!stack.isEmpty()) {
			LOGGER.info("ItemStack: {}", stack.serializeNBT());
			logCapability(stack, PigSpawnerCapability.PIG_SPAWNER_CAPABILITY, Direction.NORTH);
			logFluidHandler(stack);

			final String modName;
			final ResourceLocation registryName = stack.getItem().getRegistryName();
			if (registryName != null) {

				modName = ModList.get().getModContainerById(registryName.getNamespace())
						.map(modContainer -> modContainer.getModInfo().getDisplayName())
						.orElse("Unknown - No ModContainer");
			} else {
				modName = "Unknown - No Registry Name";
			}

			LOGGER.info("Mod Name: {}", modName);
		}
	}

	private <T> void logCapability(final ItemStack stack, final Capability<T> capability, @Nullable final Direction facing) {
		stack.getCapability(capability, facing).ifPresent(instance ->
				LOGGER.info("Capability: {} - {}", capability.getName(), instance)
		);
	}

	private void logFluidHandler(final ItemStack stack) {
		FluidUtil.getFluidContained(stack).ifPresent(fluidStack ->
				LOGGER.info("Fluid: {} - {}", fluidStack.getFluid().getRegistryName(), fluidStack.getAmount())
		);
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		logItem(player.getItemInHand(hand));

		return ActionResultType.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void attack(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player) {
		for (final Hand hand : Hand.values()) {
			logItem(player.getItemInHand(hand));
		}
	}
}
