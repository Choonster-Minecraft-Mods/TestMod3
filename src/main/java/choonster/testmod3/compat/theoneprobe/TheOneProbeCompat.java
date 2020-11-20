package choonster.testmod3.compat.theoneprobe;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import mcjty.theoneprobe.api.ITheOneProbe;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

/**
 * The One Probe compatibility.
 *
 * @author Choonster
 */
public class TheOneProbeCompat implements Function<ITheOneProbe, Void> {
	@Override
	public Void apply(final ITheOneProbe theOneProbe) {
		theOneProbe.registerProvider(new RotatableProbeInfoProvider<>(
				new ResourceLocation(TestMod3.MODID, "rotatable_block"),
				ColoredRotatableBlock.class,
				ColoredRotatableBlock.FACING
		));

		theOneProbe.registerProvider(new MultiRotatableProbeInfoProvider<>(
				new ResourceLocation(TestMod3.MODID, "multi_rotatable_block"),
				ColoredMultiRotatableBlock.class,
				ColoredMultiRotatableBlock.FACE_ROTATION
		));

		theOneProbe.registerProvider(new RotatableProbeInfoProvider<>(
				new ResourceLocation(TestMod3.MODID, "rotatable_lamp"),
				RotatableLampBlock.class,
				RotatableLampBlock.FACING
		));

		theOneProbe.registerProvider(new RotatableProbeInfoProvider<>(
				new ResourceLocation(TestMod3.MODID, "chest"),
				ModChestBlock.class,
				ModChestBlock.FACING
		));

		theOneProbe.registerProvider(new RotatableProbeInfoProvider<>(
				new ResourceLocation(TestMod3.MODID, "plane_horizontal"),
				PlaneBlock.class,
				PlaneBlock.HORIZONTAL_ROTATION,
				"block.testmod3.plane.horizontal_rotation.desc"
		));

		theOneProbe.registerProvider(new VerticalRotatableProbeInfoProvider<>(
				new ResourceLocation(TestMod3.MODID, "plane_vertical"),
				PlaneBlock.class,
				PlaneBlock.VERTICAL_ROTATION
		));

		theOneProbe.registerProvider(new RestrictedFluidTankEnabledFacingsProbeInfoProvider<>(
				new ResourceLocation(TestMod3.MODID, "restricted_fluid_tank"),
				RestrictedFluidTankBlock.class
		));

		return null;
	}
}
