package com.choonster.testmod3.block;

import com.choonster.testmod3.api.pigspawner.IPigSpawner;
import com.choonster.testmod3.api.pigspawner.IPigSpawnerFinite;
import com.choonster.testmod3.api.pigspawner.IPigSpawnerInteractable;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.Optional;

/**
 * A block that refills any {@link IPigSpawnerFinite} that interacts with it.
 *
 * @author Choonster
 */
public class BlockPigSpawnerRefiller extends BlockTestMod3 implements IPigSpawnerInteractable {
	public BlockPigSpawnerRefiller() {
		super(Material.iron, "pigSpawnerRefiller");
	}

	/**
	 * Interact with the {@link IPigSpawner}. Only called on the server.
	 *
	 * @param pigSpawner     The IPigSpawner
	 * @param world          The World
	 * @param pos            The position of this object
	 * @param iCommandSender The ICommandSender that caused the interaction, if any
	 * @return {@code true} to prevent the default action of the IPigSpawner
	 */
	@Override
	public boolean interact(IPigSpawner pigSpawner, World world, BlockPos pos, Optional<ICommandSender> iCommandSender) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			pigSpawnerFinite.setNumPigs(pigSpawnerFinite.getMaxNumPigs());

			if (iCommandSender.isPresent()) {
				iCommandSender.get().addChatMessage(new ChatComponentTranslation("message.pigSpawnerRefiller.refilled"));
			}
		}

		return true;
	}
}
