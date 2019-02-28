package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.trees.AbstractTree;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;

/**
 * A sapling that uses Forge's blockstates format to specify its item models.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/43377-110-blockstates-and-item-models/
 *
 * @author Choonster
 */
public class BlockSaplingTestMod3 extends BlockSapling {
	/**
	 * A dummy property to allow for separate item models.
	 */
	public static final IProperty<Boolean> ITEM = BooleanProperty.create("item");

	public BlockSaplingTestMod3(final AbstractTree tree, final Block.Properties properties) {
		super(tree, properties);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(ITEM);
	}
}
