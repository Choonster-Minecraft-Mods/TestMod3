package choonster.testmod3.block;

import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.maxhealth.CapabilityMaxHealth;
import net.minecraft.block.material.Material;
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
public class BlockMaxHealthGetter extends BlockTestMod3 {
	public BlockMaxHealthGetter() {
		super(Material.IRON, "max_health_getter");
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (!worldIn.isRemote) {
			final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(playerIn);

			if (maxHealth != null) {
				playerIn.sendMessage(new TextComponentTranslation("message.testmod3:max_health.get", playerIn.getDisplayName(), playerIn.getMaxHealth(), maxHealth.getBonusMaxHealth()));
			}
		}

		return true;
	}
}
