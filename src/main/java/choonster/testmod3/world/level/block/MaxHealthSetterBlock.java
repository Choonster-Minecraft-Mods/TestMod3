package choonster.testmod3.world.level.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.CapabilityNotPresentException;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
	public static final MapCodec<MaxHealthSetterBlock> CODEC = simpleCodec(MaxHealthSetterBlock::new);

	public MaxHealthSetterBlock(final Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected InteractionResult useItemOn(final ItemStack stack, final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult blockHitResult) {
		if (!level.isClientSide && player instanceof final ServerPlayer serverPlayer) {
			final var maxHealth = MaxHealthCapability
					.getMaxHealth(serverPlayer)
					.orElseThrow(CapabilityNotPresentException::new);

			final float healthToAdd = serverPlayer.isShiftKeyDown() ? -1.0f : 1.0f;

			maxHealth.addBonusMaxHealth(healthToAdd);

			serverPlayer.sendSystemMessage(
					Component.translatable(
							TestMod3Lang.MESSAGE_MAX_HEALTH_ADD.getTranslationKey(),
							serverPlayer.getDisplayName(),
							healthToAdd
					)
			);
		}

		return InteractionResult.SUCCESS;
	}
}
