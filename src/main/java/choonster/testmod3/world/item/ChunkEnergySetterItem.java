package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An item that inserts or extracts energy from the {@link IChunkEnergy} of the player's current chunk when left or right-clicked.
 *
 * @author Choonster
 */
public class ChunkEnergySetterItem extends Item implements ILeftClickEmpty {
	public ChunkEnergySetterItem(final Item.Properties properties) {
		super(properties);
	}

	/**
	 * Add or remove chunk energy from the player's current chunk.
	 *
	 * @param world  The level
	 * @param player The player
	 * @param amount The amount to add/remove
	 */
	private void addRemoveChunkEnergy(final Level world, final Player player, final int amount) {
		final var chunk = world.getChunkAt(player.blockPosition());
		final var chunkPos = chunk.getPos();

		final var chunkEnergy = ChunkEnergyCapability.getChunkEnergy(chunk).orElseThrow(CapabilityNotPresentException::new);

		if (player.isShiftKeyDown()) {
			final var energyRemoved = chunkEnergy.extractEnergy(amount, false);
			player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CHUNK_ENERGY_REMOVE.getTranslationKey(), energyRemoved, chunkPos));
		} else {
			final var energyAdded = chunkEnergy.receiveEnergy(amount, false);
			player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CHUNK_ENERGY_ADD.getTranslationKey(), energyAdded, chunkPos));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
		if (!level.isClientSide) {
			addRemoveChunkEnergy(level, player, 1);
		}

		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public void onLeftClickEmpty(final ItemStack stack, final Player player) {
		final var level = player.getCommandSenderWorld();
		if (!level.isClientSide) {
			addRemoveChunkEnergy(level, player, 100);
		}
	}
}
