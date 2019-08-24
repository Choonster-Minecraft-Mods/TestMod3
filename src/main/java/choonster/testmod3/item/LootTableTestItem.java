package choonster.testmod3.item;

import choonster.testmod3.init.ModLootTables;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
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
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		if (!world.isRemote) {
			final LootTable lootTable = Objects.requireNonNull(world.getServer()).getLootTableManager().getLootTableFromLocation(ModLootTables.LOOT_TABLE_TEST);

			final LootContext lootContext = new LootContext.Builder((ServerWorld) world)
					.withParameter(LootParameters.THIS_ENTITY, player)
					.withParameter(LootParameters.LAST_DAMAGE_PLAYER, player)
					.withParameter(LootParameters.KILLER_ENTITY, player)
					.withParameter(LootParameters.DIRECT_KILLER_ENTITY, player)
					.withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.ANVIL)
					.withParameter(LootParameters.TOOL, player.getHeldItemMainhand())
					.withParameter(LootParameters.POSITION, player.getPosition())
					.withParameter(LootParameters.BLOCK_STATE, Blocks.CHEST.getDefaultState())
					.withParameter(LootParameters.BLOCK_ENTITY, Objects.requireNonNull(TileEntityType.CHEST.create()))
					.withParameter(LootParameters.EXPLOSION_RADIUS, 99.0f)
					.build(LootParameterSets.GENERIC);

			final List<ItemStack> itemStacks = lootTable.generate(lootContext);
			for (final ItemStack itemStack : itemStacks) {
				ItemHandlerHelper.giveItemToPlayer(player, itemStack);
			}

			player.container.detectAndSendChanges();

			if (itemStacks.size() > 0) {
				final ITextComponent lootMessage = getItemStackTextComponent(itemStacks.get(0));

				IntStream.range(1, itemStacks.size()).forEachOrdered(i -> {
					lootMessage.appendText(", ");
					lootMessage.appendSibling(getItemStackTextComponent(itemStacks.get(i)));
				});

				final ITextComponent chatMessage = new TranslationTextComponent("message.testmod3.player_received_loot.base", lootMessage);

				player.sendMessage(chatMessage);
			} else {
				player.sendMessage(new TranslationTextComponent("message.testmod3.player_received_loot.no_loot"));
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * Get an {@link ITextComponent} with the quantity and display name of the {@link ItemStack}.
	 *
	 * @param itemStack The ItemStack
	 * @return The ITextComponent
	 */
	private ITextComponent getItemStackTextComponent(final ItemStack itemStack) {
		return new TranslationTextComponent("message.testmod3.player_received_loot.item", itemStack.getCount(), itemStack.getTextComponent());
	}
}
