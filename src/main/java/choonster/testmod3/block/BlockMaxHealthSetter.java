package choonster.testmod3.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * A block that adds or removes max health from players who right click it using the {@link IMaxHealth} capability.
 *
 * @author Choonster
 */
public class BlockMaxHealthSetter extends Block {
	public BlockMaxHealthSetter(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!world.isRemote) {
			final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(player);

			if (maxHealth != null) {
				final float healthToAdd = player.isSneaking() ? -1.0f : 1.0f;

				maxHealth.addBonusMaxHealth(healthToAdd);

				player.sendMessage(new TextComponentTranslation("message.testmod3:max_health.add", player.getDisplayName(), healthToAdd));
			}
		}

		return true;
	}
}
