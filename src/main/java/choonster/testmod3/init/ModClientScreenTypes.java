package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.lock.LockScreenData;
import choonster.testmod3.client.gui.ClientScreenType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link ClientScreenType ClientScreenTypes}.
 *
 * @author Choonster
 */
public class ModClientScreenTypes {
	public static final ResourceKey<Registry<ClientScreenType<?>>> KEY = ResourceKey.createRegistryKey(
			new ResourceLocation(TestMod3.MODID, "client_screen_type")
	);

	private static final DeferredRegister<ClientScreenType<?>> CLIENT_SCREEN_TYPES = DeferredRegister.create(KEY, TestMod3.MODID);

	public static final Supplier<IForgeRegistry<ClientScreenType<?>>> REGISTRY = CLIENT_SCREEN_TYPES
			.makeRegistry(() -> RegistryBuilder.<ClientScreenType<?>>of().disableSaving());

	private static boolean isInitialised;

	public static final RegistryObject<ClientScreenType<BlockPos>> SURVIVAL_COMMAND_BLOCK = register("survival_command_block",
			BlockPos.STREAM_CODEC.cast()
	);

	public static final RegistryObject<ClientScreenType<Integer>> SURVIVAL_COMMAND_BLOCK_MINECART = register("survival_command_block_minecart",
			ByteBufCodecs.VAR_INT.cast()
	);

	public static final RegistryObject<ClientScreenType<LockScreenData>> LOCK = register("lock",
			LockScreenData.STREAM_CODEC
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

		CLIENT_SCREEN_TYPES.register(modEventBus);

		isInitialised = true;
	}

	private static <T> RegistryObject<ClientScreenType<T>> register(final String name, final StreamCodec<RegistryFriendlyByteBuf, T> codec) {
		return CLIENT_SCREEN_TYPES.register(name, () -> new ClientScreenType<>(codec));
	}
}
