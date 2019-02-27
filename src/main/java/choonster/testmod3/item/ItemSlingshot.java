package choonster.testmod3.item;

import choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
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
public class ItemSlingshot extends ItemSnowballLauncher {
	public ItemSlingshot(final Item.Properties properties) {
		super(properties);
		CapabilityLastUseTime.TicksSinceLastUseGetter.addToItem(this);
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
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
		return CapabilityLastUseTime.createProvider(new LastUseTime(false));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand) {
		final ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, hand);

		if (result.getType() == EnumActionResult.SUCCESS) {
			CapabilityLastUseTime.updateLastUseTime(playerIn, playerIn.getHeldItem(hand));
		}

		return result;
	}
}
