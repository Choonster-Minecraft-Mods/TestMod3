package choonster.testmod3.block;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerInteractable;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A block that refills any {@link IPigSpawnerFinite} that interacts with it.
 *
 * @author Choonster
 */
public class PigSpawnerRefillerBlock extends Block implements IPigSpawnerInteractable {
	public PigSpawnerRefillerBlock(final Block.Properties properties) {
		super(properties);
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
	public boolean interact(final IPigSpawner pigSpawner, final World world, final BlockPos pos, @Nullable final ICommandSource iCommandSender) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			final IPigSpawnerFinite pigSpawnerFinite = (IPigSpawnerFinite) pigSpawner;
			pigSpawnerFinite.setNumPigs(pigSpawnerFinite.getMaxNumPigs());

			if (iCommandSender != null) {
				iCommandSender.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_PIG_SPAWNER_REFILLER_REFILLED.getTranslationKey()), Util.DUMMY_UUID);
			}
		}

		return true;
	}
}
