package choonster.testmod3.network.capability;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

import org.jetbrains.annotations.Nullable;

/**
 * Utility methods and common interfaces for {@link UpdateMenuCapabilityMessage} .
 *
 * @author Choonster
 */
public class CapabilityMenuUpdateMessageUtils {
	/**
	 * Applies the data instance to the capability handler instance in the specified {@link AbstractContainerMenu} slot.
	 *
	 * @param menu                  The menu
	 * @param stateID               The state ID from the menu
	 * @param slotNumber            The slot number to apply the data to
	 * @param capability            The capability to apply the data for
	 * @param facing                The facing to apply the data for
	 * @param data                  The data instance to apply
	 * @param capabilityDataApplier A function that applies the capability data from a data instance to a capability handler instance
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 */
	static <HANDLER, DATA> void applyCapabilityDataToMenuSlot(
			final AbstractContainerMenu menu,
			final int stateID,
			final int slotNumber,
			final Capability<HANDLER> capability,
			@Nullable final Direction facing,
			final DATA data,
			final CapabilityDataApplier<HANDLER, DATA> capabilityDataApplier
	) {
		final ItemStack originalStack = menu.getSlot(slotNumber).getItem();

		originalStack.getCapability(capability, facing).ifPresent(originalHandler -> {
			final ItemStack newStack = originalStack.copy();

			newStack.getCapability(capability, facing).ifPresent(newHandler -> {
				capabilityDataApplier.apply(newHandler, data);

				if (!originalHandler.equals(newHandler)) {
					menu.setItem(slotNumber, stateID, newStack);
				}
			});
		});
	}

	/**
	 * Converts a capability handler instance to a data instance.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataConverter<HANDLER, DATA> {
		@Nullable
		DATA convert(HANDLER handler);
	}

	/**
	 * A function that decodes a data instance from the buffer
	 *
	 * @param <DATA> The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataDecoder<DATA> {
		DATA decode(FriendlyByteBuf buffer);
	}

	/**
	 * A function that encodes a data instance to the buffer
	 *
	 * @param <DATA> The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataEncoder<DATA> {
		void encode(DATA data, FriendlyByteBuf buffer);
	}

	/**
	 * A function that applies the capability data from a data instance to a capability handler instance.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataApplier<HANDLER, DATA> {
		void apply(HANDLER handler, DATA data);
	}
}
