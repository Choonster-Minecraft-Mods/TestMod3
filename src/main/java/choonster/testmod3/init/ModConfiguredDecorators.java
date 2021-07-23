package choonster.testmod3.init;

import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

/**
 * Stores this mod's {@link ConfiguredDecorator}s.
 * <p>
 * There's no registry for ConfiguredPlacements, so no registration is done here.
 *
 * @author Choonster
 */
public class ModConfiguredDecorators {
	public static final Supplier<ConfiguredDecorator<NoneDecoratorConfiguration>> IN_CHUNKS_DIVISIBLE_BY_16 = Lazy.of(() ->
			ModFeatureDecorators.IN_CHUNKS_DIVISIBLE_BY_16.get()
					.configured(DecoratorConfiguration.NONE)
	);
}
