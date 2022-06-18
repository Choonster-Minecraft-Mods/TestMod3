package choonster.testmod3.world.level.biome.modifier;

import choonster.testmod3.init.levelgen.ModBiomeModifierSerializers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

/**
 * A biome modifier that adds mob spawns based on another mob's spawns.
 *
 * @author Choonster
 */
public record CopyMobSpawnsBiomeModifier(
		EntityType<?> sourceType,
		EntityType<?> destinationType
) implements BiomeModifier {
	public static CopyMobSpawnsBiomeModifier create(final EntityType<?> sourceType, final EntityType<?> destinationType) {
		return new CopyMobSpawnsBiomeModifier(sourceType, destinationType);
	}

	@Override
	public void modify(final Holder<Biome> biome, final Phase phase, final ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase != Phase.MODIFY) {
			return;
		}

		builder.getMobSpawnSettings()
				.getSpawner(sourceType.getCategory())
				.stream()
				.filter(spawnerData -> spawnerData.type == sourceType)
				.map(spawnerData -> new MobSpawnSettings.SpawnerData(
						destinationType,
						spawnerData.getWeight(),
						spawnerData.minCount,
						spawnerData.maxCount
				))
				.toList()
				.forEach(spawnerData -> builder.getMobSpawnSettings().addSpawn(destinationType.getCategory(), spawnerData));
	}

	@Override
	public Codec<CopyMobSpawnsBiomeModifier> codec() {
		return ModBiomeModifierSerializers.COPY_MOB_SPAWNS.get();
	}
}
