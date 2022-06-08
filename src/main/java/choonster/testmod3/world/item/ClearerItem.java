package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!world.isClientSide) {
			final int currentMode = getMode(heldItem);

			if (player.isShiftKeyDown()) {
				final int newMode = currentMode == MODE_ALL ? MODE_WHITELIST : MODE_ALL;
				setMode(heldItem, newMode);
				player.sendSystemMessage(Component.translatable(String.format(TestMod3Lang.MESSAGE_CLEARER_MODE_S.getTranslationKey(), newMode)));
			} else {
				final int minX = Mth.floor(player.getX() / 16) * 16;
				final int minZ = Mth.floor(player.getZ() / 16) * 16;

				player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CLEARER_CLEARING.getTranslationKey(), minX, minZ));

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

				player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CLEARER_CLEARED.getTranslationKey()));
			}
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}

	@Override
	public boolean isFoil(final ItemStack stack) {
		return getMode(stack) == MODE_ALL || super.isFoil(stack);
	}
}
