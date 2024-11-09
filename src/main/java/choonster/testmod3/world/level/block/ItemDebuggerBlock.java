package choonster.testmod3.world.level.block;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.util.RegistryUtil;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;

/**
 * A Block that prints the current state of the player's held {@link ItemStack}s on the client and server when left- or right-clicked.
 *
 * @author Choonster
 */
public class ItemDebuggerBlock extends Block {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final MapCodec<ItemDebuggerBlock> CODEC = simpleCodec(ItemDebuggerBlock::new);

	public ItemDebuggerBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	private void logItem(final Level level, final ItemStack stack) {
		if (!stack.isEmpty()) {
			LOGGER.info("ItemStack: {}", stack.save(level.registryAccess()));
			logComponent(stack, ModDataComponents.PIG_SPAWNER.get());
			logFluidHandler(stack);

			final var key = RegistryUtil.getKey(stack.getItem());
			final var modName = ModList.get().getModContainerById(key.getNamespace())
					.map(modContainer -> modContainer.getModInfo().getDisplayName())
					.orElse("Unknown - No ModContainer");

			LOGGER.info("Mod Name: {}", modName);
		}
	}

	private <T> void logComponent(final ItemStack stack, final DataComponentType<T> componentType) {
		final var component = stack.get(componentType);

		if (component != null) {
			LOGGER.info("Component: {} - {}", RegistryUtil.getKey(componentType), component);
		}
	}

	private void logFluidHandler(final ItemStack stack) {
		FluidUtil.getFluidContained(stack).ifPresent(fluidStack ->
				LOGGER.info("Fluid: {} - {}", RegistryUtil.getKey(fluidStack.getFluid()), fluidStack.getAmount())
		);
	}

	@Override
	protected ItemInteractionResult useItemOn(final ItemStack stack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult blockHitResult) {
		logItem(level, player.getItemInHand(hand));

		return ItemInteractionResult.SUCCESS;
	}

	@Override
	public void attack(final BlockState state, final Level level, final BlockPos pos, final Player player) {
		for (final var hand : InteractionHand.values()) {
			logItem(level, player.getItemInHand(hand));
		}
	}
}
