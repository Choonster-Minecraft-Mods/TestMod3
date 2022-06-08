package choonster.testmod3.world.level.levelgen.placement;

import choonster.testmod3.init.levelgen.ModPlacementModifierTypes;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

/**
 * Only generates positions in chunks with coordinates divisible by 16.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class InChunksDivisibleBy16Filter extends PlacementFilter {
	private static final InChunksDivisibleBy16Filter INSTANCE = new InChunksDivisibleBy16Filter();
	public static final Codec<InChunksDivisibleBy16Filter> CODEC = Codec.unit(() -> INSTANCE);

	public static InChunksDivisibleBy16Filter instance() {
		return INSTANCE;
	}

	@Override
	protected boolean shouldPlace(final PlacementContext context, final RandomSource random, final BlockPos pos) {
		final ChunkPos chunkPos = new ChunkPos(pos);

		return chunkPos.x % 16 == 0 && chunkPos.z % 16 == 0;
	}

	@Override
	public PlacementModifierType<?> type() {
		return ModPlacementModifierTypes.IN_CHUNKS_DIVISIBLE_BY_16.get();
	}
}
