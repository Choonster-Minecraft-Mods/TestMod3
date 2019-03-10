package choonster.testmod3.block;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.tileentity.TileEntityHidden;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
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
public class BlockHidden extends BlockTileEntity<TileEntityHidden> {
	/**
	 * Is this block hidden?
	 * <p>
	 * Only set from {@link Block#getActualState} on the client side, do not query on the server.
	 */
	public static final IProperty<Boolean> HIDDEN = BooleanProperty.create("hidden");

	public BlockHidden(final Block.Properties properties) {
		super(false, properties);
		setDefaultState(getStateContainer().getBaseState().with(HIDDEN, true));
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(HIDDEN);
	}

	@Override
	public TileEntity createTileEntity(final IBlockState state, final IBlockReader world) {
		return new TileEntityHidden();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(final IBlockState state) {
		return !state.get(HIDDEN);
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
