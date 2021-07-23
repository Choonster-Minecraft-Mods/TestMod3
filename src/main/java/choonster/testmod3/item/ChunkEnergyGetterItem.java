package choonster.testmod3.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
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
	public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity playerIn, final Hand handIn) {
		if (!worldIn.isClientSide) {
			final Chunk chunk = worldIn.getChunkAt(playerIn.blockPosition());
			final ChunkPos chunkPos = chunk.getPos();

			ChunkEnergyCapability.getChunkEnergy(chunk)
					.map(chunkEnergy -> {
						playerIn.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_CHUNK_ENERGY_GET.getTranslationKey(), chunkPos, chunkEnergy.getEnergyStored()), Util.NIL_UUID);
						return true;
					})
					.orElseGet(() -> {
						playerIn.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_CHUNK_ENERGY_NOT_FOUND.getTranslationKey(), chunkPos), Util.NIL_UUID);
						return false;
					});
		}

		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getItemInHand(handIn));
	}
}
