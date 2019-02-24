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
 * A block that tells players who right click it their current max health and the bonus max health provided by their {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class BlockMaxHealthGetter extends Block {
	public BlockMaxHealthGetter(final Block.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World worldIn, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!worldIn.isRemote) {
			final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(player);

			if (maxHealth != null) {
				player.sendMessage(new TextComponentTranslation("message.testmod3:max_health.get", player.getDisplayName(), player.getMaxHealth(), maxHealth.getBonusMaxHealth()));
			}
		}

		return true;
	}
}
