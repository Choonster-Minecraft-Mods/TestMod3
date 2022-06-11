package choonster.testmod3.world.level.biome.modifier;

import choonster.testmod3.init.levelgen.ModBiomeModifierSerializers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

/**
 * A biome modifier that adds a mob spawn.
 */
public record AddMobSpawnBiomeModifier(
		HolderSet<Biome> biomes,
		MobCategory category,
		MobSpawnSettings.SpawnerData spawnerData
) implements BiomeModifier {
	public static AddMobSpawnBiomeModifier create(
			final HolderSet<Biome> biomes,
			final MobCategory category,
			final EntityType<?> type,
			final int weight,
			final int minCount,
			final int maxCount
	) {
		return new AddMobSpawnBiomeModifier(
				biomes,
				category,
				new MobSpawnSettings.SpawnerData(type, weight, minCount, maxCount)
		);
	}
	
	@Override
	public void modify(final Holder<Biome> biome, final Phase phase, final ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD && biomes.contains(biome)) {
			builder.getMobSpawnSettings().addSpawn(category, spawnerData);
		}
	}

	@Override
	public Codec<AddMobSpawnBiomeModifier> codec() {
		return ModBiomeModifierSerializers.ADD_MOB_SPAWN.get();
	}
}
