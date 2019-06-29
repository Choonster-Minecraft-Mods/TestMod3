package choonster.testmod3.block;

import choonster.testmod3.tileentity.PotionEffectTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

/**
 * A block that applies a potion effect to all entities within a certain distance of it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35818.0.html
 *
 * @author Choonster
 */
public class PotionEffectBlock extends TileEntityBlock<PotionEffectTileEntity> {
	public PotionEffectBlock(final Block.Properties properties) {
		super(false, properties);
	}

	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		return new PotionEffectTileEntity();
	}
}
