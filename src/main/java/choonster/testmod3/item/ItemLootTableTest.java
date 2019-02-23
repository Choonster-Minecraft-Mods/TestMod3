package choonster.testmod3.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModLootTables;
import choonster.testmod3.network.MessagePlayerReceivedLoot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

/**
 * Gives the player random loot from a {@link LootTable} when they right click.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/37754-19-custom-loot-table-for-my-own-structures/
 *
 * @author Choonster
 */
public class ItemLootTableTest extends Item {
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand) {
		if (!worldIn.isRemote) {
			final LootTable lootTable = worldIn.getLootTableManager().getLootTableFromLocation(ModLootTables.LOOT_TABLE_TEST);

			final LootContext lootContext = new LootContext.Builder((WorldServer) worldIn).withPlayer(playerIn).build();

			final List<ItemStack> itemStacks = lootTable.generateLootForPools(itemRand, lootContext);
			for (final ItemStack itemStack : itemStacks) {
				ItemHandlerHelper.giveItemToPlayer(playerIn, itemStack);
			}

			playerIn.inventoryContainer.detectAndSendChanges();

			if (itemStacks.size() > 0) {
				TestMod3.network.sendTo(new MessagePlayerReceivedLoot(itemStacks), (EntityPlayerMP) playerIn);
			} else {
				playerIn.sendMessage(new TextComponentTranslation("message.testmod3:player_received_loot.no_loot"));
			}
		}


		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}
}
