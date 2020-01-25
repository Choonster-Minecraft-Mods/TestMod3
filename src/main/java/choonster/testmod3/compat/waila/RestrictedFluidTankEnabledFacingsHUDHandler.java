package choonster.testmod3.compat.waila;

import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.tileentity.RestrictedFluidTankTileEntity;
import choonster.testmod3.util.RegistryUtil;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

/**
 * Adds a line to the Waila tooltip body displaying the enabled facings of a {@link RestrictedFluidTankTileEntity}.
 *
 * @author Choonster
 */
public class RestrictedFluidTankEnabledFacingsHUDHandler implements IComponentProvider {
	@Override
	public void appendBody(final List<ITextComponent> tooltip, final IDataAccessor accessor, final IPluginConfig config) {
		final TileEntity tileEntity = accessor.getTileEntity();

		if (tileEntity instanceof RestrictedFluidTankTileEntity) {
			final String enabledFacingsString = RegistryUtil.getRequiredRegistryEntry(ModBlocks.FLUID_TANK_RESTRICTED)
					.getEnabledFacingsString(accessor.getWorld(), accessor.getPosition());

			tooltip.add(new TranslationTextComponent("block.testmod3.fluid_tank_restricted.enabled_facings", enabledFacingsString));
		}
	}
}
