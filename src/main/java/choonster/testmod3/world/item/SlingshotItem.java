package choonster.testmod3.world.item;

import choonster.testmod3.capability.lastusetime.LastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
		return LastUseTimeCapability.createProvider(new LastUseTime(false));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
		final InteractionResultHolder<ItemStack> result = super.use(level, player, hand);

		if (result.getResult() == InteractionResult.SUCCESS) {
			LastUseTimeCapability.updateLastUseTime(player, player.getItemInHand(hand));
		}

		return result;
	}
}
