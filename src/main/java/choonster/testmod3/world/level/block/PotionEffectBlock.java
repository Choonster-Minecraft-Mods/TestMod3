package choonster.testmod3.world.level.block;

import choonster.testmod3.world.level.block.entity.PotionEffectBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * A block that applies a potion effect to all entities within a certain distance of it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35818.0.html
 *
 * @author Choonster
 */
public class PotionEffectBlock extends BaseEntityBlock<PotionEffectBlockEntity> {
	public PotionEffectBlock(final Block.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return new PotionEffectBlockEntity(pos, state);
	}
}
