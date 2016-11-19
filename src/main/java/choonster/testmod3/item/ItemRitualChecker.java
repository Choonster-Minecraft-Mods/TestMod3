package choonster.testmod3.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * An item that checks if the player is standing on obsidian and surrounded by the following pattern of blocks:
 * <p>
 * A A A A A
 * A R R R A
 * A R P R A
 * A R R R A
 * A A A A A
 * <p>
 * Where A is air, R is redstone and P is the player.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,41308.0.html
 *
 * @author Choonster
 */
public class ItemRitualChecker extends ItemTestMod3 {
	public ItemRitualChecker() {
		super("ritual_checker");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			final ITextComponent textComponent;

			final Optional<BlockPos> invalidPosition = checkRitual(playerIn);
			if (invalidPosition.isPresent()) {
				final BlockPos pos = invalidPosition.get();
				textComponent = new TextComponentTranslation("message.testmod3:ritual_checker.failure", pos.getX(), pos.getY(), pos.getZ());
				textComponent.getStyle().setColor(TextFormatting.RED);
			} else {
				textComponent = new TextComponentTranslation("message.testmod3:ritual_checker.success");
				textComponent.getStyle().setColor(TextFormatting.GREEN);
			}

			playerIn.sendMessage(textComponent);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}

	/**
	 * Is the player surrounded by the correct pattern of blocks?
	 *
	 * @param player The command player
	 * @return The first invalid position, if any.
	 */
	private Optional<BlockPos> checkRitual(EntityPlayer player) {
		final World world = player.getEntityWorld();
		final BlockPos playerPos = new BlockPos(player);

		// The block under the player must be obsidian
		if (!(world.getBlockState(playerPos.down()).getBlock() == Blocks.OBSIDIAN))
			return Optional.of(playerPos.down());

		// Iterate from -2,0,-2 to +2,0,+2
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				// If this is the player's position, skip it
				if (x == 0 && z == 0) {
					continue;
				}

				final BlockPos pos = playerPos.add(x, 0, z);
				final Block block = world.getBlockState(pos).getBlock();

				if (Math.abs(x) == 2 || Math.abs(z) == 2) { // If this is the outer layer, the block must be air
					if (block != Blocks.AIR) return Optional.of(pos);

				} else { // If this is the inner layer, the block must be redstone
					if (block != Blocks.REDSTONE_WIRE) return Optional.of(pos);

				}
			}
		}

		// All blocks are correct, the ritual is valid
		return Optional.empty();
	}
}
