package choonster.testmod3.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

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
	public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		if (!world.isRemote) {
			MaxHealthCapability.getMaxHealth(player).ifPresent(maxHealth -> {
				final float healthToAdd = player.isSneaking() ? -1.0f : 1.0f;

				maxHealth.addBonusMaxHealth(healthToAdd);

				player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_MAX_HEALTH_ADD.getTranslationKey(), player.getDisplayName(), healthToAdd), Util.DUMMY_UUID);
			});
		}

		return ActionResultType.SUCCESS;
	}
}
