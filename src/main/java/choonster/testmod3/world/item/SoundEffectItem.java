package choonster.testmod3.world.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

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
	private final Supplier<SoundEvent> soundEvent;

	public SoundEffectItem(final Supplier<SoundEvent> soundEvent, final Item.Properties properties) {
		super(properties);
		this.soundEvent = soundEvent;
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
		final Player player = entity instanceof Player ? ((Player) entity) : null;
		entity.level.playSound(player, entity.getX(), entity.getY(), entity.getZ(), soundEvent.get(), SoundSource.PLAYERS, 0.5F, 1.0f);

		return false;
	}
}
