package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;

/**
 * A sapling that uses Forge's blockstates format to specify its item models.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/43377-110-blockstates-and-item-models/
 *
 * @author Choonster
 */
public class TestMod3SaplingBlock extends SaplingBlock {
	/**
	 * A dummy property to allow for separate item models.
	 */
	public static final Property<Boolean> ITEM = BooleanProperty.create("item");

	public TestMod3SaplingBlock(final Tree tree, final Block.Properties properties) {
		super(tree, properties);
	}

	@Override
	protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ITEM);
	}
}
