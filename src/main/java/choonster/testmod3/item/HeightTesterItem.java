package choonster.testmod3.item;

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
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		if (!world.isRemote) {
			final BlockPos pos = player.getPosition();

			player.sendMessage(new TranslationTextComponent("message.testmod3.height_tester.height", pos.getX(), pos.getZ(), world.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY()), Util.DUMMY_UUID);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}
}
