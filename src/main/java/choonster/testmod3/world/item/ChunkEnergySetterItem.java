package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * An item that inserts or extracts energy from the {@link IChunkEnergy} of the player's current chunk when left or right clicked.
 *
 * @author Choonster
 */
public class ChunkEnergySetterItem extends Item {
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
		final LevelChunk chunk = world.getChunkAt(player.blockPosition());
		final ChunkPos chunkPos = chunk.getPos();

		ChunkEnergyCapability.getChunkEnergy(chunk)
				.map(chunkEnergy -> {
					if (player.isShiftKeyDown()) {
						final int energyRemoved = chunkEnergy.extractEnergy(amount, false);
						player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_CHUNK_ENERGY_REMOVE.getTranslationKey(), energyRemoved, chunkPos), Util.NIL_UUID);
					} else {
						final int energyAdded = chunkEnergy.receiveEnergy(amount, false);
						player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_CHUNK_ENERGY_ADD.getTranslationKey(), energyAdded, chunkPos), Util.NIL_UUID);
					}

					return true;
				})
				.orElseGet(() -> {
					player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_CHUNK_ENERGY_NOT_FOUND.getTranslationKey(), chunkPos), Util.NIL_UUID);

					return false;
				});
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player playerIn, final InteractionHand handIn) {
		if (!level.isClientSide) {
			addRemoveChunkEnergy(level, playerIn, 1);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
	}

	@Override
	public boolean onEntitySwing(final ItemStack stack, final LivingEntity entity) {
		final Level world = entity.getCommandSenderWorld();
		if (!world.isClientSide && entity instanceof Player) {
			addRemoveChunkEnergy(world, (Player) entity, 100);
		}

		return false;
	}
}
