package choonster.testmod3.network.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

/**
 * Updates the {@link IPigSpawnerFinite} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageBulkUpdateContainerPigSpawnerFinites extends MessageBulkUpdateContainerCapability<IPigSpawner, Integer> {

	@SuppressWarnings("unused")
	protected MessageBulkUpdateContainerPigSpawnerFinites() {
		super(CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY);
	}

	public MessageBulkUpdateContainerPigSpawnerFinites(final int windowID, final NonNullList<ItemStack> items) {
		super(CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY, CapabilityPigSpawner.DEFAULT_FACING, windowID, items);
	}

	/**
	 * Convert a capability handler instance to a data instance.
	 *
	 * @param pigSpawner The handler
	 * @return The data instance
	 */
	@Nullable
	@Override
	protected Integer convertCapabilityToData(final IPigSpawner pigSpawner) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			return ((IPigSpawnerFinite) pigSpawner).getNumPigs();
		} else {
			return null;
		}
	}

	/**
	 * Read a data instance from the buffer.
	 *
	 * @param buf The buffer
	 */
	@Override
	protected Integer readCapabilityData(final ByteBuf buf) {
		return buf.readInt();
	}

	/**
	 * Write a data instance to the buffer.
	 *
	 * @param buf  The buffer
	 * @param data The data instance
	 */
	@Override
	protected void writeCapabilityData(final ByteBuf buf, final Integer data) {
		buf.writeInt(data);
	}

	public static class Handler extends MessageBulkUpdateContainerCapability.Handler<IPigSpawner, Integer, MessageBulkUpdateContainerPigSpawnerFinites> {

		/**
		 * Apply the capability data from the data instance to the capability handler instance.
		 *
		 * @param pigSpawner The capability handler instance
		 * @param data       The data instance
		 */
		@Override
		protected void applyCapabilityData(final IPigSpawner pigSpawner, final Integer data) {
			if (pigSpawner instanceof IPigSpawnerFinite) {
				((IPigSpawnerFinite) pigSpawner).setNumPigs(data);
			}
		}
	}
}
