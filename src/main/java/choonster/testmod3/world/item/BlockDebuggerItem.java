package choonster.testmod3.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An Item that prints the current state of a Block and its BlockEntity on the client and server when right-clicked.
 *
 * @author Choonster
 */
public class BlockDebuggerItem extends Item {
	private static final Logger LOGGER = LogManager.getLogger();

	public BlockDebuggerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(final UseOnContext context) {
		final BlockPos pos = context.getClickedPos();
		final BlockState state = context.getLevel().getBlockState(pos);
		LOGGER.info("Block at {},{},{}: {}", pos.getX(), pos.getY(), pos.getZ(), state);

		final BlockEntity blockEntity = context.getLevel().getBlockEntity(pos);
		if (blockEntity != null) {
			LOGGER.info("BlockEntity data: {}", blockEntity.serializeNBT());
		}

		return InteractionResult.SUCCESS;
	}
}
