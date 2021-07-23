package choonster.testmod3.compat.waila;

/**
 * Adds a line to the Waila tooltip body displaying the enabled facings of a {@link RestrictedFluidTankBlockEntity}.
 *
 * @author Choonster
 */
/*
public class RestrictedFluidTankEnabledFacingsHUDHandler implements IComponentProvider {
	@Override
	public void appendBody(final List<Component> tooltip, final IDataAccessor accessor, final IPluginConfig config) {
		final BlockEntity tileEntity = accessor.getTileEntity();

		if (tileEntity instanceof RestrictedFluidTankBlockEntity) {
			final String enabledFacingsString = ((RestrictedFluidTankBlock) accessor.getBlock())
					.getEnabledFacingsString(accessor.getWorld(), accessor.getPosition());

			tooltip.add(new TranslatableComponent(TestMod3Lang.BLOCK_DESC_FLUID_TANK_RESTRICTED_ENABLED_FACINGS.getTranslationKey(), enabledFacingsString));
		}
	}
}
*/
