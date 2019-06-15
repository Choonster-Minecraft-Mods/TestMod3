package choonster.testmod3.item;

import choonster.testmod3.init.ModLootTables;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
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
public class ItemLootTableTest extends Item {
	public ItemLootTableTest(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		if (!world.isRemote) {
			final LootTable lootTable = Objects.requireNonNull(world.getServer()).getLootTableManager().getLootTableFromLocation(ModLootTables.LOOT_TABLE_TEST);

			final LootContext lootContext = new LootContext.Builder((WorldServer) world).withPlayer(player).build();

			final List<ItemStack> itemStacks = lootTable.generateLootForPools(random, lootContext);
			for (final ItemStack itemStack : itemStacks) {
				ItemHandlerHelper.giveItemToPlayer(player, itemStack);
			}

			player.inventoryContainer.detectAndSendChanges();

			if (itemStacks.size() > 0) {
				final ITextComponent lootMessage = getItemStackTextComponent(itemStacks.get(0));

				IntStream.range(1, itemStacks.size()).forEachOrdered(i -> {
					lootMessage.appendText(", ");
					lootMessage.appendSibling(getItemStackTextComponent(itemStacks.get(i)));
				});

				final ITextComponent chatMessage = new TextComponentTranslation("message.testmod3.player_received_loot.base", lootMessage);

				player.sendMessage(chatMessage);
			} else {
				player.sendMessage(new TextComponentTranslation("message.testmod3.player_received_loot.no_loot"));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * Get an {@link ITextComponent} with the quantity and display name of the {@link ItemStack}.
	 *
	 * @param itemStack The ItemStack
	 * @return The ITextComponent
	 */
	private ITextComponent getItemStackTextComponent(final ItemStack itemStack) {
		return new TextComponentTranslation("message.testmod3.player_received_loot.item", itemStack.getCount(), itemStack.getTextComponent());
	}
}
