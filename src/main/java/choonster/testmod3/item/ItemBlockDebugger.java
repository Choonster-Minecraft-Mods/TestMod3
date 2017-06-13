package choonster.testmod3.item;

import choonster.testmod3.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An Item that prints the current state of a Block and its TileEntity on the client and server when right clicked.
 *
 * @author Choonster
 */
public class ItemBlockDebugger extends ItemTestMod3 {
	public ItemBlockDebugger() {
		super("block_debugger");
	}

	@Override
	public EnumActionResult onItemUse(final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
		final IBlockState state = worldIn.getBlockState(pos).getActualState(worldIn, pos);
		Logger.info("Block at %d,%d,%d: %s", pos.getX(), pos.getY(), pos.getZ(), state);

		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null) {
			Logger.info("TileEntity data: %s", tileEntity.serializeNBT());
		}

		return EnumActionResult.SUCCESS;
	}
}
