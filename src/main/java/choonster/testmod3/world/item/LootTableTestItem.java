package choonster.testmod3.world.item;

import choonster.testmod3.init.ModLootTables;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Gives the player random loot from a {@link LootTable} when they right-click.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/37754-19-custom-loot-table-for-my-own-structures/
 *
 * @author Choonster
 */
public class LootTableTestItem extends Item {
	public LootTableTestItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if (level instanceof final ServerLevel serverLevel && player instanceof final ServerPlayer serverPlayer) {
			final var lootTable = serverLevel
					.getServer()
					.reloadableRegistries()
					.getLootTable(ModLootTables.LOOT_TABLE_TEST);

			final var state = Blocks.CHEST.defaultBlockState();

			final var lootParams = new LootParams.Builder(serverLevel)
					.withParameter(LootContextParams.THIS_ENTITY, serverPlayer)
					.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, serverPlayer)
					.withParameter(LootContextParams.ATTACKING_ENTITY, serverPlayer)
					.withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, serverPlayer)
					.withParameter(LootContextParams.DAMAGE_SOURCE, level.damageSources().generic())
					.withParameter(LootContextParams.TOOL, serverPlayer.getMainHandItem())
					.withParameter(LootContextParams.ORIGIN, serverPlayer.position())
					.withParameter(LootContextParams.BLOCK_STATE, state)
					.withParameter(
							LootContextParams.BLOCK_ENTITY,
							Objects.requireNonNull(BlockEntityType.CHEST.create(serverPlayer.getOnPos(), state))
					)
					.withParameter(LootContextParams.EXPLOSION_RADIUS, 99.0f)
					.create(LootContextParamSets.ALL_PARAMS);

			final var itemStacks = lootTable.getRandomItems(lootParams);
			for (final var itemStack : itemStacks) {
				ItemHandlerHelper.giveItemToPlayer(serverPlayer, itemStack);
			}

			serverPlayer.inventoryMenu.broadcastChanges();

			if (!itemStacks.isEmpty()) {
				final var lootMessage = getItemStackTextComponent(itemStacks.getFirst());

				IntStream.range(1, itemStacks.size()).forEachOrdered(i -> {
					lootMessage.append(", ");
					lootMessage.append(getItemStackTextComponent(itemStacks.get(i)));
				});

				final var chatMessage = Component.translatable(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_BASE.getTranslationKey(), lootMessage);

				serverPlayer.sendSystemMessage(chatMessage);
			} else {
				serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_NO_LOOT.getTranslationKey()));
			}
		}

		return InteractionResult.SUCCESS;
	}

	/**
	 * Get a {@link MutableComponent} with the quantity and display name of the {@link ItemStack}.
	 *
	 * @param itemStack The ItemStack
	 * @return The ITextComponent
	 */
	private MutableComponent getItemStackTextComponent(final ItemStack itemStack) {
		return Component.translatable(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_ITEM.getTranslationKey(), itemStack.getCount(), itemStack.getDisplayName());
	}
}
