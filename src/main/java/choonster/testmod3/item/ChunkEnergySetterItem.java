package choonster.testmod3.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import net.minecraft.entity.LivingEntity;
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
	 * @param world  The World
	 * @param player The player
	 * @param amount The amount to add/remove
	 */
	private void addRemoveChunkEnergy(final World world, final PlayerEntity player, final int amount) {
		final Chunk chunk = world.getChunkAt(new BlockPos(player));
		final ChunkPos chunkPos = chunk.getPos();

		ChunkEnergyCapability.getChunkEnergy(chunk)
				.map(chunkEnergy -> {
					if (player.isSneaking()) {
						final int energyRemoved = chunkEnergy.extractEnergy(amount, false);
						player.sendMessage(new TranslationTextComponent("message.testmod3.chunk_energy.remove", energyRemoved, chunkPos));
					} else {
						final int energyAdded = chunkEnergy.receiveEnergy(amount, false);
						player.sendMessage(new TranslationTextComponent("message.testmod3.chunk_energy.add", energyAdded, chunkPos));

					}

					return true;
				})
				.orElseGet(() -> {
					player.sendMessage(new TranslationTextComponent("message.testmod3.chunk_energy.not_found", chunkPos));

					return false;
				});
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final PlayerEntity playerIn, final Hand handIn) {
		if (!worldIn.isRemote) {
			addRemoveChunkEnergy(worldIn, playerIn, 1);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public boolean onEntitySwing(final ItemStack stack, final LivingEntity entity) {
		final World world = entity.getEntityWorld();
		if (!world.isRemote && entity instanceof PlayerEntity) {
			addRemoveChunkEnergy(world, (PlayerEntity) entity, 100);
		}

		return false;
	}
}
