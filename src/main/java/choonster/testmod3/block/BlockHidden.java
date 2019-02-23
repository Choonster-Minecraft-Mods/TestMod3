package choonster.testmod3.block;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockManager;
import choonster.testmod3.tileentity.TileEntityHidden;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
	public static final IProperty<Boolean> HIDDEN = PropertyBool.create("hidden");

	{
		setDefaultState(getBlockState().getBaseState().withProperty(HIDDEN, true));
	}

	public BlockHidden(final Material material, final MapColor mapColor) {
		super(material, mapColor, false);
	}

	public BlockHidden(final Material materialIn) {
		super(materialIn, false);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this).add(HIDDEN).build();
	}

	@Override
	public TileEntity createTileEntity(final World world, final IBlockState state) {
		return new TileEntityHidden();
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(final IBlockState state) {
		return !state.getValue(HIDDEN);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
		// We can't easily get the logical side here, so only set the HIDDEN property on the physical client (client thread and integrated server thread)
		if (FMLCommonHandler.instance().getSide().isClient()) {
			state = state.withProperty(HIDDEN, !HiddenBlockManager.shouldHeldItemRevealHiddenBlocksClient());
		}

		return state;
	}
}
