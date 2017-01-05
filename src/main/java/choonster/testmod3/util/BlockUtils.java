package choonster.testmod3.util;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;

/**
 * Utility methods for {@link Block}s.
 *
 * @author Choonster
 */
public class BlockUtils {

	/**
	 * Get a {@link TileEntity} from an {@link IBlockAccess} without creating a new instance if the {@link IBlockAccess} is a {@link ChunkCache}.
	 * <p>
	 * Safe for use in {@link Block#getActualState}, which can be called from multiple threads.
	 *
	 * @param blockAccess The world
	 * @param pos         The block position
	 * @return The TileEntity, if any
	 * @see	<a href="https://github.com/MinecraftForge/Documentation/pull/81"></a>
	 */
	@Nullable
	public static TileEntity getTileEntitySafe(IBlockAccess blockAccess, BlockPos pos) {
		return blockAccess instanceof ChunkCache ? ((ChunkCache) blockAccess).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : blockAccess.getTileEntity(pos);
	}
}
