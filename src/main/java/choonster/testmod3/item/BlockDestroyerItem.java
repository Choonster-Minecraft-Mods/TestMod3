package choonster.testmod3.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * An item that sends the player a chat message when it's used to destroy Wheat with an age &gt;= 6.
 *
 * @author Choonster
 */
public class BlockDestroyerItem extends Item {
	public BlockDestroyerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean onBlockDestroyed(final ItemStack stack, final World world, final BlockState state, final BlockPos pos, final LivingEntity entityLiving) {
		if (!world.isRemote) {
			if (state.getBlock() == Blocks.WHEAT && state.get(CropsBlock.AGE) >= 6) {
				entityLiving.sendMessage(new TranslationTextComponent("message.testmod3.block_destroyer.destroy"), Util.DUMMY_UUID);
			}
		}

		return true;
	}
}
