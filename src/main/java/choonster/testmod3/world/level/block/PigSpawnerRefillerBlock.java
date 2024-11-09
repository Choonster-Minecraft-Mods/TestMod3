package choonster.testmod3.world.level.block;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.world.item.component.pigspawner.IPigSpawner;
import choonster.testmod3.world.item.component.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.world.item.component.pigspawner.IPigSpawnerInteractable;
import com.mojang.serialization.MapCodec;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

/**
 * A block that refills any {@link IPigSpawnerFinite} that interacts with it.
 *
 * @author Choonster
 */
public class PigSpawnerRefillerBlock extends Block implements IPigSpawnerInteractable {
	public static final MapCodec<PigSpawnerRefillerBlock> CODEC = simpleCodec(PigSpawnerRefillerBlock::new);

	public PigSpawnerRefillerBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	public IPigSpawner interact(final IPigSpawner pigSpawner, final Level world, final BlockPos pos, @Nullable final CommandSource commandSource) {
		if (pigSpawner instanceof final IPigSpawnerFinite finitePigSpawner) {
			final var newPigSpawner = finitePigSpawner.withNumPigs(finitePigSpawner.maxNumPigs());

			if (commandSource != null) {
				commandSource.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_PIG_SPAWNER_REFILLER_REFILLED.getTranslationKey()));
			}

			return newPigSpawner;
		}

		return null;
	}
}
