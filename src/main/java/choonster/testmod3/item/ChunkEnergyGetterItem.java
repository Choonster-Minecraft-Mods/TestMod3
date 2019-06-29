package choonster.testmod3.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

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
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final PlayerEntity playerIn, final Hand handIn) {
		if (!worldIn.isRemote) {
			final Chunk chunk = worldIn.getChunkAt(new BlockPos(playerIn));
			final ChunkPos chunkPos = chunk.getPos();

			ChunkEnergyCapability.getChunkEnergy(chunk)
					.map(chunkEnergy -> {
						playerIn.sendMessage(new TranslationTextComponent("message.testmod3.chunk_energy.get", chunkPos, chunkEnergy.getEnergyStored()));
						return true;
					})
					.orElseGet(() -> {
						playerIn.sendMessage(new TranslationTextComponent("message.testmod3.chunk_energy.not_found", chunkPos));
						return false;
					});
		}

		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
