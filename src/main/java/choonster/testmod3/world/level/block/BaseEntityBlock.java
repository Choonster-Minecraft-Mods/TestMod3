package choonster.testmod3.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

/**
 * A block with a {@link BlockEntity}.
 *
 * @author Choonster
 */
public abstract class BaseEntityBlock<TE extends BlockEntity> extends Block implements net.minecraft.world.level.block.EntityBlock {
	public BaseEntityBlock(final Block.Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public abstract BlockEntity newBlockEntity(final BlockPos pos, final BlockState state);

	/**
	 * Get the {@link BlockEntity} at the specified position.
	 *
	 * @param level The level
	 * @param pos   The position
	 * @return The BlockEntity
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	protected TE getBlockEntity(final BlockGetter level, final BlockPos pos) {
		return (TE) level.getBlockEntity(pos);
	}

	@SuppressWarnings("unchecked")
	@Nullable
	protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
			final BlockEntityType<A> actualType, final BlockEntityType<E> requiredType,
			final BlockEntityTicker<? super E> ticker
	) {
		return requiredType == actualType ? (BlockEntityTicker<A>) ticker : null;
	}
}
