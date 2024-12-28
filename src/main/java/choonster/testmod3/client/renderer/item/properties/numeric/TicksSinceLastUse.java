package choonster.testmod3.client.renderer.item.properties.numeric;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.world.item.component.lastusetime.LastUseTimeProperties;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * {@link RangeSelectItemModelProperty} to get the ticks since the last use of the item,
 * as recorded by the item's {@link LastUseTimeProperties} component.
 * <p>
 * Returns the number of ticks since the last use time of the item, or -1 if the required information isn't available.
 */
public record TicksSinceLastUse() implements RangeSelectItemModelProperty {
	public static final MapCodec<TicksSinceLastUse> MAP_CODEC = MapCodec.unit(new TicksSinceLastUse());

	@Override
	public float get(
			final ItemStack stack,
			@Nullable final ClientLevel clientLevel,
			@Nullable final LivingEntity entity,
			final int seed
	) {
		final var level = clientLevel != null ? clientLevel : entity != null ? entity.level() : null;

		if (level == null) {
			return -1;
		}

		return Optional.ofNullable(stack.get(ModDataComponents.LAST_USE_TIME_PROPERTIES.get()))
				.map(properties -> level.getGameTime() - properties.lastUseTime())
				.orElse(-1L);
	}

	@Override
	public MapCodec<? extends RangeSelectItemModelProperty> type() {
		return MAP_CODEC;
	}
}
