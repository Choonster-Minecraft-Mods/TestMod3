package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.item.IItemPropertyGetterFix;
import choonster.testmod3.network.MessageUpdateHeldHiddenBlockRevealer;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * Items with this capability reveal hidden blocks.
 *
 * @author Choonster
 */
public class CapabilityHiddenBlockRevealer {
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
					public NBTBase writeNBT(Capability<IHiddenBlockRevealer> capability, IHiddenBlockRevealer instance, EnumFacing side) {
						final NBTTagCompound tagCompound = new NBTTagCompound();
						tagCompound.setBoolean("RevealHiddenBlocks", instance.revealHiddenBlocks());
						return tagCompound;
					}

					@Override
					public void readNBT(Capability<IHiddenBlockRevealer> capability, IHiddenBlockRevealer instance, EnumFacing side, NBTBase nbt) {
						final NBTTagCompound tagCompound = (NBTTagCompound) nbt;
						instance.setRevealHiddenBlocks(tagCompound.getBoolean("RevealHiddenBlocks"));
					}
				}, HiddenBlockRevealer::new
		);
	}

	/**
	 * Get the {@link IHiddenBlockRevealer} for the {@link ItemStack}, if any.
	 *
	 * @param stack The ItemStack
	 * @return The {@link IHiddenBlockRevealer} for the {@link ItemStack}, if any
	 */
	@Nullable
	public static IHiddenBlockRevealer getHiddenBlockRevealer(ItemStack stack) {
		return CapabilityUtils.getCapability(stack, HIDDEN_BLOCK_REVEALER_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Toggle the reveal state of the player's held {@link IHiddenBlockRevealer}.
	 *
	 * @param playerIn The player
	 * @param hand     The hand holding the hidden block revealer
	 * @return The new reveal state, or null if there is no IHiddenBlockRevealer
	 */
	@Nullable
	public static Boolean toggleRevealHiddenBlocks(EntityPlayerMP playerIn, EnumHand hand) {
		final IHiddenBlockRevealer hiddenBlockRevealer = getHiddenBlockRevealer(playerIn.getHeldItem(hand));
		if (hiddenBlockRevealer != null) {
			final boolean revealHiddenBlocks = !hiddenBlockRevealer.revealHiddenBlocks();
			hiddenBlockRevealer.setRevealHiddenBlocks(revealHiddenBlocks);

			sendToPlayer(hiddenBlockRevealer, playerIn, hand);

			return revealHiddenBlocks;
		}

		return null;
	}

	/**
	 * Sync the {@link IHiddenBlockRevealer} for the player's held item.
	 *
	 * @param hiddenBlockRevealer The hidden block revealer
	 * @param player              The player
	 * @param hand                The hand holding the hidden block revealer
	 */
	private static void sendToPlayer(IHiddenBlockRevealer hiddenBlockRevealer, EntityPlayerMP player, EnumHand hand) {
		TestMod3.network.sendTo(new MessageUpdateHeldHiddenBlockRevealer(hiddenBlockRevealer, hand), player);
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
		public static void addToItem(Item item) {
			item.addPropertyOverride(ID, GETTER);
		}
	}
}
