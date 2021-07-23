package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.entity.BlockDetectionArrow;
import choonster.testmod3.world.entity.ModArrow;
import choonster.testmod3.world.entity.PlayerAvoidingCreeper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class ModEntities {
	private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<EntityType<ModArrow>> MOD_ARROW = registerEntityType("mod_arrow",
			() -> EntityType.Builder.<ModArrow>of((ModArrow::new), MobCategory.MISC)
					.sized(0.5f, 0.5f)
	);

	public static final RegistryObject<EntityType<BlockDetectionArrow>> BLOCK_DETECTION_ARROW = registerEntityType("block_detection_arrow",
			() -> EntityType.Builder.<BlockDetectionArrow>of(BlockDetectionArrow::new, MobCategory.MISC)
					.sized(0.5f, 0.5f)
	);

	public static final RegistryObject<EntityType<PlayerAvoidingCreeper>> PLAYER_AVOIDING_CREEPER = registerEntityType("player_avoiding_creeper",
			() -> EntityType.Builder.of(PlayerAvoidingCreeper::new, MobCategory.MONSTER)
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
			event.put(PLAYER_AVOIDING_CREEPER.get(), PlayerAvoidingCreeper.registerAttributes().build());
		}
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class SpawnHandler {
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void registerEntitySpawns(final BiomeLoadingEvent event) {
			final ResourceKey<Biome> biomeRegistryKey = ResourceKey.create(ForgeRegistries.Keys.BIOMES, event.getName());

			if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OCEAN)) {
				addSpawn(event, EntityType.GUARDIAN, 100, 5, 20, MobCategory.WATER_CREATURE);
			}

			copySpawns(event, PLAYER_AVOIDING_CREEPER.get(), MobCategory.MONSTER, EntityType.CREEPER, MobCategory.MONSTER);
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
		private static void addSpawn(final BiomeLoadingEvent event, final EntityType<? extends Mob> entityType, final int itemWeight, final int minGroupCount, final int maxGroupCount, final MobCategory classification) {
			final List<MobSpawnSettings.SpawnerData> spawnersList = event.getSpawns()
					.getSpawner(classification);

			// Try to find an existing entry for the entity type
			spawnersList.stream()
					.filter(spawners -> spawners.type == entityType)
					.findFirst()
					.ifPresent(spawnersList::remove); // If there is one, remove it

			// Add a new one
			spawnersList.add(new MobSpawnSettings.SpawnerData(entityType, itemWeight, minGroupCount, maxGroupCount));
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
		private static void copySpawns(final BiomeLoadingEvent event, final EntityType<? extends Mob> entityTypeToAdd, final MobCategory classificationToAdd, final EntityType<? extends Mob> entityTypeToCopy, final MobCategory classificationToCopy) {
			event.getSpawns()
					.getSpawner(classificationToCopy)
					.stream()
					.filter(spawners -> spawners.type == entityTypeToCopy)
					.findFirst()
					.ifPresent(spawners ->
							event.getSpawns().getSpawner(classificationToAdd)
									.add(new MobSpawnSettings.SpawnerData(entityTypeToAdd, spawners.getWeight(), spawners.minCount, spawners.maxCount))
					);
		}
	}
}
