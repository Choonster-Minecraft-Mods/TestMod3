package choonster.testmod3.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An Item that prints the current state of a Block and its TileEntity on the client and server when right clicked.
 *
 * @author Choonster
 */
public class ItemBlockDebugger extends Item {
	private static final Logger LOGGER = LogManager.getLogger();

	public ItemBlockDebugger(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public EnumActionResult onItemUse(final ItemUseContext context) {
		final BlockPos pos = context.getPos();
		final IBlockState state = context.getWorld().getBlockState(pos);
		LOGGER.info("Block at {},{},{}: {}", pos.getX(), pos.getY(), pos.getZ(), state);

		final TileEntity tileEntity = context.getWorld().getTileEntity(pos);
		if (tileEntity != null) {
			LOGGER.info("TileEntity data: {}", tileEntity.serializeNBT());
		}

		return EnumActionResult.SUCCESS;
	}
}
