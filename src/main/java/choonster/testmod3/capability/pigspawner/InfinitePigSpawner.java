package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A spawner that can spawn an infinite number of pigs.
 *
 * @author Choonster
 */
public class InfinitePigSpawner extends BasePigSpawner {

	@Override
	public boolean canSpawnPig(final Level world, final double x, final double y, final double z) {
		return true;
	}

	@Override
	public List<MutableComponent> getTooltipLines() {
		return ImmutableList.of(new TranslatableComponent(TestMod3Lang.PIG_SPAWNER_INFINITE_DESC.getTranslationKey()));
	}
}
