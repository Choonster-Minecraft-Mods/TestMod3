package choonster.testmod3.world.level.block;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.init.ModBlockEntities;
import choonster.testmod3.world.level.block.entity.HiddenBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

/**
 * A block that only renders when the player is holding an item with the {@link IHiddenBlockRevealer} capability.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/41720-1102-only-render-block-if-player-holds-a-specified-item/
 *
 * @author Choonster
 */
public class HiddenBlock extends BaseEntityBlock<HiddenBlockEntity> {
	public static final MapCodec<HiddenBlock> CODEC = simpleCodec(HiddenBlock::new);

	/**
	 * Is this block hidden?
	 * <p>
	 * Only set from {_@link Block#getActualState} on the client side, do not query on the server.
	 */
	public static final Property<Boolean> HIDDEN = BooleanProperty.create("hidden");

	public HiddenBlock(final Block.Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(HIDDEN, true));
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HIDDEN);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		return new HiddenBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level, final BlockState state, final BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, ModBlockEntities.HIDDEN.get(), HiddenBlockEntity::tick);
	}

	/*
	// TODO: Figure out how to implement this in 1.13
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, final IBlockAccess level, final BlockPos pos) {
		// We can't easily get the logical side here, so only set the HIDDEN property on the physical client (client thread and integrated server thread)
		if (FMLCommonHandler.instance().getSide().isClient()) {
			state = state.withProperty(HIDDEN, !HiddenBlockManager.shouldHeldItemRevealHiddenBlocksClient());
		}

		return state;
	}*/
}
