package choonster.testmod3.item;

import choonster.testmod3.api.capability.chunkenergy.IChunkEnergy;
import choonster.testmod3.capability.chunkenergy.CapabilityChunkEnergy;
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
 * An item that inserts or extracts energy from the {@link IChunkEnergy} of the player's current chunk when right clicked.
 *
 * @author Choonster
 */
public class ItemChunkEnergySetter extends ItemTestMod3 {
	public ItemChunkEnergySetter() {
		super("chunk_energy_setter");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if (!worldIn.isRemote) {
			final Chunk chunk = worldIn.getChunkFromBlockCoords(new BlockPos(playerIn));
			final ChunkPos chunkPos = chunk.getPos();
			final IChunkEnergy chunkEnergy = CapabilityChunkEnergy.getChunkEnergy(worldIn, chunkPos);

			if (chunkEnergy != null) {
				if (playerIn.isSneaking()) {
					final int energyRemoved = chunkEnergy.extractEnergy(1, false);
					playerIn.sendMessage(new TextComponentTranslation("message.testmod3:chunk_energy.remove", energyRemoved, chunkPos));
				} else {
					final int energyAdded = chunkEnergy.receiveEnergy(1, false);
					playerIn.sendMessage(new TextComponentTranslation("message.testmod3:chunk_energy.add", energyAdded, chunkPos));

				}
			} else {
				playerIn.sendMessage(new TextComponentTranslation("message.testmod3:chunk_energy.not_found", chunkPos));
			}
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
