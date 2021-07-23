package choonster.testmod3.world.level.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A block that adds or removes max health from players who right click it using the {@link IMaxHealth} capability.
 *
 * @author Choonster
 */
public class MaxHealthSetterBlock extends Block {
	public MaxHealthSetterBlock(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		if (!world.isClientSide) {
			MaxHealthCapability.getMaxHealth(player).ifPresent(maxHealth -> {
				final float healthToAdd = player.isShiftKeyDown() ? -1.0f : 1.0f;

				maxHealth.addBonusMaxHealth(healthToAdd);

				player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_MAX_HEALTH_ADD.getTranslationKey(), player.getDisplayName(), healthToAdd), Util.NIL_UUID);
			});
		}

		return InteractionResult.SUCCESS;
	}
}
