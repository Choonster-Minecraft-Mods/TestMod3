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
 * An item that tells the player how much energy is stored in their current chunk's {@link IChunkEnergy}.
 *
 * @author Choonster
 */
public class ChunkEnergyGetterItem extends Item {
	public ChunkEnergyGetterItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
		if (!level.isClientSide) {
			final var chunk = level.getChunkAt(player.blockPosition());
			final var chunkPos = chunk.getPos();

			final var chunkEnergy = ChunkEnergyCapability.getChunkEnergy(chunk).orElseThrow(CapabilityNotPresentException::new);
			player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_CHUNK_ENERGY_GET.getTranslationKey(), chunkPos, chunkEnergy.getEnergyStored()));
		}

		return InteractionResultHolder.success(player.getItemInHand(hand));
	}
}
