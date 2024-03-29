package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.CapabilityContainerListenerManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;

/**
 * Items with this capability reveal hidden blocks.
 *
 * @author Choonster
 */
public final class HiddenBlockRevealerCapability {
	/**
	 * The {@link Capability} instance.
	 */
	public static final Capability<IHiddenBlockRevealer> HIDDEN_BLOCK_REVEALER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "hidden_block_revealer");

	public static void register(final RegisterCapabilitiesEvent event) {
		event.register(IHiddenBlockRevealer.class);

		CapabilityContainerListenerManager.registerListenerFactory(HiddenBlockRevealerContainerListener::new);
	}

	/**
	 * Get the {@link IHiddenBlockRevealer} for the {@link ItemStack}, if any.
	 *
	 * @param stack The ItemStack
	 * @return A lazy optional containing the IHiddenBlockRevealer for the {@link ItemStack}, if any
	 */
	public static LazyOptional<IHiddenBlockRevealer> getHiddenBlockRevealer(final ItemStack stack) {
		return stack.getCapability(HIDDEN_BLOCK_REVEALER_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Toggle the reveal state of the player's held {@link IHiddenBlockRevealer}.
	 *
	 * @param stack The ItemStack
	 * @return The new reveal state, or empty if there is no IHiddenBlockRevealer
	 */
	public static Optional<Boolean> toggleRevealHiddenBlocks(final ItemStack stack) {
		return getHiddenBlockRevealer(stack)
				.map(hiddenBlockRevealer -> {
					final boolean revealHiddenBlocks = !hiddenBlockRevealer.revealHiddenBlocks();
					hiddenBlockRevealer.setRevealHiddenBlocks(revealHiddenBlocks);

					return revealHiddenBlocks;
				});
	}

}
