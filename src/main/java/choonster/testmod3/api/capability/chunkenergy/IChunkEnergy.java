package choonster.testmod3.api.capability.chunkenergy;

import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Stores an energy value for a single chunk.
 *
 * @author Choonster
 */
public interface IChunkEnergy extends IEnergyStorage, INBTSerializable<NBTTagInt> {
}
