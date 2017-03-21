package choonster.testmod3.compat.waila;

import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.tileentity.TileEntityFluidTankRestricted;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Adds a line to the Waila tooltip body displaying the enabled facings of a {@link TileEntityFluidTankRestricted}.
 *
 * @author Choonster
 */
public class HUDHandlerFluidTankRestrictedEnabledFacings implements IWailaDataProvider {
	/**
	 * Callback used to override the default Waila lookup system.</br>
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerStackProvider}.</br>
	 *
	 * @param accessor Contains most of the relevant information about the current environment.
	 * @param config   Current configuration of Waila.
	 * @return null if override is not required, an ItemStack otherwise.
	 */
	@Override
	public ItemStack getWailaStack(final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return ItemStack.EMPTY;
	}

	/**
	 * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).</br>
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerHeadProvider} client side.</br>
	 * You are supposed to always return the modified input currenttip.</br>
	 *
	 * @param itemStack  Current block scanned, in ItemStack form.
	 * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be processed by other providers).
	 * @param accessor   Contains most of the relevant information about the current environment.
	 * @param config     Current configuration of Waila.
	 * @return Modified input currenttip
	 */
	@Override
	public List<String> getWailaHead(final ItemStack itemStack, final List<String> currenttip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return currenttip;
	}

	/**
	 * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).</br>
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerBodyProvider} client side.</br>
	 * You are supposed to always return the modified input currenttip.</br>
	 *
	 * @param itemStack  Current block scanned, in ItemStack form.
	 * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be processed by other providers).
	 * @param accessor   Contains most of the relevant information about the current environment.
	 * @param config     Current configuration of Waila.
	 * @return Modified input currenttip
	 */
	@Override
	public List<String> getWailaBody(final ItemStack itemStack, final List<String> currenttip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		final TileEntity tileEntity = accessor.getTileEntity();

		if (tileEntity instanceof TileEntityFluidTankRestricted) {
			final String enabledFacingsString = ModBlocks.FLUID_TANK_RESTRICTED.getEnabledFacingsString(accessor.getWorld(), accessor.getPosition());
			currenttip.add(I18n.format("tile.testmod3:fluid_tank_restricted.enabled_facings", enabledFacingsString));
		}

		return currenttip;
	}

	/**
	 * Callback used to add lines to one of the three sections of the tooltip (Head, Body, Tail).</br>
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerTailProvider} client side.</br>
	 * You are supposed to always return the modified input currenttip.</br>
	 *
	 * @param itemStack  Current block scanned, in ItemStack form.
	 * @param currenttip Current list of tooltip lines (might have been processed by other providers and might be processed by other providers).
	 * @param accessor   Contains most of the relevant information about the current environment.
	 * @param config     Current configuration of Waila.
	 * @return Modified input currenttip
	 */
	@Override
	public List<String> getWailaTail(final ItemStack itemStack, final List<String> currenttip, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return currenttip;
	}

	/**
	 * Callback used server side to return a custom synchronization NBTTagCompound.</br>
	 * Will be used if the implementing class is registered via {@link IWailaRegistrar#registerNBTProvider} server and client side.</br>
	 * You are supposed to always return the modified input NBTTagCompound tag.</br>
	 *
	 * @param player The player requesting data synchronization (The owner of the current connection).
	 * @param te     The TileEntity targeted for synchronization.
	 * @param tag    Current synchronization tag (might have been processed by other providers and might be processed by other providers).
	 * @param world  TileEntity's World.
	 * @param pos    Position of the TileEntity.
	 * @return Modified input NBTTagCompound tag.
	 */
	@Override
	public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity te, final NBTTagCompound tag, final World world, final BlockPos pos) {
		return tag;
	}
}
