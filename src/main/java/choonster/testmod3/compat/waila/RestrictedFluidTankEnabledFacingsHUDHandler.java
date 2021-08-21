package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.RestrictedFluidTankBlock;
import choonster.testmod3.world.level.block.entity.RestrictedFluidTankBlockEntity;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Adds a line to the Waila tooltip body displaying the enabled facings of a {@link RestrictedFluidTankBlockEntity}.
 *
 * @author Choonster
 */
public class RestrictedFluidTankEnabledFacingsHUDHandler implements IComponentProvider {
	@Override
	public void appendTooltip(final ITooltip tooltip, final BlockAccessor accessor, final IPluginConfig config) {
		final BlockEntity blockEntity = accessor.getBlockEntity();

		if (blockEntity instanceof RestrictedFluidTankBlockEntity) {
			final String enabledFacingsString = ((RestrictedFluidTankBlock) accessor.getBlock())
					.getEnabledFacingsString(accessor.getLevel(), accessor.getPosition());

			tooltip.add(new TranslatableComponent(TestMod3Lang.BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS.getTranslationKey(), enabledFacingsString));
		}
	}
}
