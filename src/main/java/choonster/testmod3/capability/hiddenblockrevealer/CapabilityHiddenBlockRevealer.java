package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.CapabilityContainerListenerManager;
import choonster.testmod3.item.IItemPropertyGetterFix;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Items with this capability reveal hidden blocks.
 *
 * @author Choonster
 */
public final class CapabilityHiddenBlockRevealer {
	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(IHiddenBlockRevealer.class)
	public static final Capability<IHiddenBlockRevealer> HIDDEN_BLOCK_REVEALER_CAPABILITY = null;

	/**
	 * The default {@link EnumFacing} to use for this capability.
	 */
	public static final EnumFacing DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "hidden_block_revealer");

	public static void register() {
		CapabilityManager.INSTANCE.register(IHiddenBlockRevealer.class, new Capability.IStorage<IHiddenBlockRevealer>() {
					@Override
					public NBTBase writeNBT(final Capability<IHiddenBlockRevealer> capability, final IHiddenBlockRevealer instance, final EnumFacing side) {
						final NBTTagCompound tagCompound = new NBTTagCompound();
						tagCompound.setBoolean("RevealHiddenBlocks", instance.revealHiddenBlocks());
						return tagCompound;
					}

					@Override
					public void readNBT(final Capability<IHiddenBlockRevealer> capability, final IHiddenBlockRevealer instance, final EnumFacing side, final NBTBase nbt) {
						final NBTTagCompound tagCompound = (NBTTagCompound) nbt;
						instance.setRevealHiddenBlocks(tagCompound.getBoolean("RevealHiddenBlocks"));
					}
				}, HiddenBlockRevealer::new
		);

		CapabilityContainerListenerManager.registerListenerFactory(ContainerListenerHiddenBlockRevealer::new);
	}

	/**
	 * Get the {@link IHiddenBlockRevealer} for the {@link ItemStack}, if any.
	 *
	 * @param stack The ItemStack
	 * @return The IHiddenBlockRevealer for the {@link ItemStack}, if any
	 */
	@Nullable
	public static IHiddenBlockRevealer getHiddenBlockRevealer(final ItemStack stack) {
		return CapabilityUtils.getCapability(stack, HIDDEN_BLOCK_REVEALER_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Toggle the reveal state of the player's held {@link IHiddenBlockRevealer}.
	 *
	 * @param stack The ItemStack
	 * @return The new reveal state, or empty if there is no IHiddenBlockRevealer
	 */
	public static Optional<Boolean> toggleRevealHiddenBlocks(final ItemStack stack) {
		final IHiddenBlockRevealer hiddenBlockRevealer = getHiddenBlockRevealer(stack);
		if (hiddenBlockRevealer != null) {
			final boolean revealHiddenBlocks = !hiddenBlockRevealer.revealHiddenBlocks();
			hiddenBlockRevealer.setRevealHiddenBlocks(revealHiddenBlocks);

			return Optional.of(revealHiddenBlocks);
		}

		return Optional.empty();
	}

	/**
	 * {@link IItemPropertyGetter} to get whether hidden blocks are being revealed.
	 */
	public static class RevealHiddenBlocksGetter {
		/**
		 * The ID of this getter.
		 */
		private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "reveal_hidden_blocks");

		/**
		 * The getter.
		 */
		private static final IItemPropertyGetter GETTER = IItemPropertyGetterFix.create((stack, worldIn, entityIn) -> {
			final IHiddenBlockRevealer hiddenBlockRevealer = CapabilityHiddenBlockRevealer.getHiddenBlockRevealer(stack);
			return hiddenBlockRevealer != null && hiddenBlockRevealer.revealHiddenBlocks() ? 1 : 0;
		});

		/**
		 * Add this getter to an {@link Item}.
		 *
		 * @param item The item
		 */
		public static void addToItem(final Item item) {
			item.addPropertyOverride(ID, GETTER);
		}
	}
}
