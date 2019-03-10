package choonster.testmod3.network.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nullable;

/**
 * Functions used by the {@link IPigSpawner} capability update messages.
 *
 * @author Choonster
 */
class PigSpawnerFunctions {
	@Nullable
	static Integer convertFinitePigSpawnerToNumPigs(final IPigSpawner pigSpawner) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			return ((IPigSpawnerFinite) pigSpawner).getNumPigs();
		} else {
			return null;
		}
	}

	static int decodeNumPigs(final PacketBuffer buf) {
		return buf.readInt();
	}

	static void encodeNumPigs(final int numPigs, final PacketBuffer buffer) {
		buffer.writeInt(numPigs);
	}

	static void applyNumPigsToFinitePigSpawner(final IPigSpawner pigSpawner, final int numPigs) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			((IPigSpawnerFinite) pigSpawner).setNumPigs(numPigs);
		}
	}
}
