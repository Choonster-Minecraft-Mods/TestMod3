package choonster.testmod3.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.MaxHealthCapability;
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
	public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		if (!world.isRemote) {
			MaxHealthCapability.getMaxHealth(player).ifPresent(maxHealth ->
					player.sendMessage(new TranslationTextComponent("message.testmod3.max_health.get", player.getDisplayName(), player.getMaxHealth(), maxHealth.getBonusMaxHealth()), Util.DUMMY_UUID)
			);
		}

		return ActionResultType.SUCCESS;
	}
}
