package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * An item that tells the player how much energy is stored in their current chunk's {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class ChunkEnergyGetterItem extends Item {
	public ChunkEnergyGetterItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player playerIn, final InteractionHand handIn) {
		if (!level.isClientSide) {
			final LevelChunk chunk = level.getChunkAt(playerIn.blockPosition());
			final ChunkPos chunkPos = chunk.getPos();

			ChunkEnergyCapability.getChunkEnergy(chunk)
					.map(chunkEnergy -> {
						playerIn.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CHUNK_ENERGY_GET.getTranslationKey(), chunkPos, chunkEnergy.getEnergyStored()));
						return true;
					})
					.orElseGet(() -> {
						playerIn.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CHUNK_ENERGY_NOT_FOUND.getTranslationKey(), chunkPos));
						return false;
					});
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(handIn));
	}
}
