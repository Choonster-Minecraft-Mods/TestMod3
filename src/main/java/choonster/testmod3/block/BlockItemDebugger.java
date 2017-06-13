package choonster.testmod3.block;

import choonster.testmod3.Logger;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

/**
 * A Block that prints the current state of the player's held {@link ItemStack}s on the client and server when left or right clicked.
 *
 * @author Choonster
 */
public class BlockItemDebugger extends BlockTestMod3 {
	public BlockItemDebugger() {
		super(Material.IRON, "item_debugger");
		setBlockUnbreakable();
	}

	private void logItem(final ItemStack stack) {
		if (!stack.isEmpty()) {
			Logger.info("ItemStack: %s", stack.serializeNBT());
			logCapability(stack, CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY, EnumFacing.NORTH);

			final String modName;
			final ResourceLocation registryName = stack.getItem().getRegistryName();
			if (registryName != null) {
				final ModContainer modContainer = Loader.instance().getIndexedModList().get(registryName.getResourceDomain());

				if (modContainer != null) {
					modName = modContainer.getName();
				} else {
					modName = "Unknown - No ModContainer";
				}
			} else {
				modName = "Unknown - No Registry Name";
			}

			Logger.info("Mod Name: %s", modName);
		}
	}

	private <T> void logCapability(final ItemStack stack, final Capability<T> capability, final EnumFacing facing) {
		if (stack.hasCapability(capability, facing)) {
			final T instance = stack.getCapability(capability, facing);
			Logger.info("Capability: %s - %s", capability.getName(), instance);
		}
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		logItem(playerIn.getHeldItem(hand));

		return true;
	}

	@Override
	public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
		for (final EnumHand hand : EnumHand.values()) {
			logItem(playerIn.getHeldItem(hand));
		}
	}
}
