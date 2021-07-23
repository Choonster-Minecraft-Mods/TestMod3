package choonster.testmod3.item;

import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * An Item with a different model depending on how long ago it was last used.
 * <p>
 * Doesn't use {@link CooldownTracker} because that would prevent using the item again before the cooldown has expired.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2470478-how-to-do-a-custom-bow-animation
 *
 * @author Choonster
 */
public class LastUseTimeModelItem extends Item {
	public LastUseTimeModelItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(final World worldIn, final PlayerEntity playerIn, final Hand hand) {
		return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getItemInHand(hand));
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
		//noinspection ConstantConditions
		if (LastUseTimeCapability.LAST_USE_TIME_CAPABILITY == null) return null;

		return LastUseTimeCapability.createProvider();
	}
}
