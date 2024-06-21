package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * A spawner that can spawn an infinite number of pigs.
 *
 * @author Choonster
 */
public class InfinitePigSpawner extends BasePigSpawner {
	private static final InfinitePigSpawner INSTANCE = new InfinitePigSpawner();

	public static final Codec<InfinitePigSpawner> CODEC = Codec.unit(INSTANCE);

	@Override
	public boolean canSpawnPig(final Level level, final double x, final double y, final double z) {
		return true;
	}

	@Override
	public List<MutableComponent> getTooltipLines() {
		return ImmutableList.of(Component.translatable(TestMod3Lang.PIG_SPAWNER_INFINITE_DESC.getTranslationKey()));
	}
}
