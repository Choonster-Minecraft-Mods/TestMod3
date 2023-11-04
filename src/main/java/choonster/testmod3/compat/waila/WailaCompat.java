package choonster.testmod3.compat.waila;

import choonster.testmod3.compat.HudProvider;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.level.block.*;
import snownee.jade.api.IBlockComponentProvider;
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
	public static final IBlockComponentProvider COLORED_ROTATABLE_BLOCK_FACING = new RotatableProvider(
			HudProvider.COLORED_ROTATABLE_BLOCK_FACING.getId(),
			ColoredRotatableBlock.FACING
	);

	public static final IBlockComponentProvider COLORED_MULTI_ROTATABLE_BLOCK_FACE_ROTATION = new MultiRotatableProvider(
			HudProvider.COLORED_MULTI_ROTATABLE_BLOCK_FACE_ROTATION.getId(),
			ColoredMultiRotatableBlock.FACE_ROTATION
	);

	public static final IBlockComponentProvider ROTATABLE_LAMP_FACING = new RotatableProvider(
			HudProvider.ROTATABLE_LAMP_FACING.getId(),
			RotatableLampBlock.FACING
	);

	public static final IBlockComponentProvider CHEST_FACING = new RotatableProvider(
			HudProvider.CHEST_FACING.getId(),
			ModChestBlock.FACING
	);

	public static final IBlockComponentProvider PLANE_HORIZONTAL_ROTATION = new RotatableProvider(
			HudProvider.PLANE_HORIZONTAL_ROTATION.getId(),
			PlaneBlock.HORIZONTAL_ROTATION,
			TestMod3Lang.BLOCK_DESC_PLANE_HORIZONTAL_ROTATION.getTranslationKey()
	);

	public static final IBlockComponentProvider PLANE_VERTICAL_ROTATION = new VerticalRotatableProvider(
			HudProvider.PLANE_VERTICAL_ROTATION.getId(),
			PlaneBlock.VERTICAL_ROTATION
	);

	public static final IBlockComponentProvider RESTRICTED_FLUID_TANK_ENABLED_FACINGS = new RestrictedFluidTankEnabledFacingsProvider(
			HudProvider.RESTRICTED_FLUID_TANK_ENABLED_FACINGS.getId()
	);

	@Override
	public void registerClient(final IWailaClientRegistration registration) {
		registration.registerBlockComponent(COLORED_ROTATABLE_BLOCK_FACING, ColoredRotatableBlock.class);
		registration.registerBlockComponent(COLORED_MULTI_ROTATABLE_BLOCK_FACE_ROTATION, ColoredMultiRotatableBlock.class);

		registration.registerBlockComponent(ROTATABLE_LAMP_FACING, RotatableLampBlock.class);
		registration.registerBlockComponent(CHEST_FACING, ModChestBlock.class);

		registration.registerBlockComponent(PLANE_HORIZONTAL_ROTATION, PlaneBlock.class);
		registration.registerBlockComponent(PLANE_VERTICAL_ROTATION, PlaneBlock.class);

		registration.registerBlockComponent(RESTRICTED_FLUID_TANK_ENABLED_FACINGS, RestrictedFluidTankBlock.class);
	}
}
