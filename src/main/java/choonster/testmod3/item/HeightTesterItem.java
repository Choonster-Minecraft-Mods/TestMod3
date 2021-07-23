package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

/**
 * When right clicked, prints the value of {@link World#getHeight(Heightmap.Type, BlockPos)} at the player's current position.
 *
 * @author Choonster
 */
public class HeightTesterItem extends Item {
	public HeightTesterItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		if (!world.isClientSide) {
			final BlockPos pos = player.blockPosition();

			player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_HEIGHT_TESTER_HEIGHT.getTranslationKey(), pos.getX(), pos.getZ(), world.getHeightmapPos(Heightmap.Type.WORLD_SURFACE, pos).getY()), Util.NIL_UUID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
	}
}
