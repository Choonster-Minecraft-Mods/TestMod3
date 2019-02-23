package choonster.testmod3.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An Item that prints the current state of a Block and its TileEntity on the client and server when right clicked.
 *
 * @author Choonster
 */
public class ItemBlockDebugger extends Item {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public EnumActionResult onItemUse(final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		final IBlockState state = worldIn.getBlockState(pos).getActualState(worldIn, pos);
		LOGGER.info("Block at {},{},{}: {}", pos.getX(), pos.getY(), pos.getZ(), state);

		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null) {
			LOGGER.info("TileEntity data: {}", tileEntity.serializeNBT());
		}

		return EnumActionResult.SUCCESS;
	}
}
