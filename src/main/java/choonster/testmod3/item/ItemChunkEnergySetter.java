package choonster.testmod3.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.CapabilityChunkEnergy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * An item that inserts or extracts energy from the {@link IChunkEnergy} of the player's current chunk when left or right clicked.
 *
 * @author Choonster
 */
public class ItemChunkEnergySetter extends Item {
	public ItemChunkEnergySetter(final Item.Properties properties) {
		super(properties);
	}

	/**
	 * Add or remove chunk energy from the player's current chunk.
	 *
	 * @param world  The World
	 * @param player The player
	 * @param amount The amount to add/remove
	 */
	private void addRemoveChunkEnergy(final World world, final EntityPlayer player, final int amount) {
		final Chunk chunk = world.getChunk(new BlockPos(player));
		final ChunkPos chunkPos = chunk.getPos();
		final IChunkEnergy chunkEnergy = CapabilityChunkEnergy.getChunkEnergy(chunk);

		if (chunkEnergy != null) {
			if (player.isSneaking()) {
				final int energyRemoved = chunkEnergy.extractEnergy(amount, false);
				player.sendMessage(new TextComponentTranslation("message.testmod3:chunk_energy.remove", energyRemoved, chunkPos));
			} else {
				final int energyAdded = chunkEnergy.receiveEnergy(amount, false);
				player.sendMessage(new TextComponentTranslation("message.testmod3:chunk_energy.add", energyAdded, chunkPos));

			}
		} else {
			player.sendMessage(new TextComponentTranslation("message.testmod3:chunk_energy.not_found", chunkPos));
		}
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
		if (!worldIn.isRemote) {
			addRemoveChunkEnergy(worldIn, playerIn, 1);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public boolean onEntitySwing(final ItemStack stack, final EntityLivingBase entity) {
		final World world = entity.getEntityWorld();
		if (!world.isRemote && entity instanceof EntityPlayer) {
			addRemoveChunkEnergy(world, (EntityPlayer) entity, 100);
		}

		return false;
	}
}
