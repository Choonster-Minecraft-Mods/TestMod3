package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.entity.EntityBlockDetectionArrow;
import choonster.testmod3.entity.EntityModArrow;
import choonster.testmod3.entity.EntityPlayerAvoidingCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

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

			// TODO: Figure out how to add spawns in 1.13
//			addSpawns();
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

		/*
		 *//**
		 * Add this mod's entity spawns.
		 *//*
		private static void addSpawns() {
			EntityRegistry.addSpawn(EntityGuardian.class, 100, 5, 20, EnumCreatureType.WATER_CREATURE, getBiomes(BiomeDictionary.Type.OCEAN));
			copySpawns(EntityPlayerAvoidingCreeper.class, EnumCreatureType.MONSTER, EntityCreeper.class, EnumCreatureType.MONSTER);
		}

		*//**
		 * Get an array of {@link Biome}s with the specified {@link BiomeDictionary.Type}.
		 *
		 * @param type The Type
		 * @return An array of Biomes
		 *//*
		private static Biome[] getBiomes(final BiomeDictionary.Type type) {
			return BiomeDictionary.getBiomes(type).toArray(new Biome[0]);
		}

		*//**
		 * Add a spawn list entry for {@code classToAdd} in each {@link Biome} with an entry for {@code classToCopy} using the same weight and group count.
		 *
		 * @param classToAdd         The class to add spawn entries for
		 * @param creatureTypeToAdd  The EnumCreatureType to add spawn entries for
		 * @param classToCopy        The class to copy spawn entries from
		 * @param creatureTypeToCopy The EnumCreatureType to copy spawn entries from
		 *//*
		private static void copySpawns(final Class<? extends EntityLiving> classToAdd, final EnumCreatureType creatureTypeToAdd, final Class<? extends EntityLiving> classToCopy, final EnumCreatureType creatureTypeToCopy) {
			for (final Biome biome : ForgeRegistries.BIOMES) {
				biome.getSpawnableList(creatureTypeToCopy).stream()
						.filter(entry -> entry.entityClass == classToCopy)
						.findFirst()
						.ifPresent(spawnListEntry ->
								biome.getSpawnableList(creatureTypeToAdd).add(new Biome.SpawnListEntry(classToAdd, spawnListEntry.itemWeight, spawnListEntry.minGroupCount, spawnListEntry.maxGroupCount))
						);
			}
		}
		*/
	}
}
