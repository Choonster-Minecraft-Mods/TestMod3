package choonster.testmod3.item;

import choonster.testmod3.init.ModLootTables;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		if (!world.isClientSide) {
			final LootTable lootTable = Objects.requireNonNull(world.getServer()).getLootTables().get(ModLootTables.LOOT_TABLE_TEST);

			final LootContext lootContext = new LootContext.Builder((ServerWorld) world)
					.withParameter(LootParameters.THIS_ENTITY, player)
					.withParameter(LootParameters.LAST_DAMAGE_PLAYER, player)
					.withParameter(LootParameters.KILLER_ENTITY, player)
					.withParameter(LootParameters.DIRECT_KILLER_ENTITY, player)
					.withParameter(LootParameters.DAMAGE_SOURCE, DamageSource.ANVIL)
					.withParameter(LootParameters.TOOL, player.getMainHandItem())
					.withParameter(LootParameters.ORIGIN, player.position())
					.withParameter(LootParameters.BLOCK_STATE, Blocks.CHEST.defaultBlockState())
					.withParameter(LootParameters.BLOCK_ENTITY, Objects.requireNonNull(TileEntityType.CHEST.create()))
					.withParameter(LootParameters.EXPLOSION_RADIUS, 99.0f)
					.create(LootParameterSets.ALL_PARAMS);

			final List<ItemStack> itemStacks = lootTable.getRandomItems(lootContext);
			for (final ItemStack itemStack : itemStacks) {
				ItemHandlerHelper.giveItemToPlayer(player, itemStack);
			}

			player.inventoryMenu.broadcastChanges();

			if (itemStacks.size() > 0) {
				final IFormattableTextComponent lootMessage = getItemStackTextComponent(itemStacks.get(0));

				IntStream.range(1, itemStacks.size()).forEachOrdered(i -> {
					lootMessage.append(", ");
					lootMessage.append(getItemStackTextComponent(itemStacks.get(i)));
				});

				final ITextComponent chatMessage = new TranslationTextComponent(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_BASE.getTranslationKey(), lootMessage);

				player.sendMessage(chatMessage, Util.NIL_UUID);
			} else {
				player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_NO_LOOT.getTranslationKey()), Util.NIL_UUID);
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
	}

	/**
	 * Get an {@link IFormattableTextComponent} with the quantity and display name of the {@link ItemStack}.
	 *
	 * @param itemStack The ItemStack
	 * @return The ITextComponent
	 */
	private IFormattableTextComponent getItemStackTextComponent(final ItemStack itemStack) {
		return new TranslationTextComponent(TestMod3Lang.MESSAGE_PLAYER_RECEIVED_LOOT_ITEM.getTranslationKey(), itemStack.getCount(), itemStack.getDisplayName());
	}
}
