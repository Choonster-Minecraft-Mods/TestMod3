package choonster.testmod3.world.level.block;

import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import choonster.testmod3.util.RegistryUtil;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;

import javax.annotation.Nullable;

/**
 * A Block that prints the current state of the player's held {@link ItemStack}s on the client and server when left- or right-clicked.
 *
 * @author Choonster
 */
public class ItemDebuggerBlock extends Block {
	private static final Logger LOGGER = LogUtils.getLogger();

	public ItemDebuggerBlock(final Block.Properties properties) {
		super(properties);
	}

	private void logItem(final ItemStack stack) {
		if (!stack.isEmpty()) {
			LOGGER.info("ItemStack: {}", stack.serializeNBT());
			logCapability(stack, PigSpawnerCapability.PIG_SPAWNER_CAPABILITY, Direction.NORTH);
			logFluidHandler(stack);

			final ResourceLocation key = RegistryUtil.getKey(stack.getItem());
			final String modName = ModList.get().getModContainerById(key.getNamespace())
					.map(modContainer -> modContainer.getModInfo().getDisplayName())
					.orElse("Unknown - No ModContainer");

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
				LOGGER.info("Fluid: {} - {}", RegistryUtil.getKey(fluidStack.getFluid()), fluidStack.getAmount())
		);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		logItem(player.getItemInHand(hand));

		return InteractionResult.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void attack(final BlockState state, final Level level, final BlockPos pos, final Player player) {
		for (final InteractionHand hand : InteractionHand.values()) {
			logItem(player.getItemInHand(hand));
		}
	}
}
