package choonster.testmod3.world.level.block;

import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.world.level.block.entity.ModChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * A chest block.
 *
 * @author Choonster
 */
public class ModChestBlock extends BaseEntityBlock<ModChestBlockEntity> {
	/**
	 * The chest's shape.
	 */
	private static final VoxelShape SHAPE = box(1, 0, 1, 15, 14, 15);

	public static final Property<Direction> FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);

	public ModChestBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter world, final BlockPos pos, final CollisionContext context) {
		return SHAPE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return new ModChestBlockEntity(pos, state);
	}

	@Override
	public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
		if (stack.hasCustomHoverName()) {
			final ModChestBlockEntity blockEntity = getBlockEntity(level, pos);
			if (blockEntity != null) {
				blockEntity.setDisplayName(stack.getHoverName());
			}
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		if (!world.isClientSide && !isBlocked(world, pos)) {
			final ModChestBlockEntity blockEntity = getBlockEntity(world, pos);
			if (blockEntity != null) {
				blockEntity.openGUI((ServerPlayer) player);
			}
		}

		return InteractionResult.SUCCESS;
	}

	/**
	 * Is the chest at the specified position blocked?
	 *
	 * @param world The level
	 * @param pos   The position
	 * @return Is the chest blocked?
	 */
	private boolean isBlocked(final Level world, final BlockPos pos) {
		return isBelowSolidBlock(world, pos) || isCatSittingOnChest(world, pos);
	}

	/**
	 * Is the chest at the specified position below a solid block?
	 *
	 * @param world The level
	 * @param pos   The position
	 * @return Is the chest below a solid block?
	 */
	private boolean isBelowSolidBlock(final Level world, final BlockPos pos) {
		return world.getBlockState(pos.above()).isRedstoneConductor(world, pos.above());
	}

	/**
	 * Is a Cat sitting on the chest at the specified position?
	 *
	 * @param world The level
	 * @param pos   The position
	 * @return Is a Cat sitting on the chest?
	 */
	private boolean isCatSittingOnChest(final Level world, final BlockPos pos) {
		for (final Cat cat : world.getEntitiesOfClass(Cat.class, new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1))) {
			if (cat.isInSittingPose()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public BlockState rotate(final BlockState state, final LevelAccessor world, final BlockPos pos, final Rotation direction) {
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(final BlockState state, final Level world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			final ModChestBlockEntity blockEntity = getBlockEntity(world, pos);
			if (blockEntity != null) {
				blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(itemHandler -> {
					InventoryUtils.dropItemHandlerContents(world, pos, itemHandler);
					world.updateNeighbourForOutputSignal(pos, this);
				});
			}

			super.onRemove(state, world, pos, newState, isMoving);
		}
	}
}
