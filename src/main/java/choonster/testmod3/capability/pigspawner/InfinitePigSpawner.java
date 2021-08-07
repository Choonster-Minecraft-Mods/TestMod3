package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;

/**
 * A spawner that can spawn an infinite number of pigs.
 *
 * @author Choonster
 */
public class InfinitePigSpawner extends BasePigSpawner implements INBTSerializable<CompoundTag> {

	@Override
	public boolean canSpawnPig(final Level level, final double x, final double y, final double z) {
		return true;
	}

	@Override
	public List<MutableComponent> getTooltipLines() {
		return ImmutableList.of(new TranslatableComponent(TestMod3Lang.PIG_SPAWNER_INFINITE_DESC.getTranslationKey()));
	}

	@Override
	public CompoundTag serializeNBT() {
		// No-op
		return new CompoundTag();
	}

	@Override
	public void deserializeNBT(final CompoundTag nbt) {
		// No-op
	}
}
