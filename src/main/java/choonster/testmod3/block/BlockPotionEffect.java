package choonster.testmod3.block;

import choonster.testmod3.tileentity.TileEntityPotionEffect;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * A block that applies a potion effect to all entities within a certain distance of it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35818.0.html
 *
 * @author Choonster
 */
public class BlockPotionEffect extends BlockTileEntity<TileEntityPotionEffect> {
	public BlockPotionEffect(final Block.Properties properties) {
		super(properties, false);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(final IBlockState state, final IBlockReader world) {
		return new TileEntityPotionEffect();
	}
}
