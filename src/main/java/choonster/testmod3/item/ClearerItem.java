package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/**
 * An item that clears all whitelisted blocks from the player's current chunk when used.
 *
 * @author Choonster
 */
public class ClearerItem extends Item {
	private static final ImmutableList<Block> whitelist = ImmutableList.of(Blocks.STONE, Blocks.DIRT, Blocks.GRASS, Blocks.GRAVEL, Blocks.SAND, Blocks.WATER, Blocks.LAVA, Blocks.ICE);

	private static final int MODE_WHITELIST = 0;
	private static final int MODE_ALL = 1;

	public ClearerItem(final Item.Properties properties) {
		super(properties);
	}

	private int getMode(final ItemStack stack) {
		return stack.getOrCreateTag().getInt("Mode");
	}

	private void setMode(final ItemStack stack, final int mode) {
		stack.getOrCreateTag().putInt("Mode", mode);
	}

	@Override
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!world.isClientSide) {
			final int currentMode = getMode(heldItem);

			if (player.isShiftKeyDown()) {
				final int newMode = currentMode == MODE_ALL ? MODE_WHITELIST : MODE_ALL;
				setMode(heldItem, newMode);
				player.sendMessage(new TranslationTextComponent(String.format(TestMod3Lang.MESSAGE_CLEARER_MODE_S.getTranslationKey(), newMode)), Util.NIL_UUID);
			} else {
				final int minX = MathHelper.floor(player.getX() / 16) * 16;
				final int minZ = MathHelper.floor(player.getZ() / 16) * 16;

				player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_CLEARER_CLEARING.getTranslationKey(), minX, minZ), Util.NIL_UUID);

				for (int x = minX; x < minX + 16; x++) {
					for (int z = minZ; z < minZ + 16; z++) {
						for (int y = 0; y < 256; y++) {
							final BlockPos pos = new BlockPos(x, y, z);
							final Block block = world.getBlockState(pos).getBlock();
							if ((currentMode == MODE_ALL && block != Blocks.BEDROCK) || whitelist.contains(block)) {
								world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
							}
						}
					}
				}

				final BlockPos pos = player.blockPosition();
				final BlockState state = world.getBlockState(pos);
				world.sendBlockUpdated(pos, state, state, 3);

				player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_CLEARER_CLEARED.getTranslationKey()), Util.NIL_UUID);
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}

	@Override
	public boolean isFoil(final ItemStack stack) {
		return getMode(stack) == MODE_ALL || super.isFoil(stack);
	}
}
