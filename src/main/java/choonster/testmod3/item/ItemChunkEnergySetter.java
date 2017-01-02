package choonster.testmod3.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.CapabilityChunkEnergy;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
public class ItemChunkEnergySetter extends ItemTestMod3 {
	public ItemChunkEnergySetter() {
		super("chunk_energy_setter");
	}

	/**
	 * Add or remove chunk energy from the player's current chunk.
	 *
	 * @param world  The World
	 * @param player The player
	 * @param amount The amount to add/remove
	 */
	private void addRemoveChunkEnergy(World world, EntityPlayer player, int amount) {
		final Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(player));
		final ChunkPos chunkPos = chunk.getPos();
		final IChunkEnergy chunkEnergy = CapabilityChunkEnergy.getChunkEnergy(world, chunkPos);

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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			addRemoveChunkEnergy(worldIn, playerIn, 1);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		final World world = entityLiving.getEntityWorld();
		if (!world.isRemote && entityLiving instanceof EntityPlayer) {
			addRemoveChunkEnergy(world, (EntityPlayer) entityLiving, 100);
		}

		return false;
	}
}
