package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.entity.EntityBlockDetectionArrow;
import choonster.testmod3.entity.EntityModArrow;
import choonster.testmod3.entity.EntityPlayerAvoidingCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
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
import java.util.Optional;

import static choonster.testmod3.util.InjectionUtil.Null;

@ObjectHolder(TestMod3.MODID)
public class ModEntities {
	public static final EntityType<EntityModArrow> MOD_ARROW = Null();

	public static final EntityType<EntityBlockDetectionArrow> BLOCK_DETECTION_ARROW = Null();

	public static final EntityType<EntityPlayerAvoidingCreeper> PLAYER_AVOIDING_CREEPER = Null();


	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link Entity} types.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
			final EntityType<?>[] entries = {
					build(
							"mod_arrow",
							EntityType.Builder.create(EntityModArrow.class, EntityModArrow::new)
									.tracker(64, 20, false)
					),

					build(
							"block_detection_arrow",
							EntityType.Builder.create(EntityBlockDetectionArrow.class, EntityBlockDetectionArrow::new)
									.tracker(64, 20, false)
					),

					build(
							"player_avoiding_creeper",
							EntityType.Builder.create(EntityPlayerAvoidingCreeper.class, EntityPlayerAvoidingCreeper::new)
									.tracker(80, 3, true)
					)
			};

			event.getRegistry().registerAll(entries);
		}

		/**
		 * Build an {@link EntityType} from a {@link EntityType.Builder} using the specified name.
		 *
		 * @param name    The entity type name
		 * @param builder The entity type builder to build
		 * @return The built entity type
		 */
		private static EntityType<?> build(final String name, final EntityType.Builder<?> builder) {
			final ResourceLocation registryName = new ResourceLocation(TestMod3.MODID, name);

			return builder
					.build(registryName.toString())
					.setRegistryName(registryName);
		}

		/**
		 * Add this mod's entity spawns.
		 */
		// TODO: Call this from the TestMod3 class?
		public static void addSpawns() {
			addSpawn(EntityType.GUARDIAN, 100, 5, 20, EnumCreatureType.WATER_CREATURE, getBiomes(BiomeDictionary.Type.OCEAN));
			copySpawns(ModEntities.PLAYER_AVOIDING_CREEPER, EnumCreatureType.MONSTER, EntityType.CREEPER, EnumCreatureType.MONSTER);
		}

		/**
		 * Add a spawn entry for the supplied entity in the supplied {@link Biome} list.
		 * <p>
		 * Adapted from Forge's {@code EntityRegistry.addSpawn} method in 1.12.2.
		 *
		 * @param entityType    The entity type
		 * @param itemWeight    The weight of the spawn list entry (higher wrights have a higher chance to be chosen)
		 * @param minGroupCount Min spawn count
		 * @param maxGroupCount Max spawn count
		 * @param creatureType  Type of spawn
		 * @param biomes        The biomes to add the spawn to
		 */
		private static void addSpawn(final EntityType<? extends EntityLiving> entityType, final int itemWeight, final int minGroupCount, final int maxGroupCount, final EnumCreatureType creatureType, final Biome... biomes) {
			for (final Biome biome : biomes) {
				final List<Biome.SpawnListEntry> spawns = biome.getSpawns(creatureType);

				// Try to find an existing entry for the entity type
				final Optional<Biome.SpawnListEntry> optionalSpawnListEntry = spawns.stream()
						.filter(entry -> entry.entityType == entityType)
						.findFirst();

				if (optionalSpawnListEntry.isPresent()) { // If there is one, update it with the new values
					final Biome.SpawnListEntry spawnListEntry = optionalSpawnListEntry.get();
					spawnListEntry.itemWeight = itemWeight;
					spawnListEntry.minGroupCount = minGroupCount;
					spawnListEntry.maxGroupCount = maxGroupCount;
				} else { // Else add a new one
					spawns.add(new Biome.SpawnListEntry(entityType, itemWeight, minGroupCount, maxGroupCount));
				}
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
		 * @param entityTypeToAdd    The entity type to add spawn entries for
		 * @param creatureTypeToAdd  The EnumCreatureType to add spawn entries for
		 * @param entityTypeToCopy   The entity type to copy spawn entries from
		 * @param creatureTypeToCopy The EnumCreatureType to copy spawn entries from
		 */
		private static void copySpawns(final EntityType<? extends EntityLiving> entityTypeToAdd, final EnumCreatureType creatureTypeToAdd, final EntityType<? extends EntityLiving> entityTypeToCopy, final EnumCreatureType creatureTypeToCopy) {
			for (final Biome biome : ForgeRegistries.BIOMES) {
				biome.getSpawns(creatureTypeToCopy).stream()
						.filter(entry -> entry.entityType == entityTypeToCopy)
						.findFirst()
						.ifPresent(spawnListEntry ->
								biome.getSpawns(creatureTypeToAdd).add(new Biome.SpawnListEntry(entityTypeToAdd, spawnListEntry.itemWeight, spawnListEntry.minGroupCount, spawnListEntry.maxGroupCount))
						);
			}
		}
	}
}
