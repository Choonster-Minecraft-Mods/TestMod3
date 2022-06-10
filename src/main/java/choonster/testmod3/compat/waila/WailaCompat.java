package choonster.testmod3.compat.waila;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.*;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

/**
 * Waila compatibility.
 *
 * @author Choonster
 */
@WailaPlugin
public class WailaCompat implements IWailaPlugin {
	@Override
	public void registerClient(final IWailaClientRegistration registration) {
		registration.registerBlockComponent(new RotatableHUDHandler(ColoredRotatableBlock.FACING), ColoredRotatableBlock.class);
		registration.registerBlockComponent(new MultiRotatableHUDHandler(ColoredMultiRotatableBlock.FACE_ROTATION), ColoredMultiRotatableBlock.class);

		registration.registerBlockComponent(new RotatableHUDHandler(RotatableLampBlock.FACING), RotatableLampBlock.class);
		registration.registerBlockComponent(new RotatableHUDHandler(ModChestBlock.FACING), ModChestBlock.class);

		registration.registerBlockComponent(new RotatableHUDHandler(PlaneBlock.HORIZONTAL_ROTATION, TestMod3Lang.BLOCK_DESC_PLANE_HORIZONTAL_ROTATION.getTranslationKey()), PlaneBlock.class);
		registration.registerBlockComponent(new VerticalRotatableHUDHandler(PlaneBlock.VERTICAL_ROTATION), PlaneBlock.class);

		registration.registerBlockComponent(new RestrictedFluidTankEnabledFacingsHUDHandler(), RestrictedFluidTankBlock.class);
	}
}
