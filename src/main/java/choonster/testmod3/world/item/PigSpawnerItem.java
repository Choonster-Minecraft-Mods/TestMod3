package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.world.item.component.pigspawner.IPigSpawnerFinite;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * A pig spawner item.
 *
 * @author Choonster
 */
public class PigSpawnerItem extends Item {
	private static final float BAR_WIDTH = 13.0f;

	public PigSpawnerItem(final Item.Properties properties) {
		super(properties);
	}

	@Nullable
	private IPigSpawnerFinite getFinitePigSpawner(final ItemStack stack) {
		final var pigSpawner = stack.get(ModDataComponents.PIG_SPAWNER.get());

		if (pigSpawner instanceof final IPigSpawnerFinite finitePigSpawner) {
			return finitePigSpawner;
		}

		return null;
	}

	@Override
	public boolean isBarVisible(final ItemStack stack) {
		final var finitePigSpawner = getFinitePigSpawner(stack);

		return finitePigSpawner != null && finitePigSpawner.numPigs() < finitePigSpawner.maxNumPigs();
	}

	@Override
	public int getBarWidth(final ItemStack stack) {
		final var finitePigSpawner = getFinitePigSpawner(stack);

		if (finitePigSpawner != null) {
			final var maxNumPigs = finitePigSpawner.maxNumPigs();
			final var numPigs = finitePigSpawner.numPigs();

			return Math.round(BAR_WIDTH - ((float) (maxNumPigs - numPigs) / maxNumPigs) * BAR_WIDTH);
		}

		return super.getBarWidth(stack);
	}
}
