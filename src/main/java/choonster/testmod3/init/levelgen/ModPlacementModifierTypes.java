package choonster.testmod3.init.levelgen;

import choonster.testmod3.world.level.levelgen.placement.InChunksDivisibleBy16Filter;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

/**
 * Registers this mod's {@link PlacementModifierType}s.
 *
 * @author Choonster
 */
public class ModPlacementModifierTypes {
	public static final PlacementModifierType<InChunksDivisibleBy16Filter> IN_CHUNKS_DIVISIBLE_BY_16 = register(
			"in_chunks_divisible_by_16",
			InChunksDivisibleBy16Filter.CODEC
	);

	private static <P extends PlacementModifier> PlacementModifierType<P> register(final String name, final Codec<P> codec) {
		return Registry.register(Registry.PLACEMENT_MODIFIERS, name, () -> codec);
	}
}
