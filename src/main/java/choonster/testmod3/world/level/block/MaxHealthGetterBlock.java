package choonster.testmod3.world.level.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A block that tells players who right click it their current max health and the bonus max health provided by their {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class MaxHealthGetterBlock extends Block {
	public MaxHealthGetterBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		if (!world.isClientSide) {
			final var maxHealth = MaxHealthCapability
					.getMaxHealth(player)
					.orElseThrow(CapabilityNotPresentException::new);

			player.sendSystemMessage(
					Component.translatable(
							TestMod3Lang.MESSAGE_MAX_HEALTH_GET.getTranslationKey(),
							player.getDisplayName(),
							player.getMaxHealth(),
							maxHealth.getBonusMaxHealth()
					)
			);
		}

		return InteractionResult.SUCCESS;
	}
}
