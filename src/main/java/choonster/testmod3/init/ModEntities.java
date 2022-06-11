package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.entity.BlockDetectionArrow;
import choonster.testmod3.world.entity.ModArrow;
import choonster.testmod3.world.entity.PlayerAvoidingCreeper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
}
