package choonster.testmod3.world.level.biome.modifier;

import choonster.testmod3.init.levelgen.ModBiomeModifierSerializers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A biome modifier that adds mob spawns based on another mob's spawns.
 *
 * @author Choonster
 */
public record CopyMobSpawnsBiomeModifier(
		CategoryAndType source,
		CategoryAndType destination
) implements BiomeModifier {
	public static CopyMobSpawnsBiomeModifier create(
			final MobCategory sourceCategory,
			final EntityType<?> sourceType,
			final MobCategory destinationCategory,
			final EntityType<?> destinationType
	) {
		return new CopyMobSpawnsBiomeModifier(
				new CategoryAndType(sourceCategory, sourceType),
				new CategoryAndType(destinationCategory, destinationType)
		);
	}

	@Override
	public void modify(final Holder<Biome> biome, final Phase phase, final ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase != Phase.MODIFY) {
			return;
		}

		builder.getMobSpawnSettings()
				.getSpawner(source.category)
				.stream()
				.filter(spawnerData -> spawnerData.type == source.type)
				.map(spawnerData -> new MobSpawnSettings.SpawnerData(
						destination.type,
						spawnerData.getWeight(),
						spawnerData.minCount,
						spawnerData.maxCount
				))
				.forEach(spawnerData -> builder.getMobSpawnSettings().addSpawn(destination.category, spawnerData));
	}

	@Override
	public Codec<CopyMobSpawnsBiomeModifier> codec() {
		return ModBiomeModifierSerializers.COPY_MOB_SPAWNS.get();
	}

	public record CategoryAndType(MobCategory category, EntityType<?> type) {
		public static Codec<CategoryAndType> CODEC = RecordCodecBuilder.create(builder ->
				builder.group(

						MobCategory.CODEC
								.fieldOf("category")
								.forGetter(CategoryAndType::category),

						ForgeRegistries.ENTITIES.getCodec()
								.fieldOf("type")
								.forGetter(CategoryAndType::type)

				).apply(builder, CategoryAndType::new)
		);
	}
}
