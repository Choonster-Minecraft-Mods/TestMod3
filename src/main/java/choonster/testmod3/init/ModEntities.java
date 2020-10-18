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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;

import static choonster.testmod3.util.InjectionUtil.Null;

@ObjectHolder(TestMod3.MODID)
public class ModEntities {
	public static final EntityType<ModArrowEntity> MOD_ARROW = Null();

	public static final EntityType<BlockDetectionArrowEntity> BLOCK_DETECTION_ARROW = Null();

	public static final EntityType<PlayerAvoidingCreeperEntity> PLAYER_AVOIDING_CREEPER = Null();
	
	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Entity} types.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
			final EntityType<ModArrowEntity> modArrow = build(
					"mod_arrow",
					EntityType.Builder.<ModArrowEntity>create((ModArrowEntity::new), EntityClassification.MISC)
							.size(0.5f, 0.5f)
			);

			final EntityType<BlockDetectionArrowEntity> blockDetectionArrow = build(
					"block_detection_arrow",
					EntityType.Builder.<BlockDetectionArrowEntity>create(BlockDetectionArrowEntity::new, EntityClassification.MISC)
							.size(0.5f, 0.5f)

			);
			final EntityType<PlayerAvoidingCreeperEntity> playerAvoidingCreeper = build(
					"player_avoiding_creeper",
					EntityType.Builder.create(PlayerAvoidingCreeperEntity::new, EntityClassification.MONSTER)
							.size(0.6f, 1.7f)
			);

			event.getRegistry().registerAll(
					modArrow,
					blockDetectionArrow,
					playerAvoidingCreeper
			);
		}

		/**
		 * Build an {@link EntityType} from a {@link EntityType.Builder} using the specified name.
		 *
		 * @param name    The entity type name
		 * @param builder The entity type builder to build
		 * @return The built entity type
		 */
		private static <T extends Entity> EntityType<T> build(final String name, final EntityType.Builder<T> builder) {
			final ResourceLocation registryName = new ResourceLocation(TestMod3.MODID, name);

			final EntityType<T> entityType = builder
					.build(registryName.toString());

			entityType.setRegistryName(registryName);

			return entityType;
		}
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class SpawnHandler {
		@SubscribeEvent(priority = EventPriority.LOW)
		public static void registerEntitySpawns(final BiomeLoadingEvent event) {
			final RegistryKey<Biome> biomeRegistryKey = RegistryKey.getOrCreateKey(ForgeRegistries.Keys.BIOMES, event.getName());

			if (BiomeDictionary.hasType(biomeRegistryKey, BiomeDictionary.Type.OCEAN)) {
				addSpawn(event, EntityType.GUARDIAN, 100, 5, 20, EntityClassification.WATER_CREATURE);
			}

			copySpawns(event, PLAYER_AVOIDING_CREEPER, EntityClassification.MONSTER, EntityType.CREEPER, EntityClassification.MONSTER);
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
									.add(new MobSpawnInfo.Spawners(entityTypeToAdd, spawners.itemWeight, spawners.minCount, spawners.maxCount))
					);
		}
	}
}
