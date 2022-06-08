package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

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
	public boolean mineBlock(final ItemStack stack, final Level level, final BlockState state, final BlockPos pos, final LivingEntity entityLiving) {
		if (!level.isClientSide) {
			if (state.getBlock() == Blocks.WHEAT && state.getValue(CropBlock.AGE) >= 6) {
				entityLiving.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_BLOCK_DESTROYER_DESTROY.getTranslationKey()));
			}
		}

		return true;
	}
}
