package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;

/**
 * When right clicked, prints the value of {@link World#getHeight(Heightmap.Type, BlockPos)} at the player's current position.
 * When sneak-right clicked, regenerates the skylight map for the player's current chunk (updating the height map used by {@link World#getHeight(Heightmap.Type, BlockPos)}.
 *
 * @author Choonster
 */
public class ItemHeightTester extends Item {
	public ItemHeightTester(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		if (!world.isRemote) {
			final BlockPos pos = player.getPosition();

			if (player.isSneaking()) {
				final Chunk chunk = world.getChunk(pos);
				chunk.generateSkylightMap();
				player.sendMessage(new TextComponentTranslation("message.testmod3.height_tester.generate", chunk.x, chunk.z, pos.getX(), pos.getY(), pos.getZ()));
			} else {
				player.sendMessage(new TextComponentTranslation("message.testmod3.height_tester.height", pos.getX(), pos.getZ(), world.getHeight(Heightmap.Type.WORLD_SURFACE, pos).getY()));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}
}
