package choonster.testmod3.block;

import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A Block that prints the current state of the player's held {@link ItemStack}s on the client and server when left or right clicked.
 *
 * @author Choonster
 */
public class BlockItemDebugger extends Block {
	private static final Logger LOGGER = LogManager.getLogger();

	public BlockItemDebugger(final Block.Properties properties) {
		super(properties);
	}

	private void logItem(final ItemStack stack) {
		if (!stack.isEmpty()) {
			LOGGER.info("ItemStack: {}", stack.serializeNBT());
			logCapability(stack, CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY, EnumFacing.NORTH);

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

	private <T> void logCapability(final ItemStack stack, final Capability<T> capability, final EnumFacing facing) {
		stack.getCapability(capability).ifPresent(instance -> {
			LOGGER.info("Capability: {} - {}", capability.getName(), instance);
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World worldIn, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		logItem(player.getHeldItem(hand));

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockClicked(final IBlockState state, final World worldIn, final BlockPos pos, final EntityPlayer player) {
		for (final EnumHand hand : EnumHand.values()) {
			logItem(player.getHeldItem(hand));
		}
	}
}
