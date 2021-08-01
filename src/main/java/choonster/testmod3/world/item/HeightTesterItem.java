package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * When right-clicked, prints the value of {@link Level#getHeightmapPos(Heightmap.Types, BlockPos)} at the player's current position.
 *
 * @author Choonster
 */
public class HeightTesterItem extends Item {
	public HeightTesterItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		if (!world.isClientSide) {
			final BlockPos pos = player.blockPosition();

			player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_HEIGHT_TESTER_HEIGHT.getTranslationKey(), pos.getX(), pos.getZ(), world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos).getY()), Util.NIL_UUID);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}
}
