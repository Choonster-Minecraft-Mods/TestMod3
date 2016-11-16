package choonster.testmod3.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

/**
 * An item that plays a sound when left clicked by a player.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38242.0.html
 *
 * @author Choonster
 */
public class ItemSoundEffect extends ItemTestMod3 {
	/**
	 * The {@link SoundEvent} to play when left clicked.
	 */
	private final SoundEvent soundEvent;

	public ItemSoundEffect(String itemName, SoundEvent soundEvent) {
		super(itemName);
		this.soundEvent = soundEvent;
	}

	/**
	 * Called when a entity tries to play the 'swing' animation.
	 *
	 * @param entityLiving The entity swinging the item.
	 * @param stack        The Item stack
	 * @return True to cancel any further processing by EntityLiving
	 */
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		final EntityPlayer player = entityLiving instanceof EntityPlayer ? ((EntityPlayer) entityLiving) : null;
		entityLiving.world.playSound(player, entityLiving.posX, entityLiving.posY, entityLiving.posZ, soundEvent, SoundCategory.PLAYERS, 0.5F, 1.0f);

		return false;
	}
}
