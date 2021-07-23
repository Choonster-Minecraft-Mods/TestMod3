package choonster.testmod3.world.level.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;

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

	public TestMod3SaplingBlock(final AbstractTreeGrower tree, final Block.Properties properties) {
		super(tree, properties);
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(ITEM);
	}
}
