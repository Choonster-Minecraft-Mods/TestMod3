package choonster.testmod3.item;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * An item that sends the player a chat message when it's used to destroy Wheat with an age &gt;= 6.
 *
 * @author Choonster
 */
public class ItemBlockDestroyer extends ItemTestMod3 {
	public ItemBlockDestroyer() {
		super("block_destroyer");
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
		if (!worldIn.isRemote) {
			final IBlockState state = worldIn.getBlockState(pos);
			if (state.getBlock() == Blocks.WHEAT && state.getValue(BlockCrops.AGE) >= 6) {
				entityLiving.sendMessage(new TextComponentTranslation("message.testmod3:block_destroyer.destroy"));
			}
		}

		return true;
	}
}
