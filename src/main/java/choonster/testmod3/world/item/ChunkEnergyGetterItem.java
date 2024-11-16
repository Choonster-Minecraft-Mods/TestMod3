package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

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
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if (!level.isClientSide && player instanceof final ServerPlayer serverPlayer) {
			final var chunk = level.getChunkAt(player.blockPosition());
			final var chunkPos = chunk.getPos().toString();

			final var chunkEnergy = ChunkEnergyCapability.getChunkEnergy(chunk).orElseThrow(CapabilityNotPresentException::new);
			serverPlayer.sendSystemMessage(
					Component.translatable(
							TestMod3Lang.MESSAGE_CHUNK_ENERGY_GET.getTranslationKey(),
							chunkPos,
							chunkEnergy.getEnergyStored()
					)
			);
		}

		return InteractionResult.SUCCESS;
	}
}
