package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.entity.BlockDetectionArrowEntity;
import choonster.testmod3.entity.ModArrowEntity;
import choonster.testmod3.entity.PlayerAvoidingCreeperEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
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

			addSpawn(EntityType.GUARDIAN, 100, 5, 20, EntityClassification.WATER_CREATURE, getBiomes(BiomeDictionary.Type.OCEAN));
			copySpawns(playerAvoidingCreeper, EntityClassification.MONSTER, EntityType.CREEPER, EntityClassification.MONSTER);
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

		/**
		 * Add a spawn entry for the supplied entity in the supplied {@link Biome} list.
		 * <p>
		 * Adapted from Forge's {@code EntityRegistry.addSpawn} method in 1.12.2.
		 *
		 * @param entityType     The entity type
		 * @param itemWeight     The weight of the spawn list entry (higher weights have a higher chance to be chosen)
		 * @param minGroupCount  Min spawn count
		 * @param maxGroupCount  Max spawn count
		 * @param classification The entity classification
		 * @param biomes         The biomes to add the spawn to
		 */
		private static void addSpawn(final EntityType<? extends MobEntity> entityType, final int itemWeight, final int minGroupCount, final int maxGroupCount, final EntityClassification classification, final Biome... biomes) {
			for (final Biome biome : biomes) {
				final List<Biome.SpawnListEntry> spawns = biome.getSpawns(classification);

				// Try to find an existing entry for the entity type
				spawns.stream()
						.filter(entry -> entry.entityType == entityType)
						.findFirst()
						.ifPresent(spawns::remove); // If there is one, remove it

				// Add a new one
				spawns.add(new Biome.SpawnListEntry(entityType, itemWeight, minGroupCount, maxGroupCount));
			}
		}

		/**
		 * Get an array of {@link Biome}s with the specified {@link BiomeDictionary.Type}.
		 *
		 * @param type The Type
		 * @return An array of Biomes
		 */
		private static Biome[] getBiomes(final BiomeDictionary.Type type) {
			return BiomeDictionary.getBiomes(type).toArray(new Biome[0]);
		}

		/**
		 * Add a spawn list entry for {@code entityTypeToAdd} in each {@link Biome} with an entry for {@code entityTypeToCopy} using the same weight and group count.
		 *
		 * @param entityTypeToAdd      The entity type to add spawn entries for
		 * @param classificationToAdd  The entity classification to add spawn entries for
		 * @param entityTypeToCopy     The entity type to copy spawn entries from
		 * @param classificationToCopy The entity classification to copy spawn entries from
		 */
		private static void copySpawns(final EntityType<? extends MobEntity> entityTypeToAdd, final EntityClassification classificationToAdd, final EntityType<? extends MobEntity> entityTypeToCopy, final EntityClassification classificationToCopy) {
			for (final Biome biome : ForgeRegistries.BIOMES) {
				biome.getSpawns(classificationToCopy).stream()
						.filter(entry -> entry.entityType == entityTypeToCopy)
						.findFirst()
						.ifPresent(spawnListEntry ->
								biome.getSpawns(classificationToAdd).add(new Biome.SpawnListEntry(entityTypeToAdd, spawnListEntry.itemWeight, spawnListEntry.minGroupCount, spawnListEntry.maxGroupCount))
						);
			}
		}
	}
}
