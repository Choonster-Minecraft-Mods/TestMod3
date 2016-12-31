package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

/**
 * Default implementation of {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class ChunkEnergy extends EnergyStorage implements IChunkEnergy, INBTSerializable<NBTTagInt> {
	public ChunkEnergy(int capacity) {
		super(capacity);
	}

	public ChunkEnergy(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public ChunkEnergy(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	@Override
	public NBTTagInt serializeNBT() {
		return new NBTTagInt(getEnergyStored());
	}

	@Override
	public void deserializeNBT(NBTTagInt nbt) {
		energy = nbt.getInt();
	}
}
