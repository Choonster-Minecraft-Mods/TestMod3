package choonster.testmod3.item;

import choonster.testmod3.capability.lastusetime.LastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * A slingshot that fires Snowballs when used.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2483633-custom-bow-animation-and-projectiles
 *
 * @author Vastatio, Choonster
 */
public class SlingshotItem extends SnowballLauncherItem {
	public SlingshotItem(final Item.Properties properties) {
		super(properties);
	}

	/**
	 * Get the cooldown of the launcher (in ticks).
	 *
	 * @param launcher The launcher
	 * @return The cooldown of the launcher (in ticks), or 0 if there is none
	 */
	@Override
	protected int getCooldown(final ItemStack launcher) {
		return 0;
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
		//noinspection ConstantConditions
		if (LastUseTimeCapability.LAST_USE_TIME_CAPABILITY == null) return null;

		return LastUseTimeCapability.createProvider(new LastUseTime(false));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		final ActionResult<ItemStack> result = super.onItemRightClick(world, player, hand);

		if (result.getType() == ActionResultType.SUCCESS) {
			LastUseTimeCapability.updateLastUseTime(player, player.getHeldItem(hand));
		}

		return result;
	}
}
