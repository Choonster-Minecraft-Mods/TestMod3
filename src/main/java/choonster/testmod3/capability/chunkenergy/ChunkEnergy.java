package choonster.testmod3.capability.chunkenergy;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.network.MessageUpdateChunkEnergyValue;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Default implementation of {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class ChunkEnergy extends EnergyStorage implements IChunkEnergy, INBTSerializable<NBTTagInt> {
	/**
	 * The {@link World} containing this instance's chunk.
	 */
	private final World world;

	/**
	 * The {@link ChunkPos} of this instance's chunk.
	 */
	private final ChunkPos chunkPos;

	public ChunkEnergy(final int capacity, final World world, final ChunkPos chunkPos) {
		super(capacity);
		this.world = world;
		this.chunkPos = chunkPos;
		energy = capacity;
	}

	@Override
	public NBTTagInt serializeNBT() {
		return new NBTTagInt(getEnergyStored());
	}

	@Override
	public void deserializeNBT(final NBTTagInt nbt) {
		energy = nbt.getInt();
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public ChunkPos getChunkPos() {
		return chunkPos;
	}

	@Override
	public int receiveEnergy(final int maxReceive, final boolean simulate) {
		final int energyReceived = super.receiveEnergy(maxReceive, simulate);

		if (!simulate && energyReceived != 0) {
			onEnergyChanged();
		}

		return energyReceived;
	}

	@Override
	public int extractEnergy(final int maxExtract, final boolean simulate) {
		final int energyExtracted = super.extractEnergy(maxExtract, simulate);

		if (!simulate && energyExtracted != 0) {
			onEnergyChanged();
		}

		return energyExtracted;
	}

	/**
	 * Set the energy value. For internal use only.
	 *
	 * @param energy The new energy value
	 */
	public void setEnergy(final int energy) {
		this.energy = energy;
		onEnergyChanged();
	}

	/**
	 * Called when the energy value changes.
	 */
	protected void onEnergyChanged() {
		final World world = getWorld();
		final ChunkPos chunkPos = getChunkPos();

		if (world.isRemote) return;

		final BlockPos chunkOrigin = chunkPos.getBlock(0, 0, 0);

		if (world.isBlockLoaded(chunkOrigin)) {
			world.getChunk(chunkPos.x, chunkPos.z).markDirty();
		}

		final PlayerChunkMapEntry playerChunkMapEntry = ((WorldServer) world).getPlayerChunkMap().getEntry(chunkPos.x, chunkPos.z);
		if (playerChunkMapEntry == null) return;

		final IMessage message = new MessageUpdateChunkEnergyValue(this);
		TestMod3.network.sendToAllTracking(message, new NetworkRegistry.TargetPoint(world.dimension.getDimension(), chunkOrigin.getX(), chunkOrigin.getY(), chunkOrigin.getZ(), 0));
	}
}
