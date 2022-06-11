package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.biome.modifier.AddFeaturesBiomeModifier;
import choonster.testmod3.world.level.biome.modifier.AddMobSpawnBiomeModifier;
import choonster.testmod3.world.level.biome.modifier.AddMobSpawnCostBiomeModifier;
import choonster.testmod3.world.level.biome.modifier.CopyMobSpawnsBiomeModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link BiomeModifier}s.
 *
 * @author Choonster
 */
public class ModBiomeModifierSerializers {
	private static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<Codec<AddMobSpawnBiomeModifier>> ADD_MOB_SPAWN = SERIALIZERS.register(
			"add_mob_spawn",
			() -> RecordCodecBuilder.create(builder ->
					builder.group(

							Biome.LIST_CODEC
									.fieldOf("biomes")
									.forGetter(AddMobSpawnBiomeModifier::biomes),

							MobCategory.CODEC
									.fieldOf("category")
									.forGetter(AddMobSpawnBiomeModifier::category),

							MobSpawnSettings.SpawnerData.CODEC
									.fieldOf("spawner")
									.forGetter(AddMobSpawnBiomeModifier::spawnerData)

					).apply(builder, AddMobSpawnBiomeModifier::new)
			)
	);

	public static final RegistryObject<Codec<CopyMobSpawnsBiomeModifier>> COPY_MOB_SPAWNS = SERIALIZERS.register(
			"copy_mob_spawns",
			() -> RecordCodecBuilder.create(builder ->
					builder.group(
							
							CopyMobSpawnsBiomeModifier.CategoryAndType.CODEC
									.fieldOf("source")
									.forGetter(CopyMobSpawnsBiomeModifier::source),

							CopyMobSpawnsBiomeModifier.CategoryAndType.CODEC
									.fieldOf("destination")
									.forGetter(CopyMobSpawnsBiomeModifier::destination)

					).apply(builder, CopyMobSpawnsBiomeModifier::new)
			)
	);

	public static final RegistryObject<Codec<AddMobSpawnCostBiomeModifier>> ADD_MOB_SPAWN_COST = SERIALIZERS.register(
			"add_mob_spawn_cost",
			() -> RecordCodecBuilder.create(builder ->
					builder.group(

							Biome.LIST_CODEC
									.fieldOf("biomes")
									.forGetter(AddMobSpawnCostBiomeModifier::biomes),

							ForgeRegistries.ENTITIES.getCodec()
									.fieldOf("type")
									.forGetter(AddMobSpawnCostBiomeModifier::type),

							MobSpawnSettings.MobSpawnCost.CODEC
									.fieldOf("spawn_cost")
									.forGetter(AddMobSpawnCostBiomeModifier::spawnCost)

					).apply(builder, AddMobSpawnCostBiomeModifier::new)
			)
	);

	public static final RegistryObject<Codec<AddFeaturesBiomeModifier>> ADD_FEATURES = SERIALIZERS.register(
			"add_features",
			() -> RecordCodecBuilder.create(builder ->
					builder.group(

							Biome.LIST_CODEC
									.fieldOf("biomes")
									.forGetter(AddFeaturesBiomeModifier::biomes),

							PlacedFeature.LIST_CODEC
									.fieldOf("features")
									.forGetter(AddFeaturesBiomeModifier::features),

							GenerationStep.Decoration.CODEC
									.fieldOf("step")
									.forGetter(AddFeaturesBiomeModifier::step)

					).apply(builder, AddFeaturesBiomeModifier::new)
			)
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		SERIALIZERS.register(modEventBus);

		isInitialised = true;
	}
}
