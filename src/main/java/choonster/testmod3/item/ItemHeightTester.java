package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * When right clicked, prints the value of {@link World#getHeight(BlockPos)} at the player's current position.
 * When sneak-right clicked, regenerates the skylight map for the player's current chunk (updating the height map used by {@link World#getHeight(BlockPos)}.
 *
 * @author Choonster
 */
public class ItemHeightTester extends ItemTestMod3 {
	public ItemHeightTester() {
		super("heightTester");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			final BlockPos pos = playerIn.getPosition();

			if (playerIn.isSneaking()) {
				final Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
				chunk.generateSkylightMap();
				playerIn.addChatComponentMessage(new TextComponentTranslation("message.testmod3:heightTester.generate", chunk.xPosition, chunk.zPosition, pos.getX(), pos.getY(), pos.getZ()));
			} else {
				playerIn.addChatComponentMessage(new TextComponentTranslation("message.testmod3:heightTester.height", pos.getX(), pos.getZ(), worldIn.getHeight(pos).getY()));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
