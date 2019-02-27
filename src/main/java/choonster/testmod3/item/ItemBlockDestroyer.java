package choonster.testmod3.item;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * An item that sends the player a chat message when it's used to destroy Wheat with an age &gt;= 6.
 *
 * @author Choonster
 */
public class ItemBlockDestroyer extends Item {
	public ItemBlockDestroyer(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean onBlockDestroyed(final ItemStack stack, final World world, final IBlockState state, final BlockPos pos, final EntityLivingBase entityLiving) {
		if (!world.isRemote) {
			if (state.getBlock() == Blocks.WHEAT && state.get(BlockCrops.AGE) >= 6) {
				entityLiving.sendMessage(new TextComponentTranslation("message.testmod3:block_destroyer.destroy"));
			}
		}

		return true;
	}
}
