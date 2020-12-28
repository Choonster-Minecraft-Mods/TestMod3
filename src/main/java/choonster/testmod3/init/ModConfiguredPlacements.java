package choonster.testmod3.init;

import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

/**
 * Stores this mod's {@link ConfiguredPlacement}s.
 * <p>
 * There's no registry for ConfiguredPlacements, so no registration is done here.
 *
 * @author Choonster
 */
public class ModConfiguredPlacements {
	public static final Supplier<ConfiguredPlacement<NoPlacementConfig>> IN_CHUNKS_DIVISIBLE_BY_16 = Lazy.of(() ->
			ModPlacements.IN_CHUNKS_DIVISIBLE_BY_16.get()
					.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)
	);
}
