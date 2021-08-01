package choonster.testmod3.world.level.block;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerInteractable;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.Util;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

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

	@Override
	public boolean interact(final IPigSpawner pigSpawner, final Level world, final BlockPos pos, @Nullable final CommandSource iCommandSender) {
		if (pigSpawner instanceof final IPigSpawnerFinite pigSpawnerFinite) {
			pigSpawnerFinite.setNumPigs(pigSpawnerFinite.getMaxNumPigs());

			if (iCommandSender != null) {
				iCommandSender.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_PIG_SPAWNER_REFILLER_REFILLED.getTranslationKey()), Util.NIL_UUID);
			}
		}

		return true;
	}
}
