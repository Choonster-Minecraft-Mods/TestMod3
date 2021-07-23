package choonster.testmod3.block;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.tileentity.HiddenTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

/**
 * A block that only renders when the player is holding an item with the {@link IHiddenBlockRevealer} capability.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/41720-1102-only-render-block-if-player-holds-a-specified-item/
 *
 * @author Choonster
 */
public class HiddenBlock extends TileEntityBlock<HiddenTileEntity> {
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
	protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HIDDEN);
	}

	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		return new HiddenTileEntity();
	}

	/*
	// TODO: Figure out how to implement this in 1.13
	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
		// We can't easily get the logical side here, so only set the HIDDEN property on the physical client (client thread and integrated server thread)
		if (FMLCommonHandler.instance().getSide().isClient()) {
			state = state.withProperty(HIDDEN, !HiddenBlockManager.shouldHeldItemRevealHiddenBlocksClient());
		}

		return state;
	}*/
}
