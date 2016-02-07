package com.choonster.testmod3.api.pigspawner;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

/**
 * A pig spawner. Unless otherwise noted, all methods are only called on the server.
 *
 * @author Choonster
 */
public interface IPigSpawner extends INBTSerializable<NBTTagCompound> {
	/**
	 * Can a pig be spawned at the specified position?
	 *
	 * @param world The world
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Can a pig be spawned?
	 */
	boolean canSpawnPig(World world, double x, double y, double z);

	/**
	 * Can a pig be spawned at the specified position?
	 *
	 * @param world The world
	 * @param x     The x coordinate
	 * @param y     The y coordinate
	 * @param z     The z coordinate
	 * @return Can a pig be spawned?
	 */
	boolean spawnPig(World world, double x, double y, double z);

	/**
	 * Get the tooltip lines for this spawner. Can be called on the client or server.
	 *
	 * @return The tooltip lines
	 */
	List<IChatComponent> getTooltipLines();
}
