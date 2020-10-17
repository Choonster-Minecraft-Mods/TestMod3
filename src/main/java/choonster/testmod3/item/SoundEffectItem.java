package choonster.testmod3.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import java.util.function.Supplier;

/**
 * An item that plays a sound when left clicked by a player.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,38242.0.html
 *
 * @author Choonster
 */
public class SoundEffectItem extends Item {
	/**
	 * The {@link SoundEvent} to play when left clicked.
	 */
	private final LazyValue<SoundEvent> soundEvent;

	public SoundEffectItem(final Supplier<SoundEvent> soundEvent, final Item.Properties properties) {
		super(properties);
		this.soundEvent = new LazyValue<>(soundEvent);
	}

	/**
	 * Called when a entity tries to play the 'swing' animation.
	 *
	 * @param entity The entity swinging the item.
	 * @param stack  The Item stack
	 * @return True to cancel any further processing by EntityLiving
	 */
	@Override
	public boolean onEntitySwing(final ItemStack stack, final LivingEntity entity) {
		final PlayerEntity player = entity instanceof PlayerEntity ? ((PlayerEntity) entity) : null;
		entity.world.playSound(player, entity.getPosX(), entity.getPosY(), entity.getPosZ(), soundEvent.getValue(), SoundCategory.PLAYERS, 0.5F, 1.0f);

		return false;
	}
}
