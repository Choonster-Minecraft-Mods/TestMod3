package choonster.testmod3.world.level.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
	public static final MapCodec<MaxHealthGetterBlock> CODEC = simpleCodec(MaxHealthGetterBlock::new);

	public MaxHealthGetterBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected InteractionResult useWithoutItem(final BlockState state, final Level level, final BlockPos pos, final Player player, final BlockHitResult blockHitResult) {
		if (!level.isClientSide() && player instanceof final ServerPlayer serverPlayer) {
			final var maxHealth = MaxHealthCapability
					.getMaxHealth(player)
					.orElseThrow(CapabilityNotPresentException::new);

			serverPlayer.sendSystemMessage(
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
