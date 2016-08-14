package choonster.testmod3.block;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerInteractable;
import net.minecraft.block.material.Material;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A block that refills any {@link IPigSpawnerFinite} that interacts with it.
 *
 * @author Choonster
 */
public class BlockPigSpawnerRefiller extends BlockTestMod3 implements IPigSpawnerInteractable {
	public BlockPigSpawnerRefiller() {
		super(Material.IRON, "pig_spawner_refiller");
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
	public boolean interact(IPigSpawner pigSpawner, World world, BlockPos pos, @Nullable ICommandSender iCommandSender) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			pigSpawnerFinite.setNumPigs(pigSpawnerFinite.getMaxNumPigs());

			if (iCommandSender != null) {
				iCommandSender.addChatMessage(new TextComponentTranslation("message.testmod3:pig_spawner_refiller.refilled"));
			}
		}

		return true;
	}
}
