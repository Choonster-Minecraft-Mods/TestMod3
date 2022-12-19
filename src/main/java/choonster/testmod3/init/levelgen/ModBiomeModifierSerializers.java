package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.biome.modifier.AddMobSpawnCostBiomeModifier;
import choonster.testmod3.world.level.biome.modifier.CopyMobSpawnsBiomeModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link BiomeModifier} serializers.
 *
 * @author Choonster
 */
public class ModBiomeModifierSerializers {
	private static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<Codec<CopyMobSpawnsBiomeModifier>> COPY_MOB_SPAWNS = SERIALIZERS.register(
			"copy_mob_spawns",
			() -> RecordCodecBuilder.create(builder ->
					builder.group(

							ForgeRegistries.ENTITY_TYPES.getCodec()
									.fieldOf("sourceType")
									.forGetter(CopyMobSpawnsBiomeModifier::sourceType),

							ForgeRegistries.ENTITY_TYPES.getCodec()
									.fieldOf("destinationType")
									.forGetter(CopyMobSpawnsBiomeModifier::destinationType)

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

							ForgeRegistries.ENTITY_TYPES.getCodec()
									.fieldOf("type")
									.forGetter(AddMobSpawnCostBiomeModifier::type),

							MobSpawnSettings.MobSpawnCost.CODEC
									.fieldOf("spawn_cost")
									.forGetter(AddMobSpawnCostBiomeModifier::spawnCost)

					).apply(builder, AddMobSpawnCostBiomeModifier::new)
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
