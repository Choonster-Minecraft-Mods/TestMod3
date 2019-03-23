package choonster.testmod3.compat.waila;

import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.tileentity.TileEntityFluidTankRestricted;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

/**
 * Adds a line to the Waila tooltip body displaying the enabled facings of a {@link TileEntityFluidTankRestricted}.
 *
 * @author Choonster
 */
public class HUDHandlerFluidTankRestrictedEnabledFacings implements IComponentProvider {
	@Override
	public void appendBody(final List<ITextComponent> tooltip, final IDataAccessor accessor, final IPluginConfig config) {
		final TileEntity tileEntity = accessor.getTileEntity();

		if (tileEntity instanceof TileEntityFluidTankRestricted) {
			final String enabledFacingsString = ModBlocks.FLUID_TANK_RESTRICTED.getEnabledFacingsString(accessor.getWorld(), accessor.getPosition());
			tooltip.add(new TextComponentTranslation("block.testmod3.fluid_tank_restricted.enabled_facings", enabledFacingsString));
		}
	}
}
