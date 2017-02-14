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

	private void logItem(ItemStack stack) {
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

	private <T> void logCapability(ItemStack stack, Capability<T> capability, EnumFacing facing) {
		if (stack.hasCapability(capability, facing)) {
			final T instance = stack.getCapability(capability, facing);
			Logger.info("Capability: %s - %s", capability.getName(), instance);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		logItem(playerIn.getHeldItem(hand));

		return true;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		for (final EnumHand hand : EnumHand.values()) {
			logItem(playerIn.getHeldItem(hand));
		}
	}
}
