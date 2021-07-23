package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.entity.BlockDetectionArrowEntity;
import choonster.testmod3.entity.ModArrowEntity;
import choonster.testmod3.entity.PlayerAvoidingCreeperEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class ModEntities {
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<EntityType<ModArrowEntity>> MOD_ARROW = registerEntityType("mod_arrow",
			() -> EntityType.Builder.<ModArrowEntity>of((ModArrowEntity::new), EntityClassification.MISC)
					.sized(0.5f, 0.5f)
	);

	public static final RegistryObject<EntityType<BlockDetectionArrowEntity>> BLOCK_DETECTION_ARROW = registerEntityType("block_detection_arrow",
			() -> EntityType.Builder.<BlockDetectionArrowEntity>of(BlockDetectionArrowEntity::new, EntityClassification.MISC)
					.sized(0.5f, 0.5f)
	);

	public static final RegistryObject<EntityType<PlayerAvoidingCreeperEntity>> PLAYER_AVOIDING_CREEPER = registerEntityType("player_avoiding_creeper",
			() -> EntityType.Builder.of(PlayerAvoidingCreeperEntity::new, EntityClassification.MONSTER)
					.sized(0.6f, 1.7f)
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

		ENTITIES.register(modEventBus);

		isInitialised = true;
	}

	/**
	 * Registers an entity type.
	 *
	 * @param name    The registry name of the entity type
	 * @param factory The factory used to create the entity type builder
	 * @return A RegistryObject reference to the entity type
	 */
	private static <T extends Entity> RegistryObject<EntityType<T>> registerEntityType(final String name, final Supplier<EntityType.Builder<T>> factory) {
		return ENTITIES.register(name,
				() -> factory.get().build(new ResourceLocation(TestMod3.MODID, name).toString())
		);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerAttributes(final EntityAttributeCreationEvent event) {
			event.put(PLAYER_AVOIDING_CREEPER.get(), PlayerAvoidingCreeperEntity.registerAttributes().build());
		}
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class SpawnHandler {
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void registerEntitySpawns(final BiomeLoadingEvent event) {
			final RegistryKey<Biome> biomeRegistryKey = RegistryKey.create(ForgeRegistries.Keys.BIOMES, event.getName());

			if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OCEAN)) {
				addSpawn(event, EntityType.GUARDIAN, 100, 5, 20, EntityClassification.WATER_CREATURE);
			}

			copySpawns(event, PLAYER_AVOIDING_CREEPER.get(), EntityClassification.MONSTER, EntityType.CREEPER, EntityClassification.MONSTER);
		}

		/**
		 * Add a spawn entry for the supplied entity to the biome being loaded in {@link BiomeLoadingEvent}.
		 * <p>
		 * Adapted from Forge's {@code EntityRegistry.addSpawn} method in 1.12.2.
		 *
		 * @param event          The event
		 * @param entityType     The entity type
		 * @param itemWeight     The weight of the spawn list entry (higher weights have a higher chance to be chosen)
		 * @param minGroupCount  Min spawn count
		 * @param maxGroupCount  Max spawn count
		 * @param classification The entity classification
		 */
		private static void addSpawn(final BiomeLoadingEvent event, final EntityType<? extends MobEntity> entityType, final int itemWeight, final int minGroupCount, final int maxGroupCount, final EntityClassification classification) {
			final List<MobSpawnInfo.Spawners> spawnersList = event.getSpawns()
					.getSpawner(classification);

			// Try to find an existing entry for the entity type
			spawnersList.stream()
					.filter(spawners -> spawners.type == entityType)
					.findFirst()
					.ifPresent(spawnersList::remove); // If there is one, remove it

			// Add a new one
			spawnersList.add(new MobSpawnInfo.Spawners(entityType, itemWeight, minGroupCount, maxGroupCount));
		}

		/**
		 * Add a spawn list entry for {@code entityTypeToAdd} to the biome being loaded in {@link BiomeLoadingEvent} with an entry for {@code entityTypeToCopy} using the same weight and group count.
		 *
		 * @param event                The event
		 * @param entityTypeToAdd      The entity type to add spawn entries for
		 * @param classificationToAdd  The entity classification to add spawn entries for
		 * @param entityTypeToCopy     The entity type to copy spawn entries from
		 * @param classificationToCopy The entity classification to copy spawn entries from
		 */
		private static void copySpawns(final BiomeLoadingEvent event, final EntityType<? extends MobEntity> entityTypeToAdd, final EntityClassification classificationToAdd, final EntityType<? extends MobEntity> entityTypeToCopy, final EntityClassification classificationToCopy) {
			event.getSpawns()
					.getSpawner(classificationToCopy)
					.stream()
					.filter(spawners -> spawners.type == entityTypeToCopy)
					.findFirst()
					.ifPresent(spawners ->
							event.getSpawns().getSpawner(classificationToAdd)
									.add(new MobSpawnInfo.Spawners(entityTypeToAdd, spawners.weight, spawners.minCount, spawners.maxCount))
					);
		}
	}
}
