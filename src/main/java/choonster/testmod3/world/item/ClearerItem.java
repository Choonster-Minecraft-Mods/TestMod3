package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.IntFunction;

/**
 * An item that clears all whitelisted blocks from the player's current chunk when used.
 *
 * @author Choonster
 */
public class ClearerItem extends Item {
	private static final ImmutableList<Block> whitelist = ImmutableList.of(Blocks.STONE, Blocks.DIRT, Blocks.SHORT_GRASS, Blocks.GRAVEL, Blocks.SAND, Blocks.WATER, Blocks.LAVA, Blocks.ICE);

	public ClearerItem(final Item.Properties properties) {
		super(properties);
	}

	private ClearerMode getMode(final ItemStack stack) {
		return stack.getOrDefault(ModDataComponents.CLEARER_MODE.get(), ClearerMode.WHITELIST);
	}

	private void setMode(final ItemStack stack, final ClearerMode mode) {
		stack.set(ModDataComponents.CLEARER_MODE.get(), mode);
	}

	@Override
	public InteractionResult use(final Level world, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);

		if (!world.isClientSide() && player instanceof final ServerPlayer serverPlayer) {
			final var currentMode = getMode(heldItem);

			if (serverPlayer.isShiftKeyDown()) {
				final var newMode = currentMode == ClearerMode.ALL ? ClearerMode.WHITELIST : ClearerMode.ALL;
				setMode(heldItem, newMode);
				serverPlayer.sendSystemMessage(Component.translatable(String.format(TestMod3Lang.MESSAGE_CLEARER_MODE_S.getTranslationKey(), newMode)));
			} else {
				final var minX = Mth.floor(serverPlayer.getX() / 16) * 16;
				final var minZ = Mth.floor(serverPlayer.getZ() / 16) * 16;

				serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CLEARER_CLEARING.getTranslationKey(), minX, minZ));

				for (var x = minX; x < minX + 16; x++) {
					for (var z = minZ; z < minZ + 16; z++) {
						for (var y = 0; y < 256; y++) {
							final var pos = new BlockPos(x, y, z);
							final var block = world.getBlockState(pos).getBlock();
							if ((currentMode == ClearerMode.ALL && block != Blocks.BEDROCK) || whitelist.contains(block)) {
								world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
							}
						}
					}
				}

				final var pos = serverPlayer.blockPosition();
				final var state = world.getBlockState(pos);
				world.sendBlockUpdated(pos, state, state, 3);

				serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CLEARER_CLEARED.getTranslationKey()));
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean isFoil(final ItemStack stack) {
		return getMode(stack) == ClearerMode.ALL || super.isFoil(stack);
	}

	public enum ClearerMode implements StringRepresentable {
		WHITELIST(0, "whitelist"),
		ALL(1, "all");

		private static final IntFunction<ClearerMode> BY_ID = ByIdMap.continuous(ClearerMode::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

		public static final Codec<ClearerMode> CODEC = StringRepresentable.fromEnum(ClearerMode::values);
		public static final StreamCodec<ByteBuf, ClearerMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ClearerMode::getId);

		private final int id;
		private final String name;

		ClearerMode(final int id, final String name) {
			this.id = id;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
