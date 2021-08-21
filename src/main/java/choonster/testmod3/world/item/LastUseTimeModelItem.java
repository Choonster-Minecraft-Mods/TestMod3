package choonster.testmod3.world.item;

import choonster.testmod3.capability.lastusetime.LastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

/**
 * An Item with a different model depending on how long ago it was last used.
 * <p>
 * Doesn't use {@link ItemCooldowns} because that would prevent using the item again before the cooldown has expired.
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
	public InteractionResultHolder<ItemStack> use(final Level level, final Player playerIn, final InteractionHand hand) {
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, playerIn.getItemInHand(hand));
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
		return LastUseTimeCapability.createProvider(new LastUseTime(true));
	}
}
