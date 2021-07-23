package choonster.testmod3.world.item;

import choonster.testmod3.init.ModLootTables;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Gives the player random loot from a {@link LootTable} when they right click.
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
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		if (!world.isClientSide) {
			final LootTable lootTable = Objects.requireNonNull(world.getServer()).getLootTables().get(ModLootTables.LOOT_TABLE_TEST);

			final BlockState state = Blocks.CHEST.defaultBlockState();
			
			final LootContext lootContext = new LootContext.Builder((ServerLevel) world)
					.withParameter(LootContextParams.THIS_ENTITY, player)
					.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player)
					.withParameter(LootContextParams.KILLER_ENTITY, player)
					.withParameter(LootContextParams.DIRECT_KILLER_ENTITY, player)
					.withParameter(LootContextParams.DAMAGE_SOURCE, DamageSource.ANVIL)
					.withParameter(LootContextParams.TOOL, player.getMainHandItem())
					.withParameter(LootContextParams.ORIGIN, player.position())
					.withParameter(LootContextParams.BLOCK_STATE, state)
					.withParameter(LootContextParams.BLOCK_ENTITY, Objects.requireNonNull(BlockEntityType.CHEST.create(player.getOnPos(), state)))
					.withParameter(LootContextParams.EXPLOSION_RADIUS, 99.0f)
					.create(LootContextParamSets.ALL_PARAMS);

			final List<ItemStack> itemStacks = lootTable.getRandomItems(lootContext);
			for (final ItemStack itemStack : itemStacks) {
				ItemHandlerHelper.giveItemToPlayer(player, itemStack);
			}

			player.inventoryMenu.broadcastChanges();

			if (itemStacks.size() > 0) {
				final MutableComponent lootMessage = getItemStackTextComponent(itemStacks.get(0));

				IntStream.range(1, itemStacks.size()).forEachOrdered(i -> {
					lootMessage.append(", ");
					lootMessage.append(getItemStackTextComponent(itemStacks.get(i)));
				});

				final Component chatMessage = new TranslatableComponent(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_BASE.getTranslationKey(), lootMessage);

				player.sendMessage(chatMessage, Util.NIL_UUID);
			} else {
				player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_NO_LOOT.getTranslationKey()), Util.NIL_UUID);
			}
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	/**
	 * Get a {@link MutableComponent} with the quantity and display name of the {@link ItemStack}.
	 *
	 * @param itemStack The ItemStack
	 * @return The ITextComponent
	 */
	private MutableComponent getItemStackTextComponent(final ItemStack itemStack) {
		return new TranslatableComponent(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_ITEM.getTranslationKey(), itemStack.getCount(), itemStack.getDisplayName());
	}
}
