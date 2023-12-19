package choonster.testmod3.world.level.block;

import choonster.testmod3.init.ModBlockEntities;
import choonster.testmod3.world.level.block.entity.PotionEffectBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * A block that applies a potion effect to all entities within a certain distance of it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35818.0.html
 *
 * @author Choonster
 */
public class PotionEffectBlock extends BaseEntityBlock<PotionEffectBlockEntity> {
	public static final MapCodec<PotionEffectBlock> CODEC = simpleCodec(PotionEffectBlock::new);

	public PotionEffectBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return new PotionEffectBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level, final BlockState state, final BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.POTION_EFFECT.get(), PotionEffectBlockEntity::tick);
	}
}
